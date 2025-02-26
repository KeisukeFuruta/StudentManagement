package raisetech.StudentManagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * 受講生コース情報と申込状況を持つ、受講生コース情報詳細を扱うオブジェクトです。
 */
@Schema(description = "受講生コース情報詳細")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StudentCourseDetail {

  @Valid
  private StudentCourse studentCourse;

  @Valid
  private CourseStatus courseStatus;

}
