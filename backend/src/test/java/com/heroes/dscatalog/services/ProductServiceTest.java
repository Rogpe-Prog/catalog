package com.heroes.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.heroes.dscatalog.repositories.ProductRepository;
import com.heroes.dscatalog.services.exceptions.DatabaseException;
import com.heroes.dscatalog.services.exceptions.ResourceNotFoundExecption;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {
	
	private long exintingId;
	private long nonExistingId;	
	private long dependetId;
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@BeforeEach
	void setUp() throws Exception {
		exintingId = 1L;
		nonExistingId = 1000L;
		dependetId = 4L;
				
		Mockito.doNothing().when(repository).deleteById(exintingId);
		
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
	
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependetId);
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
