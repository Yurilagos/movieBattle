package br.com.ada.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {

	public static Double convertImdbVoteToDouble(String imdbVote) {
		return Double.valueOf(imdbVote.replace(",", ""));
	}

	public static Double trunc(Double valor) {
		BigDecimal bd = new BigDecimal(valor).setScale(3, RoundingMode.HALF_EVEN);
		System.out.println(bd.doubleValue());
		return bd.doubleValue();
	}
}
