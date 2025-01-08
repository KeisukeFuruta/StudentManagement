package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。
 * 受講生の検索や登録・更新処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生一覧検索です。
   * 全件検索を行うので、条件指定は行わないません。
   *
   * @return　受講生一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentCourse> studentCoursesList = repository.searchStudentsCoursesList();
    return converter.convertStudentDetails(studentList, studentCoursesList);
  }

  /**
   * 受講生検索です。
   * IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param studentId 受講生ID
   * @return 受講生
   */
  public StudentDetail searchStudentDetail(String studentId) {
    Student student = repository.searchStudent(studentId);
    List<StudentCourse> studentCourse = repository.searchStudentCourses(student.getStudentId());
    return new StudentDetail(student, studentCourse);
  }

  /**
   * 受講生新規登録です。
   *
   * @param studentDetail 受講生詳細
   * @return 登録された受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
    for (StudentCourse studentsCourse : studentDetail.getStudentCourses()) {
      studentsCourse.setStudentId(studentDetail.getStudent().getStudentId());
      studentsCourse.setStartDate(LocalDateTime.now());
      studentsCourse.setExpectedEndDate(LocalDateTime.now().plusYears(1));
      repository.registerStudentCourses(studentsCourse);
    }
    return studentDetail;
  }

  /**
   * 受講生更新です。
   * 受講生情報と受講生コース情報を更新します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {

    repository.updateStudent(studentDetail.getStudent());
    for (StudentCourse studentCourse : studentDetail.getStudentCourses()) {
      studentCourse.setStudentId(studentDetail.getStudent().getStudentId());
      repository.updateStudentCourses(studentCourse);
    }
  }

  /**
   * コース名の重複登録を防ぐバリデーション処理
   *
   * @param studentDetail 受講生詳細
   * @param result        コメントを返します
   */
  public void extracted(StudentDetail studentDetail, BindingResult result) {
    // コース名のリストを取得
    List<String> courseNames = studentDetail.getStudentCourses()
        .stream()
        .map(StudentCourse::getCourseName)
        .toList();

    // コース名重複チェック
    if (courseNames.size() != new HashSet<>(courseNames).size()) {
      result.rejectValue("studentCourses", "error.studentCourses",
          "重複したコース名は登録できません");
    }
  }
}
