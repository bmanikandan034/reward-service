package com.rewards.service.repository;

import com.rewards.service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/***
 * @author Manikandan B
 */

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByCustomerId(Long customerId);

	List<Transaction> findByCustomerIdAndDateBetween(Long customerId, LocalDate start, LocalDate end);
}