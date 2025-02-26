package raisetech.StudentManagement.controller.converter;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentCourseDetail;
import raisetech.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter sut;
  private Student student1;
  private Student student2;
  private StudentCourse studentCourse1;
  private StudentCourse studentCourse2;
  private StudentCourse studentCourse3;

  @BeforeEach
  void setup() {
    sut = new StudentConverter();

    student1 = new Student("1", "髙比良くるま", "たかひらくるま", "くるま", "kuruma@test.com",
        "東京", 30, "男性", "なし", false);
    student2 = new Student("2", "松井ケムリ", "まついけむり", "けむり", "kemuri@test.com",
        "東京", 30, "男性", "なし", false);
    studentCourse1 = new StudentCourse("1", "1", "Javaコース", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));
    studentCourse2 = new StudentCourse("2", "1", "WPコース", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));
    studentCourse3 = new StudentCourse("3", "2", "AWSコース", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));
  }

  @Test
  void 正常系_受講生に対する受講生コースが正しくマッピングされる() throws Exception {
    List<Student> studentList = Arrays.asList(student1, student2);
    List<StudentCourse> studentCourseList = Arrays.asList(studentCourse1, studentCourse2,
        studentCourse3);

    CourseStatus courseStatus1 = new CourseStatus("1", "1", "仮申込");
    CourseStatus courseStatus2 = new CourseStatus("2", "2", "仮申込");
    CourseStatus courseStatus3 = new CourseStatus("3", "3", "仮申込");
    List<CourseStatus> courseStatusList = Arrays.asList(courseStatus1, courseStatus2,
        courseStatus3);

    // actual（実際の値）をstudentDetailsに格納
    List<StudentDetail> studentDetails = sut.convertStudentDetails(studentList,
        studentCourseList, courseStatusList);
    
    // StudentCourseDetailに格納
    List<StudentCourseDetail> studentCourseDetailList1 = Arrays.asList(
        new StudentCourseDetail(studentCourse1, courseStatus1),
        new StudentCourseDetail(studentCourse2, courseStatus2));
    List<StudentCourseDetail> studentCourseDetailList2 = Arrays.asList(
        new StudentCourseDetail(studentCourse3, courseStatus3));

    // expected（期待値）を準備
    List<StudentDetail> expected = List.of(
        new StudentDetail(student1, studentCourseDetailList1),
        new StudentDetail(student2, studentCourseDetailList2)
    );

    assertEquals(expected, studentDetails);
  }

  @Test
  void 異常系_受講生と受講生コースのstudentIdが一致しない場合() throws Exception {
    List<Student> studentList = Arrays.asList(student1, student2);
    List<StudentCourse> studentCourseList = List.of(
        new StudentCourse("1", "3", "Javaコース", LocalDateTime.now(),
            LocalDateTime.now().plusYears(1)),
        new StudentCourse("2", "4", "AWSコース", LocalDateTime.now(),
            LocalDateTime.now().plusYears(1)));

    // 申込状況のリストを準備
    CourseStatus courseStatus4 = new CourseStatus("4", "4", "仮申込");
    CourseStatus courseStatus5 = new CourseStatus("5", "5", "仮申込");
    CourseStatus courseStatus6 = new CourseStatus("6", "6", "仮申込");
    List<CourseStatus> courseStatusList = Arrays.asList(courseStatus4, courseStatus5,
        courseStatus6);

    List<StudentDetail> studentDetails = sut.convertStudentDetails(studentList,
        studentCourseList, courseStatusList);

    List<StudentDetail> expected = List.of(
        new StudentDetail(student1, Collections.emptyList()),
        new StudentDetail(student2, Collections.emptyList())
    );

    assertEquals(expected, studentDetails);

  }
}
