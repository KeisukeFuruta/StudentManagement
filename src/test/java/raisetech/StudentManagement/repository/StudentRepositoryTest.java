package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 正常系_受講生の全件検索が行えること() {
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 正常系_受講生の検索が行えること() {
    Student expected = new Student("1", "山田太郎", "ヤマダタロウ", "タロちゃん",
        "taro.yamada@example.com", "東京都", 25, "男性", "なし", false);

    Student actual = sut.searchStudent("1");

    assertEquals(expected, actual);

  }

  @Test
  void 正常系_受講生のコース情報の全件検索ができること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生IDに紐づく受講生コース情報が検索できること() {
    StudentCourse studentCourse1 = new StudentCourse("1", "1", "Java",
        LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 8, 10, 0, 0));
    StudentCourse studentCourse2 = new StudentCourse("2", "1", "AWS",
        LocalDateTime.of(2024, 5, 10, 0, 0), null);
    List<StudentCourse> expected = List.of(studentCourse1, studentCourse2);

    List<StudentCourse> actual = sut.searchStudentCourse("1");

    assertEquals(expected, actual);
  }

  @Test
  void 正常系_受講生の登録が行えること() {
    Student student = new Student("", "髙比良くるま", "たかひらくるま", "くるま",
        "kuruma@test.com",
        "東京", 30, "男性", "なし", false);

    sut.registerStudent(student);

    List<Student> actual = sut.search();

    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 正常系_受講生コース情報の登録が行えること() {
    StudentCourse studentCourse = new StudentCourse("", "5", "Javaコース", LocalDateTime.now(),
        LocalDateTime.now().plusYears(1));
    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 正常系_受講生の更新が行えること() {
    Student expected = new Student("1", "テスト", "たかひらくるま", "くるま",
        "kuruma@test.com",
        "東京", 30, "男性", "なし", false);
    sut.updateStudent(expected);

    Student actual = sut.searchStudent("1");

    assertEquals(expected, actual);
  }

  @Test
  void 正常系_受講コース情報のコース名の更新が行えること() {
    StudentCourse studentCourse1 = new StudentCourse("1", "1", "テスト",
        LocalDateTime.of(2024, 1, 1, 0, 0), LocalDateTime.of(2024, 8, 10, 0, 0));
    StudentCourse studentCourse2 = new StudentCourse("2", "1", "AWS",
        LocalDateTime.of(2024, 5, 10, 0, 0), null);
    List<StudentCourse> expected = List.of(studentCourse1, studentCourse2);
    sut.updateStudentCourse(studentCourse1);

    List<StudentCourse> actual = sut.searchStudentCourse("1");

    assertEquals(expected, actual);
  }
}
