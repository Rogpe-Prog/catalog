package com.heroes.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.heroes.dscatalog.repositories.ProductRepository;
import com.heroes.dscatalog.services.exceptions.DatabaseException;
import com.heroes.dscatalog.services.exceptions.ResourceNotFoundExecption;

@SpringBootTest
public class ProductServicesIT {
	
	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
		
	}

	@Test
	public void deleteShouldDeleteResourceWhenIdExist() {
		
		service.delete(existingId);
		
		Assertions.assertEquals(countTotalProducts - 1, repository.count());
		
	}
	
	@Test
	public void deleteShouldTrhowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundExecption.class, () -> {
			service.delete(nonExistingId);
		});
		
	}
	
	
}
