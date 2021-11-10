package com.gvendas.gestaovendas.Exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GestaoVendasExceptionHandler extends ResponseEntityExceptionHandler {

	private static final String CONSTANT_VALIDATION_NOTBLANK = "NotBlank";
	private static final String CONSTANT_VALIDATION_NOTNULL = "NotNull";
	private static final String CONSTANT_VALIDATION_LENGHT = "Length";
	private static final String CONSTANT_VALIDATION_PATTERN = "Pattern";
	private static final String CONSTANT_VALIDATION_MIN = "Min";

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			org.springframework.http.HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Error> errors = generatedListErros(ex.getBindingResult());

		return handleExceptionInternal(ex, errors, headers, status, request);
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,
			WebRequest request) {
		String message = "Recurso não encontrado.";
		String error = ex.toString();
		List<Error> errors = Arrays.asList(new Error(message, error));
		return handleExceptionInternal(ex, errors, new org.springframework.http.HttpHeaders(), HttpStatus.NOT_FOUND,
				request);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
			WebRequest rest) {
		String message = "Recurso não encontrado.";
		String error = ex.toString();
		List<Error> errors = Arrays.asList(new Error(message, error));
		return handleExceptionInternal(ex, errors, new org.springframework.http.HttpHeaders(), HttpStatus.BAD_REQUEST,
				rest);
	}

	@ExceptionHandler(GenericException.class)
	public ResponseEntity<Object> handleGenericException(GenericException ex, WebRequest request) {
		String message = ex.getMessage();
		String error = ex.getMessage();
		List<Error> errors = Arrays.asList(new Error(message, error));
		return handleExceptionInternal(ex, errors, new org.springframework.http.HttpHeaders(), HttpStatus.BAD_REQUEST,
				request);
	}

	private List<Error> generatedListErros(BindingResult bindingResult) {
		List<Error> errors = new ArrayList<Error>();

		bindingResult.getFieldErrors().forEach(field -> {
			String message = createMessageErrorUser(field);
			String error = field.toString();
			errors.add(new Error(message, error));
		});

		return errors;
	}

	private String createMessageErrorUser(FieldError field) {
		if (field.getCode().equals(CONSTANT_VALIDATION_NOTBLANK)) {
			return field.getDefaultMessage().concat(" é obrigatório.");
		}

		if (field.getCode().equals(CONSTANT_VALIDATION_LENGHT)) {
			return field.getDefaultMessage().concat(String.format(" deve ter entre %s e %s caracteres.",
					field.getArguments()[2], field.getArguments()[1]));
		}

		if (field.getCode().equals(CONSTANT_VALIDATION_NOTNULL)) {
			return field.getDefaultMessage().concat(" é obrigatório.");
		}

		if (field.getCode().equals(CONSTANT_VALIDATION_PATTERN)) {
			return field.getDefaultMessage().concat(" formato inválido.");
		}
		
		if (field.getCode().equals(CONSTANT_VALIDATION_MIN)) {
			return field.getDefaultMessage().concat(String.format(" deve ser maior ou igual a %s", field.getArguments()[1]));
		}

		return field.toString();
	}

}
