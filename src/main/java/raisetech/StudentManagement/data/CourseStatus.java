package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * 受講コースの申込状況を扱うオブジェクトです。
 */
@Schema(description = "申込状況")
@Getter
@Setter
@Validated
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CourseStatus {

  private String statusId;

  private String courseId;

  @Pattern(regexp = "仮申込|本申込|受講中|受講終了", message = "申込状況は「仮申込」「本申込」「受講中」「受講終了」のいずれかでなければなりません。")
  private String status;

}
