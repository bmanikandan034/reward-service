package com.rewards.service.common;

import lombok.*;
import java.time.LocalDateTime;

/***
 * @author Manikandan B
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RewardResponse {

	private boolean success;

	private String message;

	private Object data;

	private LocalDateTime timestamp;
}