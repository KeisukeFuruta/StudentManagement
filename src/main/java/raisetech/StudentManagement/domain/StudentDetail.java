package raisetech.StudentManagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.data.Student;

/**
 * 受講生と受講生コース情報をもつ受講生詳細を扱うオブジェクトです。
 */
@Schema(description = "受講生詳細")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StudentDetail {

  @Valid
  private Student student;

  @Valid
  private List<StudentCourseDetail> studentCourseDetailList;
}
