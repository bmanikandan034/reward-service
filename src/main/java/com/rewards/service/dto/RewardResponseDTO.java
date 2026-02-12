package com.rewards.service.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

/***
 * @author Manikandan B
 */

@Data
@AllArgsConstructor
public class RewardResponseDTO {

	private Long customerId;
	private String customerName;
	private Map<String, Long> monthlyRewards;
	private Long totalRewards;

}
