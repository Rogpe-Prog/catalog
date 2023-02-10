package com.heroes.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.heroes.dscatalog.dto.ProductDTO;
import com.heroes.dscatalog.entities.Product;
import com.heroes.dscatalog.repositories.ProductRepository;
import com.heroes.dscatalog.services.exceptions.DatabaseException;
import com.heroes.dscatalog.services.exceptions.ResourceNotFoundExecption;
import com.heroes.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
	
	private long exintingId;
	private long nonExistingId;	
	private long dependetId;
	private PageImpl<Product> page;
	private Product product;
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@BeforeEach
	void setUp() throws Exception {
		exintingId = 1L;
		nonExistingId = 1000L;
		dependetId = 4L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);		
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.when(repository.findById(exintingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
				
		Mockito.doNothing().when(repository).deleteById(exintingId);
		
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
	
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependetId);
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository).findAll(pageable);
		
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependetId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependetId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExecptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundExecption.class, () -> {
			service.delete(nonExistingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(exintingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).deleteById(exintingId);
	}
	
	

}
