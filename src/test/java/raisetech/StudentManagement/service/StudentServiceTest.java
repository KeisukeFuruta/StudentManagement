package raisetech.StudentManagement.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
import org.springframework.validation.BindingResult;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.search()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentCourseList);

    sut.searchStudentList();

    verify(repository, times(1)).search();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList);
  }

  @Test
  void 受講生単一検索_リポジトリが適切に呼び出せていること() {
    String studentId = "30";
    Student student = new Student();
    student.setStudentId(studentId);
    List<StudentCourse> studentCourse = new ArrayList<>();

    when(repository.searchStudent(studentId)).thenReturn(student);
    when(repository.searchStudentCourse(studentId)).thenReturn(studentCourse);

    sut.searchStudentDetail(studentId);

    verify(repository, times(1)).searchStudent(studentId);
    verify(repository, times(1)).searchStudentCourse(studentId);
  }

  @Test
  void 受講生詳細の登録_リポジトリが適切に呼び出せてコース情報を登録できていること() {
    StudentDetail studentDetail = mock(StudentDetail.class);
    Student student = mock(Student.class);
    List<StudentCourse> studentCourseList = List.of(mock(StudentCourse.class),
        mock(StudentCourse.class));

    when(studentDetail.getStudent()).thenReturn(student);
    when(studentDetail.getStudentCourseList()).thenReturn(studentCourseList);

    sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(any(Student.class));
    verify(repository, times(2)).registerStudentCourse(any(StudentCourse.class));

  }

  @Test
  void 受講生詳細の更新_リポジトリが適切に呼び出せてコース情報を登録できていること() {
    StudentDetail studentDetail = mock(StudentDetail.class);
    Student student = mock(Student.class);
    List<StudentCourse> studentCourseList = List.of(mock(StudentCourse.class),
        mock(StudentCourse.class));

    when(studentDetail.getStudent()).thenReturn(student);
    when(studentDetail.getStudentCourseList()).thenReturn(studentCourseList);

    sut.updateStudent(studentDetail);

    verify(repository, times(1)).updateStudent(any(Student.class));
    verify(repository, times(2)).updateStudentCourse(any(StudentCourse.class));
  }

  @Test
  void コース名の重複登録を防ぐバリデーション処理_重複がない場合() {
    StudentCourse course1 = mock(StudentCourse.class);
    StudentCourse course2 = mock(StudentCourse.class);
    when(course1.getCourseName()).thenReturn("Javaコース");
    when(course2.getCourseName()).thenReturn("WPコース");

    StudentDetail studentDetail = mock(StudentDetail.class);
    when(studentDetail.getStudentCourseList()).thenReturn(List.of(course1, course2));

    BindingResult result = mock(BindingResult.class);

    sut.validateStudentDetail(studentDetail, result);

    verify(result, never()).rejectValue(anyString(), anyString(), anyString());

  }

  @Test
  void コース名の重複登録を防ぐバリデーション処理_重複がある場合() {
    StudentCourse course1 = mock(StudentCourse.class);
    StudentCourse course2 = mock(StudentCourse.class);
    when(course1.getCourseName()).thenReturn("Javaコース");
    when(course2.getCourseName()).thenReturn("Javaコース");

    StudentDetail studentDetail = mock(StudentDetail.class);
    when(studentDetail.getStudentCourseList()).thenReturn(List.of(course1, course2));

    BindingResult result = mock(BindingResult.class);

    sut.validateStudentDetail(studentDetail, result);

    verify(result, times(1)).rejectValue(
        "studentCourseList",
        "error.studentCourseList",
        "重複したコース名は登録できません");

  }

}
