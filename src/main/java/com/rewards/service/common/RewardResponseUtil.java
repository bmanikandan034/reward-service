package com.rewards.service.common;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;

/***
 * @author Manikandan B
 */
public final class RewardResponseUtil {

	private RewardResponseUtil() {
	}

	public static ResponseEntity<RewardResponse> success(Object data, String message) {

		RewardResponse response = RewardResponse.builder().success(true).message(message).data(data)
				.timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(response);
	}

	public static ResponseEntity<RewardResponse> failure(String message) {

		RewardResponse response = RewardResponse.builder().success(false).message(message)
				.timestamp(LocalDateTime.now()).build();

		return ResponseEntity.badRequest().body(response);
	}

}
