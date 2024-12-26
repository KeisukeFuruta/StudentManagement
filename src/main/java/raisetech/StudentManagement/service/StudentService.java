package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.search();
  }

  public List<StudentCourse> searchStudentsCourseList() {
    return repository.searchStudentsCourses();
  }

  public void registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
  }

  public void registerStudentCourse(StudentDetail studentDetail) {

    for (StudentCourse studentCourse : studentDetail.getStudentCourses()) {
      studentCourse.setStudentId(studentDetail.getStudent().getStudentId());
      studentCourse.setStartDate(LocalDateTime.now());
      studentCourse.setExpectedEndDate(LocalDateTime.now().plusYears(1));
    }
    repository.registerStudentCourses(studentDetail.getStudentCourses());
  }

  public boolean hasDuplicateCourses(StudentDetail studentDetail) {
    List<String> courseNames = studentDetail.getStudentCourses().stream()
        .map(StudentCourse::getCourseName)
        .toList();

    // 重複チェック
    Set<String> uniqueCourses = new HashSet<>(courseNames);
    return uniqueCourses.size() != courseNames.size();
  }
}
