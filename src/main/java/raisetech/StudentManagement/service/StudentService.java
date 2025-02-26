package raisetech.StudentManagement.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.CourseStatus;
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
  private StudentConverter studentConverter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter studentConverter) {
    this.repository = repository;
    this.studentConverter = studentConverter;
  }

  /**
   * 受講生詳細の一覧検索です。
   * 全件検索を行うので、条件指定は行わないません。
   *
   * @return　受講生詳細一覧（全件）
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.search();
    List<StudentDetail> studentDetails = new ArrayList<>();

    for (Student student : studentList) {
      List<StudentCourse> studentCourseList = repository.searchStudentCourse(
          student.getStudentId());
      List<CourseStatus> courseStatusList = repository.searchStatusList();

      List<StudentDetail> studentDetailList = studentConverter.convertStudentDetails(
          List.of(student), studentCourseList, courseStatusList);

      studentDetails.addAll(studentDetailList);
    }
    return studentDetails;
  }


  /**
   * 受講生詳細検索です。
   * IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細
   */
  public StudentDetail searchStudentDetail(String studentId) {
    Student student = repository.searchStudent(studentId);
    if (student == null) {
      throw new IllegalArgumentException(
          "リクエストされたIDが存在しません。指定したid: " + studentId);
    }
    List<StudentCourse> studentCourseList = repository.searchStudentCourse(student.getStudentId());
    List<CourseStatus> courseStatusList = repository.searchStatus(student.getStudentId());
    return studentConverter.buildStudentDetail(student, studentCourseList,
        courseStatusList);
  }

  /**
   * 受講生詳細の登録を行います。
   * 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値とコース開始日、コース修了予定日を設定します。
   *
   * @param studentDetail 受講生詳細
   * @return 登録情報を付与した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();

    // 受講生を設定
    repository.registerStudent(student);

    studentDetail.getStudentCourseDetailList().forEach(studentCourseDetail -> {
      StudentCourse studentCourse = studentCourseDetail.getStudentCourse();
      CourseStatus courseStatus = studentCourseDetail.getCourseStatus();

      initStudentsCourse(studentCourse, student);
      repository.registerStudentCourse(studentCourse);

      String courseId = studentCourse.getCourseId();
      courseStatus.setCourseId(courseId);
      repository.registerCourseStatus(courseStatus);
    });
    return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param studentCourse 受講生コース情報
   * @param student       受講生
   */
  private void initStudentsCourse(StudentCourse studentCourse, Student student) {
    LocalDateTime now = LocalDateTime.now();

    studentCourse.setStudentId(student.getStudentId());
    studentCourse.setStartDate(now);
    studentCourse.setExpectedEndDate(now.plusYears(1));
  }

  /**
   * 受講生詳細の更新を行います。
   * 受講生と受講生コース情報をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());

    studentDetail.getStudentCourseDetailList().forEach(studentCourseDetail -> {
      StudentCourse studentCourse = studentCourseDetail.getStudentCourse();
      CourseStatus courseStatus = studentCourseDetail.getCourseStatus();

      studentCourse.setStudentId(studentDetail.getStudent().getStudentId());
      repository.updateStudentCourse(studentCourse);

      courseStatus.setCourseId(studentCourse.getCourseId());
      repository.updateCourseStatus(courseStatus);
    });
  }

  /**
   * コース名の重複登録を防ぐバリデーション処理
   *
   * @param studentDetail 受講生詳細
   */
  public void validateDuplicateStudentCourse(StudentDetail studentDetail) {
    List<String> courseNames = studentDetail.getStudentCourseDetailList()
        .stream()
        .map(studentCourseDetail -> studentCourseDetail.getStudentCourse().getCourseName())
        .toList();

    // コース名重複チェック
    if (courseNames.size() != new HashSet<>(courseNames).size()) {
      throw new IllegalArgumentException("重複したコース名は登録できません");
    }
  }
}
