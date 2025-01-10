package raisetech.StudentManagement.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * グローバルエラーをハンドリングするクラスです
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * ResponseStatusExceptionを処理します。
   *
   * @param ex 処理対象のResponseStatusException
   * @return エラー情報を含むHTTPレスポンス
   */
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

  /**
   * バリデーションエラーを処理します。
   *
   * @param ex 処理対象のMethodArgumentNotValidException
   * @return　エラー情報を含むHTTPレスポンス
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationException(
      MethodArgumentNotValidException ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.BAD_REQUEST.value());

    // バリデーションエラーの詳細を取得
    Map<String, String> errors = new HashMap<>();
    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      errors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
    response.put("errors", errors);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
