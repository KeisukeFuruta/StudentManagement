package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
@Validated
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  // 受講生情報を一覧で取得します
  public List<Student> searchStudentList() {
    return repository.searchStudentList();
  }

  // 受講コース情報を一覧で取得します。
  public List<StudentCourse> searchStudentsCourseList() {
    return repository.searchStudentsCoursesList();
  }

  // 受講生登録を行います
  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
  }

  // 受講コース情報の登録を行います
  @Transactional
  public void registerStudentCourse(StudentDetail studentDetail) {

    for (StudentCourse studentCourse : studentDetail.getStudentCourses()) {
      studentCourse.setStudentId(studentDetail.getStudent().getStudentId());
      studentCourse.setStartDate(LocalDateTime.now());
      studentCourse.setExpectedEndDate(LocalDateTime.now().plusYears(1));
    }
    repository.registerStudentCourses(studentDetail.getStudentCourses());
  }

  // コース情報の重複チェックを行うメソッドです。
  public boolean hasDuplicateCourses(StudentDetail studentDetail) {
    List<String> courseNames = studentDetail.getStudentCourses().stream()
        .map(StudentCourse::getCourseName)
        .toList();

    // 重複チェック
    Set<String> uniqueCourses = new HashSet<>(courseNames);
    return uniqueCourses.size() != courseNames.size();
  }

  // 受講生情報とコース情報の単一検索
  public StudentDetail searchStudentDetail(String studentId) {
    Student student = repository.searchStudent(studentId);
    List<StudentCourse> studentCourse = repository.searchStudentCourses(student.getStudentId());
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourses(studentCourse);
    return studentDetail;
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {

    repository.updateStudent(studentDetail.getStudent());
    for (StudentCourse studentCourse : studentDetail.getStudentCourses()) {
      studentCourse.setStudentId(studentDetail.getStudent().getStudentId());
      repository.updateStudentCourses(studentCourse);
    }
  }

}
