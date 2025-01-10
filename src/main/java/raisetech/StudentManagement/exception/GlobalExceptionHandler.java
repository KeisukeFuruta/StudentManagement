package raisetech.StudentManagement.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Map<String, Object>> handleResponseStatusException(
      ResponseStatusException ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", ex.getStatusCode().value());
    response.put("error", ex.getReason());
    response.put("path", ex.getMessage());
    return new ResponseEntity<>(response, ex.getStatusCode());
  }

}
