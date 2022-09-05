package br.com.ada.erro;

import org.springframework.http.HttpStatus;

public class ApiErrorException extends ApiBusinessException {

	private static final long serialVersionUID = -6454043133373661998L;

	public ApiErrorException(String code, HttpStatus status) {
		super(code, status);
	}

}
