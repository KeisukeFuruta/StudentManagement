package raisetech.StudentManagement.exception;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "グローバルエラーをハンドリングするクラスです。")
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * ResponseStatusExceptionを処理します。
   *
   * @param ex 処理対象のResponseStatusException
   * @return エラー情報を含むHTTPレスポンス
   */
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ErrorResponse> handleResponseStatusException(
      ResponseStatusException ex) {

    // ErrorResponseを生成
    ErrorResponse response = new ErrorResponse(
        LocalDateTime.now().toString(),
        ex.getStatusCode().value(),
        ex.getReason(),
        ex.getMessage(),
        null
    );

    return new ResponseEntity<>(response, ex.getStatusCode());
  }

  /**
   * バリデーションエラーを処理します。
   *
   * @param ex 処理対象のConstraintViolationException
   * @return　エラー情報を含むHTTPレスポンス
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      ConstraintViolationException ex) {

    // バリデーションエラーの詳細を取得
    Map<String, String> errors = new HashMap<>();
    for (ConstraintViolation<?> violationException : ex.getConstraintViolations()) {
      errors.put(violationException.getPropertyPath().toString(), violationException.getMessage());
    }

    // ErrorResponseを生成
    ErrorResponse response = new ErrorResponse(
        LocalDateTime.now().toString(),
        HttpStatus.BAD_REQUEST.value(),
        "入力値が不正です。",
        "リクエストボディのバリデーションエラー",
        errors
    );

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * リクエストボディのバリデーションエラーを処理します。
   *
   * @param ex 処理対象の MethodArgumentNotValidException
   * @return バリデーションエラーの詳細を含む HTTP レスポンス
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {

    // バリデーションエラーの詳細を取得
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
        errors.put(error.getField(), error.getDefaultMessage())
    );

    String errorMessage = "バリデーションエラー";

    // ErrorResponseを生成
    ErrorResponse response = new ErrorResponse(
        LocalDateTime.now().toString(),
        HttpStatus.BAD_REQUEST.value(),
        errorMessage,
        "入力値が不正です。",
        errors
    );

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * 単一検索に存在しないidを入力された際のエラー、受講生登録時に重複したコース名を入力された際のエラーを処理します。
   *
   * @param ex 処理対象のIllegalArgumentException
   * @return エラーメッセージとHTTPレスポンス
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
      IllegalArgumentException ex) {

    // ErrorResponseを生成
    ErrorResponse response = new ErrorResponse(
        LocalDateTime.now().toString(),
        HttpStatus.BAD_REQUEST.value(),
        ex.getMessage(),
        "不正なリクエストが送信されました。",
        null
    );

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }


}
