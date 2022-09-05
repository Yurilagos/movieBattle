package br.com.ada.erro;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@RequiredArgsConstructor
@Data
public class ApiError {
	private final String code;
	private final String message;
}