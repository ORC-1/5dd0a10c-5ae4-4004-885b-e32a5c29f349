package com.onlinebookstore.interswitch.shared.exception;

import com.fasterxml.jackson.core.JsonParseException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.validation.ValidationException;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.postgresql.util.PSQLException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String FOR = "for ";
    private static final String INVALID_REQUEST = "Request is not valid";

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError> methodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        log.error("validation exception : " + ex.getLocalizedMessage() + FOR + request.getRequestURI());
        return new ResponseEntity<>(
                ApiError.builder()
                        .errorMessage(errors)
                        .errorCode(HttpStatus.BAD_REQUEST.toString())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage(INVALID_REQUEST)
                        .build(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ApiError> validationException(
            ValidationException ex,
            HttpServletRequest request) {
        log.error(ex.getLocalizedMessage() + "validation exception : " + FOR + request.getRequestURI());
        List<String> errors = Arrays.asList(ex.getMessage());
        return new ResponseEntity<>(
                ApiError.builder()
                        .errorMessage(errors)
                        .errorCode(HttpStatus.BAD_REQUEST.toString())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage(INVALID_REQUEST)
                        .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiError> methodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        log.error(ex.getLocalizedMessage() + "MethodArgumentTypeMismatchException exception : " + FOR + request.getRequestURI());
        List<String> errors = Arrays.asList(ex.getMessage());
        return new ResponseEntity<>(
                ApiError.builder()
                        .errorMessage(errors)
                        .errorCode(HttpStatus.BAD_REQUEST.toString())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage(INVALID_REQUEST)
                        .build(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ApiError> dataIntegrityViolationException(DataIntegrityViolationException ex,
                                                                    HttpServletRequest request) {
        log.error(ex.getMessage() + "exception : " + FOR + request.getRequestURI());
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        List<String> errors;
        errors = Arrays.asList(ex.getMessage());
        PSQLException pgSqlException = (PSQLException) rootCause;
        //TODO: get this value from properties, to handle possible DB change
        final String constraintViolationExceptionKey = "23505";
        if (pgSqlException.getSQLState().equals(constraintViolationExceptionKey)) {
            errors = Arrays.asList(extractKey(ex.getMessage()) + " already exists");
        }
        return new ResponseEntity<>(
                ApiError.builder()
                        .errorMessage(errors)
                        .errorCode(HttpStatus.BAD_REQUEST.toString())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage("Data integrity violation")
                        .build(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({HttpMessageNotReadableException.class})
    protected ResponseEntity<ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                    HttpServletRequest request) {
        String errorMessage;
        if (ex.getCause() instanceof JsonParseException) {
            errorMessage = "Invalid JSON in the request body";
        } else {
            errorMessage = "Required request body is missing ";
        }
        return new ResponseEntity<>(
                ApiError.builder()
                        .errorMessage(Arrays.asList(errorMessage))
                        .errorCode(HttpStatus.BAD_REQUEST.toString())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage(ex.getMessage())
                        .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ApiError> handleRequestMethodNotSupportedException(HttpMessageNotReadableException ex,
                                                                                HttpServletRequest request) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .errorMessage(Arrays.asList("Request method not supported"))
                        .errorCode(HttpStatus.BAD_REQUEST.toString())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage(INVALID_REQUEST)
                        .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PropertyReferenceException.class})
    public ResponseEntity<ApiError> handlePropertyReferenceException(
            PropertyReferenceException ex,
            HttpServletRequest request) {
        log.error(ex.getLocalizedMessage() + " PropertyReferenceException exception : " + FOR + request.getRequestURI());
        //Get a more user friendly error message for PropertyReferenceException Exception.
        List<String> errors = getErrorSuggestion(ex);

        return new ResponseEntity<>(
                ApiError.builder()
                        .errorMessage(errors)
                        .errorCode(HttpStatus.BAD_REQUEST.toString())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage(INVALID_REQUEST)
                        .build(), HttpStatus.BAD_REQUEST);
    }

    private static List<String> getErrorSuggestion(PropertyReferenceException ex) {
        String errorMessage = ex.getMessage();
        String propertyName = ex.getPropertyName();
        String suggestedName = propertyName.startsWith("[\"") && propertyName.endsWith("\"]") ?
                propertyName.substring(2, propertyName.length() - 2) :
                propertyName.startsWith("[") && propertyName.endsWith("]") ? propertyName.substring(1, propertyName.length() - 1) : "";
        List<String> errors = !suggestedName.isEmpty() ? Arrays.asList("Invalid sort property: " + propertyName + ". Did you mean '" + suggestedName + "' with no []?") : Arrays.asList(errorMessage);
        return errors;
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ApiError> handleBadRequestException(BadRequestException ex,
                                                                 HttpServletRequest request) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .errorMessage(Arrays.asList(ex.getMessage()))
                        .errorCode(HttpStatus.BAD_REQUEST.toString())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage(INVALID_REQUEST)
                        .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiError> genericException(Exception ex,
                                                     HttpServletRequest request) {
        log.error("Stack: " + Arrays.asList(ex.getStackTrace()) + " Message: " + ex.getMessage() + " Localized message: "
                  + ex.getLocalizedMessage() + " Exception : " + FOR + request.getRequestURI());
        List<String> errors = Arrays.asList("Something went wrong.");
        return new ResponseEntity<>(
                ApiError.builder()
                        .errorMessage(errors)
                        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .request(request.getRequestURI())
                        .requestType(request.getMethod())
                        .customMessage("Could not process request")
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private static String extractConstraintName(String errorMessage) {
        Pattern pattern = Pattern.compile("constraint \\[(.*?)\\]");
        Matcher matcher = pattern.matcher(errorMessage);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private static String removeConstraintKey(String constraintName) {
        int index = constraintName.indexOf("_key");
        if (index != -1) {
            return constraintName.substring(0, index);
        } else {
            return constraintName;
        }
    }

    private static String extractKey(String errorMessage) {
        String constraintName = extractConstraintName(errorMessage);
        if (constraintName != null) {
            //Todo: remove plurization of key
            return removeConstraintKey(constraintName).replace("pk_", "");
        } else {
            return "";
        }
    }
}
