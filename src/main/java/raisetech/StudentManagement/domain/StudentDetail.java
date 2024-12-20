package raisetech.StudentManagement.domain;

import jakarta.validation.Valid;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@Getter
@Setter
public class StudentDetail {

  @Valid
  private Student student;
  
  private List<StudentCourse> studentCourses;
}
