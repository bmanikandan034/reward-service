package com.rewards.service.util;

/***
 * @author Manikandan B
 */

public final class RewardCalculator {

	private RewardCalculator() {
	}

	public static int calculate(double amount) {

		if (amount <= 50)
			return 0;

		if (amount <= 100)
			return (int) (amount - 50);

		return 50 + (int) ((amount - 100) * 2);
	}

}
