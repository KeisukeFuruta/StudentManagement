package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * 受講生情報を扱うリポジトリ。
 * <p>
 * 全件検索や単一条件での検索、コース情報の検索が行えるクラスです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 全件検索します。
   *
   * @return 全件検索した受講生情報の一覧
   */
  @Select("SELECT * FROM students")
  List<Student> search();

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentsCourses();

  /**
   * 入力フォームから受け取ったデータをDBに登録します。
   *
   * @param student 登録する受講生のデータを格納したオブジェクト
   */
  @Insert(
      "INSERT INTO students (name,furigana,nickname,email_address,residential_area,age,gender,remark,is_deleted)"
          + "VALUES (#{name}, #{furigana}, #{nickname}, #{emailAddress}, #{residentialArea}, #{age}, #{gender}, #{remark}, false)")
  void registerStudent(Student student);


}
