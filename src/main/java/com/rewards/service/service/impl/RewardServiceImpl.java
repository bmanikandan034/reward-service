package com.rewards.service.service.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.rewards.service.dto.RewardResponseDTO;
import com.rewards.service.entity.Transaction;
import com.rewards.service.error.ResourceNotFoundException;
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

	private final TransactionRepository repository;

	@Override
	@Cacheable(value = "rewards", key = "#customerId + '-' + #start + '-' + #end")
	public RewardResponseDTO getRewards(Long customerId, LocalDate start, LocalDate end) {

		List<Transaction> transactions;

		if (start != null && end != null)
			transactions = repository.findByCustomerIdAndDateBetween(customerId, start, end);
		else
			transactions = repository.findByCustomerId(customerId);

		if (transactions.isEmpty())
			throw new ResourceNotFoundException("No transactions found");

		Map<String, Integer> monthly = transactions.stream()
				.collect(Collectors.groupingBy(t -> YearMonth.from(t.getDate()).toString(),
						Collectors.summingInt(t -> RewardCalculator.calculate(t.getAmount()))));

		int total = monthly.values().stream().mapToInt(i -> i).sum();

		return new RewardResponseDTO(customerId, monthly, total);
	}

}
