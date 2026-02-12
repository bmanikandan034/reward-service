package com.rewards.service.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rewards.service.common.RewardResponse;
import com.rewards.service.common.RewardResponseUtil;
import com.rewards.service.dto.RewardResponseDTO;
import com.rewards.service.service.RewardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/***
 * @author Manikandan B
 */

@Slf4j
@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {

	private final RewardService rewardService;

	@GetMapping("/{customerId}")
	public ResponseEntity<RewardResponse> getRewards(@PathVariable Long customerId,
			@RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate) {

		log.info("REST request to fetching rewards for customerId={}, startDate={}, endDate={}", customerId, startDate,
				endDate);

		RewardResponseDTO response = rewardService.getRewards(customerId, startDate, endDate);

		return RewardResponseUtil.success(response, "Rewards fetched successfully");
	}

}
