package raisetech.StudentManagement.domain;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.StudentCourse;

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
