package com.rewards.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rewards.service.entity.Customer;

/***
 * @author Manikandan B
 */

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
