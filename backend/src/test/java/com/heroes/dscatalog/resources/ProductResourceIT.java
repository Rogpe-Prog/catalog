package com.heroes.dscatalog.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heroes.dscatalog.dto.ProductDTO;
import com.heroes.dscatalog.tests.Factory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
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
	public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
		
		ResultActions result =  
				mockMvc.perform(get("/products?page=0&size=12&sort=name,asc", existingId)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		
	}
	
	
	@Test
	public void updateShouldReturnProductDTOWhenIdNonexists() throws Exception {
		
		ProductDTO productDTO = Factory.createProductDTO();
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
				
		String expectName = productDTO.getName();
		
		
				ResultActions result =  
						mockMvc.perform(put("/products/{id}", existingId)
								.content(jsonBody)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON));
				
				result.andExpect(status().isOk());
				result.andExpect(jsonPath("$.id").exists());
				result.andExpect(jsonPath("$.name").value(expectName));

	}	
	
	@Test
	public void updateShouldReturnNotFoundWhenIdNonexists() throws Exception {
		
		ProductDTO productDTO = Factory.createProductDTO();
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);		
		
				ResultActions result =  
						mockMvc.perform(put("/products/{id}", nonExistingId)
								.content(jsonBody)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON));
				
				result.andExpect(status().isNotFound());
				
	}

}
