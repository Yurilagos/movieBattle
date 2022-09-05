package br.com.ada.erro;

import static lombok.AccessLevel.PRIVATE;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@RequiredArgsConstructor(access = PRIVATE)
@Data
public class ApiErrorResponse {

	private final int statusCode;

	private final List<ApiError> errors;

	static ApiErrorResponse of(HttpStatus status, List<ApiError> errors) {
		return new ApiErrorResponse(status.value(), errors);
	}

	static ApiErrorResponse of(HttpStatus status, ApiError error) {
		return of(status, Collections.singletonList(error));
	}

	
}
