package br.com.ada.erro;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ApiBusinessException extends RuntimeException {

	private static final long serialVersionUID = -7988535594119863662L;
	
	private final String code;
	private final HttpStatus stauts;

}
