package com.rewards.service.service;

import java.time.LocalDate;

import com.rewards.service.dto.RewardResponseDTO;

/***
 * @author Manikandan B
 */

public interface RewardService {

	RewardResponseDTO getRewards(Long customerId, LocalDate start, LocalDate end);

}
