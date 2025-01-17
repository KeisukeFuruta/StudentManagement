package raisetech.StudentManagement.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * エラー情報を扱うオブジェクトです
 */
@Schema(description = "エラー情報")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

  private String timestamp;
  private int status;
  private String error;
  private String path;
  private Map<String, String> errors;

}
