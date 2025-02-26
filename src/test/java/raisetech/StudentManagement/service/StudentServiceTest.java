package raisetech.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentCourseDetail;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter studentConverter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, studentConverter);
  }

  @Test
  void 正常系_受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    Student student = new Student("1", "山田太郎", "ヤマダタロウ", "タロちゃん",
        "taro.yamada@example.com", "東京都", 25, "男性", "なし", false);
    studentList.add(student);
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<CourseStatus> courseStatusList = new ArrayList<>();

    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourse(anyString())).thenReturn(studentCourseList);
    when(repository.searchStatusList()).thenReturn(courseStatusList);

    sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(studentList.size())).searchStudentCourse(anyString());
    verify(repository, times(studentList.size())).searchStatusList();
    verify(studentConverter, times(1)).convertStudentDetails(studentList, studentCourseList,
        courseStatusList);
  }

  @Test
  void 正常系_受講生詳細検索_リポジトリが適切に呼び出せていること() {
    String studentId = "testStudentId";
    Student student = new Student();
    student.setStudentId(studentId);
    List<StudentCourse> studentCourse = new ArrayList<>();
    List<CourseStatus> courseStatus = new ArrayList<>();

    when(repository.searchStudent(studentId)).thenReturn(student);
    when(repository.searchStudentCourse(studentId)).thenReturn(studentCourse);
    when(repository.searchStatus(studentId)).thenReturn(courseStatus);

    sut.searchStudentDetail(studentId);

    verify(repository, times(1)).searchStudent(studentId);
    verify(repository, times(1)).searchStudentCourse(studentId);
    verify(repository, times(1)).searchStatus(studentId);
  }

  @Test
  void 異常系_受講生詳細検索_存在しないstudentIdが入力された場合_IllegalArgumentExceptionがスローされる() {
    String studentId = "invalidId";
    when(repository.searchStudent(studentId)).thenReturn(null);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      sut.searchStudentDetail(studentId);
    });
    assertEquals("リクエストされたIDが存在しません。指定したid: " + studentId,
        exception.getMessage());
  }

  @Test
  void 正常系_受講生詳細検索_存在するstudentIdが入力された場合_StudentDetailが返される() {
    String studentId = "validId";
    Student student = mock(Student.class);
    List<StudentCourse> studentCourse = List.of(mock(StudentCourse.class));
    List<CourseStatus> courseStatus = List.of(mock(CourseStatus.class));

    List<StudentCourseDetail> studentCourseDetail = List.of(
        new StudentCourseDetail(studentCourse.get(0), courseStatus.get(0)));

    when(repository.searchStudent(studentId)).thenReturn(student);
    when(repository.searchStudentCourse(student.getStudentId())).thenReturn(studentCourse);
    when(repository.searchStatus(student.getStudentId())).thenReturn(courseStatus);

    when(studentConverter.buildStudentDetail(student, studentCourse, courseStatus)).thenReturn(
        new StudentDetail(student, studentCourseDetail));

    StudentDetail result = sut.searchStudentDetail(studentId);

    assertNotNull(result);
    assertEquals(student, result.getStudent());
    assertEquals(studentCourseDetail, result.getStudentCourseDetailList());

    verify(studentConverter, times(1)).buildStudentDetail(student, studentCourse, courseStatus);
  }

  @Test
  void 正常系_受講生詳細の登録_リポジトリが適切に呼び出せて受講生コース情報詳細を登録できていること() {
    Student student = mock(Student.class);
    List<StudentCourse> studentCourseList = List.of(mock(StudentCourse.class),
        mock(StudentCourse.class));
    List<CourseStatus> courseStatusList = List.of(mock(CourseStatus.class),
        mock(CourseStatus.class));
    StudentDetail studentDetail = mock(StudentDetail.class);

    when(studentDetail.getStudent()).thenReturn(student);
    when(studentDetail.getStudentCourseDetailList()).thenReturn(
        createStudentCourseDetailList(studentCourseList, courseStatusList));

    sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(any(Student.class));
    verify(repository, times(2)).registerStudentCourse(any(StudentCourse.class));
    verify(repository, times(2)).registerCourseStatus(any(CourseStatus.class));
  }

  @Test
  void 受講生詳細の更新_リポジトリが適切に呼び出せて受講生コース情報詳細を登録できていること() {
    Student student = mock(Student.class);
    List<StudentCourse> studentCourseList = List.of(mock(StudentCourse.class),
        mock(StudentCourse.class));
    List<CourseStatus> courseStatusList = List.of(mock(CourseStatus.class),
        mock(CourseStatus.class));
    StudentDetail studentDetail = mock(StudentDetail.class);

    when(studentDetail.getStudent()).thenReturn(student);
    when(studentDetail.getStudentCourseDetailList()).thenReturn(
        createStudentCourseDetailList(studentCourseList, courseStatusList));

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(any(Student.class));
    verify(repository, times(2)).updateStudentCourse(any(StudentCourse.class));
    verify(repository, times(2)).updateCourseStatus(any(CourseStatus.class));
  }

  @Test
  void コース名の重複登録を防ぐバリデーション処理_重複がない場合() {
    StudentCourse course1 = mock(StudentCourse.class);
    StudentCourse course2 = mock(StudentCourse.class);
    when(course1.getCourseName()).thenReturn("Javaコース");
    when(course2.getCourseName()).thenReturn("WPコース");
    List<StudentCourse> studentCourseList = List.of(course1, course2);
    List<CourseStatus> courseStatusList = List.of(mock(CourseStatus.class),
        mock(CourseStatus.class));

    StudentDetail studentDetail = mock(StudentDetail.class);
    when(studentDetail.getStudentCourseDetailList()).thenReturn(
        createStudentCourseDetailList(studentCourseList, courseStatusList));

    assertDoesNotThrow(() -> {
      sut.validateDuplicateStudentCourse(studentDetail);
    });
  }

  @Test
  void コース名の重複登録を防ぐバリデーション処理_重複がある場合() {
    StudentCourse course1 = mock(StudentCourse.class);
    StudentCourse course2 = mock(StudentCourse.class);
    when(course1.getCourseName()).thenReturn("Javaコース");
    when(course2.getCourseName()).thenReturn("Javaコース");
    List<StudentCourse> studentCourseList = List.of(course1, course2);
    List<CourseStatus> courseStatusList = List.of(mock(CourseStatus.class),
        mock(CourseStatus.class));

    StudentDetail studentDetail = mock(StudentDetail.class);
    when(studentDetail.getStudentCourseDetailList()).thenReturn(
        createStudentCourseDetailList(studentCourseList, courseStatusList));

    assertThrows(IllegalArgumentException.class, () -> {
      sut.validateDuplicateStudentCourse(studentDetail);
    });
  }

  /**
   * 受講生コース情報と申込状況を対応させ、受講生コース情報詳細を作成するメソッドです。
   *
   * @param studentCourseList
   * @param courseStatusList
   * @return 受講生コース情報詳細
   */
  private List<StudentCourseDetail> createStudentCourseDetailList(
      List<StudentCourse> studentCourseList, List<CourseStatus> courseStatusList) {
    List<StudentCourseDetail> studentCourseDetailList = new ArrayList<>();
    for (int i = 0; i < studentCourseList.size(); i++) {
      StudentCourse studentCourse = studentCourseList.get(i);
      CourseStatus courseStatus = courseStatusList.get(i);

      // StudentCourseDetailを作成
      studentCourseDetailList.add(new StudentCourseDetail(studentCourse, courseStatus));
    }
    return studentCourseDetailList;
  }

}
