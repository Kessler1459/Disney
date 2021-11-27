package Disney.exception;

import Disney.utils.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex){
        ApiError apiError=new ApiError(HttpStatus.NOT_FOUND, List.of(ex.getMessage()));
        return ResponseEntity.status(apiError.getHttpStatus()).body(apiError);
    }
}
