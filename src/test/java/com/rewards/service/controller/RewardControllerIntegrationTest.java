package com.rewards.service.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import com.rewards.service.entity.Customer;
import com.rewards.service.entity.Transaction;
import com.rewards.service.repository.CustomerRepository;
import com.rewards.service.repository.TransactionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RewardControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	private Customer customer;

	@BeforeEach
	void setup() {
		transactionRepository.deleteAll();
		customerRepository.deleteAll();

		customer = customerRepository.save(new Customer(null, "John"));

		transactionRepository.save(new Transaction(null, 120.0, LocalDate.now().minusMonths(1), customer));
	}

	@Test
	void shouldReturnRewardsForValidCustomer() throws Exception {

		mockMvc.perform(get("/api/rewards/{customerId}", customer.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true));
	}

	@Test
	void shouldReturnNotFoundForInvalidCustomer() throws Exception {

		mockMvc.perform(get("/api/rewards/{customerId}", 999)).andExpect(status().is4xxClientError());
	}

	@Test
	void shouldFilterByDateRange() throws Exception {

		mockMvc.perform(get("/api/rewards/{customerId}", customer.getId())
				.param("startDate", LocalDate.now().minusMonths(2).toString())
				.param("endDate", LocalDate.now().toString())).andExpect(status().isOk());
	}
}