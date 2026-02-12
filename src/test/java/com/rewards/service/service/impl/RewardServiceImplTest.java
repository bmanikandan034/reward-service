package com.rewards.service.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rewards.service.dto.RewardResponseDTO;
import com.rewards.service.entity.Customer;
import com.rewards.service.entity.Transaction;
import com.rewards.service.error.CustomerNotFoundException;
import com.rewards.service.repository.CustomerRepository;
import com.rewards.service.repository.TransactionRepository;
import com.rewards.service.util.RewardCalculator;
/***
 * @author Manikandan B
 */
@ExtendWith(MockitoExtension.class)
class RewardServiceImplTest {

	@InjectMocks
	private RewardServiceImpl rewardService;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private TransactionRepository transactionRepository;

	private Long customerId;
	private Customer customer;
	private Transaction transaction1;
	private Transaction transaction2;

	@BeforeEach
	void setUp() {
		customerId = 1L;

		customer = new Customer();
		customer.setCustomerId(customerId);
		customer.setName("Manikandan");

		transaction1 = new Transaction();
		transaction1.setCustomer(customer);
		transaction1.setAmount(120.0);
		transaction1.setDate(LocalDate.of(2025, 1, 10));

		transaction2 = new Transaction();
		transaction2.setCustomer(customer);
		transaction2.setAmount(80.0);
		transaction2.setDate(LocalDate.of(2025, 1, 20));
	}

	@Test
    void testGetRewards_WithDateRange() {

        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        when(transactionRepository.findByCustomerIdAndDateBetween(
                eq(customerId), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(transaction1, transaction2));

        try (MockedStatic<RewardCalculator> mockedStatic = mockStatic(RewardCalculator.class)) {

        	mockedStatic.when(() -> RewardCalculator.calculate(120.0)).thenReturn(90);
            mockedStatic.when(() -> RewardCalculator.calculate(80.0)).thenReturn(30);

            RewardResponseDTO response = rewardService.getRewards(
                    customerId,
                    LocalDate.of(2025, 1, 1),
                    LocalDate.of(2025, 1, 31)
            );

            assertNotNull(response);
            assertEquals(customerId, response.getCustomerId());
            assertEquals("Manikandan", response.getCustomerName());
            assertEquals(120L, response.getTotalRewards());

            Map<String, Long> monthly = response.getMonthlyRewards();
            assertEquals(1, monthly.size());
            assertTrue(monthly.containsKey(YearMonth.of(2025, 1).toString()));

            verify(transactionRepository, times(1))
                    .findByCustomerIdAndDateBetween(eq(customerId), any(), any());
        }
    }

	@Test
    void testGetRewards_WithoutDateRange() {

        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        when(transactionRepository.findByCustomerId(customerId))
                .thenReturn(List.of(transaction1));

        try (MockedStatic<RewardCalculator> mockedStatic = mockStatic(RewardCalculator.class)) {

        	mockedStatic.when(() -> RewardCalculator.calculate(120.0)).thenReturn(90);

            RewardResponseDTO response =
                    rewardService.getRewards(customerId, null, null);

            assertEquals(90L, response.getTotalRewards());

            verify(transactionRepository, times(1))
                    .findByCustomerId(customerId);
        }
    }

	@Test
	void testGetRewards_InvalidCustomerId() {

		assertThrows(IllegalArgumentException.class, () -> rewardService.getRewards(0L, null, null));
	}

	@Test
	void testGetRewards_InvalidDateRange() {

		assertThrows(IllegalArgumentException.class,
				() -> rewardService.getRewards(customerId, LocalDate.of(2025, 2, 1), LocalDate.of(2025, 1, 1)));
	}

	@Test
    void testGetRewards_CustomerNotFound() {

        when(customerRepository.existsById(customerId)).thenReturn(false);

        assertThrows(CustomerNotFoundException.class, () ->
                rewardService.getRewards(customerId, null, null));

        verify(customerRepository, times(1)).existsById(customerId);
        verify(transactionRepository, never()).findByCustomerId(any());
    }
}
