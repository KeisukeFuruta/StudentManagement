package raisetech.StudentManagement.controller.converter;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter sut;
  private Student student1;
  private Student student2;

  @BeforeEach
  void setup() {
    sut = new StudentConverter();

    student1 = new Student("1", "髙比良くるま", "たかひらくるま", "くるま", "kuruma@test.com",
        "東京", 30, "男性", "なし", false);
    student2 = new Student("2", "松井ケムリ", "まついけむり", "けむり", "kemuri@test.com",
        "東京", 30, "男性", "なし", false);
  }

  @Test
  void 正常系_受講生に対する受講生コースが正しくマッピングされる() throws Exception {
    List<Student> studentList = Arrays.asList(student1, student2);
    StudentCourse studentCourse1 = new StudentCourse("1", "1", "Javaコース", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));
    StudentCourse studentCourse2 = new StudentCourse("2", "1", "WPコース", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));
    StudentCourse studentCourse3 = new StudentCourse("3", "2", "AWSコース", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));
    List<StudentCourse> studentCourseList = Arrays.asList(studentCourse1, studentCourse2,
        studentCourse3);

    // actual（実際の値）をstudentDetailsに格納
    List<StudentDetail> studentDetails = sut.convertStudentDetails(studentList,
        studentCourseList);

    // expected（期待値）を準備
    List<StudentDetail> expected = List.of(
        new StudentDetail(student1, List.of(studentCourse1, studentCourse2)),
        new StudentDetail(student2, List.of(studentCourse3))
    );

    assertEquals(expected, studentDetails);
  }

  @Test
  void 受講生と受講生コースのstudentIdが一致しない場合() throws Exception {
    List<Student> studentList = Arrays.asList(student1, student2);
    List<StudentCourse> studentCourseList = List.of(
        new StudentCourse("1", "3", "Javaコース", LocalDateTime.now(),
            LocalDateTime.now().plusYears(1)),
        new StudentCourse("2", "4", "AWSコース", LocalDateTime.now(),
            LocalDateTime.now().plusYears(1)));

    List<StudentDetail> studentDetails = sut.convertStudentDetails(studentList,
        studentCourseList);

    List<StudentDetail> expected = List.of(
        new StudentDetail(student1, Collections.emptyList()),
        new StudentDetail(student2, Collections.emptyList())
    );

    assertEquals(expected, studentDetails);

  }
}
