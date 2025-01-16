package raisetech.StudentManagement.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
   * @param ex 処理対象のConstraintViolationException
   * @return　エラー情報を含むHTTPレスポンス
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, Object>> handleValidationException(
      ConstraintViolationException ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", "入力値が不正です");
    response.put("path", "バリデーションエラー");

    // バリデーションエラーの詳細を取得
    Map<String, String> errors = new HashMap<>();
    for (ConstraintViolation<?> violationException : ex.getConstraintViolations()) {
      errors.put(violationException.getPropertyPath().toString(), violationException.getMessage());
    }
    response.put("errors", errors);

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * リクエストボディのバリデーションエラーを処理します。
   *
   * @param ex 処理対象の MethodArgumentNotValidException
   * @return バリデーションエラーの詳細を含む HTTP レスポンス
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
        errors.put(error.getField(), error.getDefaultMessage())
    );

    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", "リクエストボディのバリデーションエラー");
    response.put("errors", errors);

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 単一検索に存在しないidを入力された際のエラーを処理します。
   *
   * @param ex 処理対象のIllegalArgumentException
   * @return エラーメッセージとHTTPレスポンス
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", ex.getMessage());
    response.put("path", "リクエストされたIDが存在しません");
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
}
