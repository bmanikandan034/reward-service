package com.rewards.service.service.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.rewards.service.dto.RewardResponseDTO;
import com.rewards.service.entity.Customer;
import com.rewards.service.entity.Transaction;
import com.rewards.service.error.CustomerNotFoundException;
import com.rewards.service.repository.CustomerRepository;
import com.rewards.service.repository.TransactionRepository;
import com.rewards.service.service.RewardService;
import com.rewards.service.util.RewardCalculator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***
 * @author Manikandan B
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class RewardServiceImpl implements RewardService {

	private final TransactionRepository transactionRepository;
	private final CustomerRepository customerRepository;

	@Override
	@Cacheable(value = "rewards", key = "#customerId + '-' + #start + '-' + #end")
	public RewardResponseDTO getRewards(Long customerId, LocalDate start, LocalDate end) {

		if (customerId == null || customerId <= 0) {
			throw new IllegalArgumentException("Customer ID must be a positive number");
		}

		if (start != null && end != null && start.isAfter(end)) {
			throw new IllegalArgumentException("Start date cannot be after end date");
		}

		if (!customerRepository.existsById(customerId)) {
			throw new CustomerNotFoundException("Customer not found with id: " + customerId);
		}

		List<Transaction> transactions;
		if (start != null && end != null) {
			transactions = transactionRepository.findByCustomerIdAndDateBetween(customerId, start, end);
		} else {
			transactions = transactionRepository.findByCustomerId(customerId);
		}

		Map<String, Long> monthlyRewards = transactions.stream()
				.collect(Collectors.groupingBy(t -> YearMonth.from(t.getDate()).toString(),
						Collectors.summingLong(t -> RewardCalculator.calculate(t.getAmount()))));

		Long totalPoints = monthlyRewards.values().stream().mapToLong(Long::longValue).sum();

		String customerName = customerRepository.findById(customerId).map(Customer::getName).orElse("Unknown");

		return new RewardResponseDTO(customerId, customerName, monthlyRewards, totalPoints);
	}

}
