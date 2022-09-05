package br.com.ada.erro;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import lombok.RequiredArgsConstructor;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

	private final MessageSource apiErrorMessageSource;

	public ApiError toApiError(String code, Locale locale, Object... args) {
		String message;
		try {
			message = apiErrorMessageSource.getMessage(code, args, locale);

		} catch (NoSuchMessageException e) {
			message = "Message not found";
			LOGGER.error("No messages were found for {} code {} locale", code, locale);
		}

		return new ApiError(code, message);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleNotValidException(MethodArgumentNotValidException ex, Locale locale) {

		Stream<ObjectError> errors = ex.getBindingResult().getAllErrors().stream();

		List<ApiError> apiErrors = errors.map(ObjectError::getDefaultMessage).map(code -> toApiError(code, locale))
				.collect(toList());

		ApiErrorResponse apiErrorResponse = ApiErrorResponse.of(HttpStatus.BAD_REQUEST, apiErrors);

		return ResponseEntity.badRequest().body(apiErrorResponse);
	}

	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<ApiErrorResponse> handleInvalidFormatException(InvalidFormatException exception,
			Locale locale) {
		final String errorCode = "M2001";
		final HttpStatus status = HttpStatus.BAD_REQUEST;
		final ApiErrorResponse errorResponse = ApiErrorResponse.of(status,
				toApiError(errorCode, locale, exception.getValue()));

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(ApiBusinessException.class)
	public ResponseEntity<ApiErrorResponse> handleCustomerException(ApiBusinessException exception, Locale locale) {
		final String code = exception.getCode();
		final HttpStatus status = exception.getStauts();

		final ApiErrorResponse errorResponse = ApiErrorResponse.of(status, toApiError(code, locale, exception));

		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiErrorResponse> handleInternalServerAccessDeniedException(AccessDeniedException exception,
			Locale locale) {
		LOGGER.error("AccessDeniedException", exception);

		final String code = "M2002";
		final HttpStatus status = HttpStatus.UNAUTHORIZED;

		final ApiErrorResponse errorResponse = ApiErrorResponse.of(status, toApiError(code, locale));

		return ResponseEntity.status(status).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleInternalServerError(Exception exception, Locale locale) {
		LOGGER.error("Unknow Error", exception);

		final String code = "M2001";
		final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

		final ApiErrorResponse errorResponse = ApiErrorResponse.of(status, toApiError(code, locale));

		return ResponseEntity.status(status).body(errorResponse);
	}

}
