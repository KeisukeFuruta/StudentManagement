package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.CourseStatus;
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
  void 正常系_受講生の受講生コース情報の全件検索ができること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 正常系_受講生IDに紐づく受講生コース情報が検索できること() {
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

  @Test
  void 正常系_申込状況の全件検索が行えること() {
    List<CourseStatus> actual = sut.searchStatusList();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 正常系_申込状況の検索が行えること() {
    CourseStatus courseStatus1 = new CourseStatus("1", "1", "仮申込");
    CourseStatus courseStatus2 = new CourseStatus("2", "2", "仮申込");
    List<CourseStatus> expected = List.of(courseStatus1, courseStatus2);

    List<CourseStatus> actual = sut.searchStatus("1");

    assertEquals(expected, actual);
  }

  @Test
  void 正常系_申込状況の登録が行えること() {
    CourseStatus courseStatus = new CourseStatus("", "6", "仮申込");
    sut.registerCourseStatus(courseStatus);

    List<CourseStatus> actual = sut.searchStatusList();

    assertThat(actual.size()).isEqualTo(6);
  }

  @Test
  void 正常系_申込状況の更新が行えること() {
    CourseStatus courseStatus1 = new CourseStatus("1", "1", "本申込");
    CourseStatus courseStatus2 = new CourseStatus("2", "2", "仮申込");
    List<CourseStatus> expected = List.of(courseStatus1, courseStatus2);

    sut.updateCourseStatus(courseStatus1);

    List<CourseStatus> actual = sut.searchStatus("1");

    assertEquals(expected, actual);
  }

  @Test
  void 正常系_検索された条件に基づいて受講生が検索されること_名前で検索() {
    Student student = new Student("1", "山田太郎", "ヤマダタロウ", "タロちゃん",
        "taro.yamada@example.com", "東京都", 25, "男性", "なし", false);
    List<Student> expected = List.of(student);

    List<Student> actual = sut.searchStudentCondition("山田", null, null, null, null, null);

    assertEquals(expected, actual);
  }

  @Test
  void 正常系_検索された条件に基づいて受講生が検索されること_フリガナで検索() {
    Student student1 = new Student("1", "山田太郎", "ヤマダタロウ", "タロちゃん",
        "taro.yamada@example.com", "東京都", 25, "男性", "なし", false);
    Student student2 = new Student("3", "佐藤一郎", "サトウイチロウ", "イッチー",
        "ichiro.sato@example.com", "北海道", 28, "男性", "なし", false);
    List<Student> expected = List.of(student1, student2);

    List<Student> actual = sut.searchStudentCondition(null, "ロウ", null, null, null, null);

    assertEquals(expected, actual);
  }

  @Test
  void 正常系_検索された条件に基づいて受講生が検索されること_住所で検索() {
    Student student = new Student("1", "山田太郎", "ヤマダタロウ", "タロちゃん",
        "taro.yamada@example.com", "東京都", 25, "男性", "なし", false);
    List<Student> expected = List.of(student);

    List<Student> actual = sut.searchStudentCondition(null, null, "東京", null, null, null);

    assertEquals(expected, actual);
  }

  @Test
  void 正常系_検索された条件に基づいて受講生が検索されること_年齢で検索() {
    Student student = new Student("1", "山田太郎", "ヤマダタロウ", "タロちゃん",
        "taro.yamada@example.com", "東京都", 25, "男性", "なし", false);
    List<Student> expected = List.of(student);

    List<Student> actual = sut.searchStudentCondition(null, null, null, 25, null, null);

    assertEquals(expected, actual);
  }

  @Test
  void 正常系_検索された条件に基づいて受講生が検索されること_性別で検索() {
    Student student1 = new Student("1", "山田太郎", "ヤマダタロウ", "タロちゃん",
        "taro.yamada@example.com", "東京都", 25, "男性", "なし", false);
    Student student2 = new Student("3", "佐藤一郎", "サトウイチロウ", "イッチー",
        "ichiro.sato@example.com", "北海道", 28, "男性", "なし", false);
    Student student3 = new Student("5", "高橋健二", "タカハシケンジ", "ケンジ",
        "kenji.takahashi@example.com", "神奈川県", 35, "男性", "受け放題", false);

    List<Student> expected = List.of(student1, student2, student3);

    List<Student> actual = sut.searchStudentCondition(null, null, null, null, "男性", null);

    assertEquals(expected, actual);
  }

  @Test
  void 正常系_検索された条件に基づいて受講生が検索されること_備考で検索() {
    Student student3 = new Student("5", "高橋健二", "タカハシケンジ", "ケンジ",
        "kenji.takahashi@example.com", "神奈川県", 35, "男性", "受け放題", false);
    List<Student> expected = List.of(student3);

    List<Student> actual = sut.searchStudentCondition(null, null, null, null, null, "受け放題");

    assertEquals(expected, actual);
  }

  @Test
  void 正常系_検索された条件に基づいて受講生が検索されること_コース名で検索() {
    StudentCourse studentCourse = new StudentCourse("2", "1", "AWS",
        LocalDateTime.of(2024, 5, 10, 0, 0), null);
    List<StudentCourse> expected = List.of(studentCourse);

    List<StudentCourse> actual = sut.searchCourseCondition("AWS");

    assertEquals(expected, actual);
  }

  @Test
  void 正常系_検索された条件に基づいて受講生が検索されること_申込状況で検索() {
    CourseStatus courseStatus = new CourseStatus("5", "5", "受講中");
    List<CourseStatus> expected = List.of(courseStatus);

    List<CourseStatus> actual = sut.searchStatusCondition("受講中");

    assertEquals(expected, actual);
  }

}
