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

	private Long id;
	private Map<String, Integer> monthlyRewards;
	private Integer totalRewards;

}
