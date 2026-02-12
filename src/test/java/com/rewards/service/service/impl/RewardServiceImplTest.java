package com.rewards.service.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rewards.service.dto.RewardResponseDTO;
import com.rewards.service.entity.Transaction;
import com.rewards.service.error.ResourceNotFoundException;
import com.rewards.service.repository.TransactionRepository;

class RewardServiceImplTest {

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private RewardServiceImpl rewardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCalculateRewardsSuccessfully() {

        Long customerId = 1L;

        List<Transaction> transactions = List.of(
                new Transaction(1L, 120.0, LocalDate.now().minusMonths(1), null),
                new Transaction(2L, 75.0, LocalDate.now().minusMonths(1), null),
                new Transaction(3L, 40.0, LocalDate.now(), null)
        );

        when(repository.findByCustomerId(customerId)).thenReturn(transactions);

        RewardResponseDTO response = rewardService.getRewards(customerId, null, null);

        assertNotNull(response);
        assertEquals(customerId, response.getId());
        assertTrue(response.getTotalRewards() > 0);

        verify(repository, times(1)).findByCustomerId(customerId);
    }

    @Test
    void shouldThrowExceptionWhenNoTransactionsFound() {

        Long customerId = 99L;

        when(repository.findByCustomerId(customerId)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () ->
                rewardService.getRewards(customerId, null, null));
    }

    @Test
    void shouldFilterByDateRange() {

        Long customerId = 1L;
        LocalDate start = LocalDate.now().minusMonths(2);
        LocalDate end = LocalDate.now();

        when(repository.findByCustomerIdAndDateBetween(customerId, start, end))
                .thenReturn(List.of(
                        new Transaction(1L, 150.0, LocalDate.now().minusMonths(1), null)
                ));

        RewardResponseDTO response = rewardService.getRewards(customerId, start, end);

        assertEquals(customerId, response.getId());
        assertTrue(response.getTotalRewards() > 0);

        verify(repository, times(1))
                .findByCustomerIdAndDateBetween(customerId, start, end);
    }
}
