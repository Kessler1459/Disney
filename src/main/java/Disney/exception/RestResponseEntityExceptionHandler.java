package Disney.exception;

import Disney.model.DTO.MessageDTO;
import Disney.utils.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex){
        ApiError apiError=new ApiError(HttpStatus.NOT_FOUND, List.of(ex.getMessage()));
        return ResponseEntity.status(apiError.getHttpStatus()).body(apiError);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolations(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation e : ex.getConstraintViolations()) {
            errors.add(e.getRootBeanClass().getName() + " " + e.getPropertyPath() + ": " + e.getMessage());
        }
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errors);
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    @ExceptionHandler(value = PageException.class)
    public ResponseEntity<Object> handlePaginationException(PageException ex) {
        return new ResponseEntity<>(new MessageDTO(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<MessageDTO> handleBadCredentials(BadCredentialsException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageDTO(ex.getMessage()));
    }
}
