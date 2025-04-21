package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   *
   * @return 受講生一覧（全件）
   */
  List<Student> search();

  /**
   * 受講生の検索を行います。
   *
   * @param studentId 受講生ID
   * @return 受講生
   */
  Student searchStudent(String studentId);

  /**
   * 受講生のコース情報の全件検索を行います。
   *
   * @return 受講生のコース情報（全件）
   */
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講コースの申込状況の全件検索を行います。
   */
  List<CourseStatus> searchStatusList();

  /**
   * 受講生IDに紐づく受講生コース情報を検索します。
   *
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報
   */
  List<StudentCourse> searchStudentCourse(String studentId);

  /**
   * 受講コースIDに紐づくコース状況を検索します。
   *
   * @param studentId
   * @return
   */
  List<CourseStatus> searchStatus(String studentId);

  /**
   * 受講生を新規登録します。
   * studentIdに関しては、自動採番を行います。
   *
   * @param student 登録する受講生のデータを格納したオブジェクト
   */
  void registerStudent(Student student);

  /**
   * 受講生コース情報を新規登録します。
   * courseIdに関しては、自動裁判を行います。
   *
   * @param studentCourse 受講生コース情報
   */
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 申込状況を新規登録します。
   * statusIdは自動採番、courseIdはServiceクラスにて設定しています。
   * 新規登録時のstatusは全て'仮申込'になるよう設定しています。
   *
   * @param courseStatus
   */
  void registerCourseStatus(CourseStatus courseStatus);

  /**
   * 受講生を更新します。
   *
   * @param student 受講生
   */
  void updateStudent(Student student);

  /**
   * 受講コース情報のコース名を更新します。
   *
   * @param studentCourse 受講生コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);

  /**
   * 申込状況を更新します。
   *
   * @param courseStatus 　申込状況
   */
  void updateCourseStatus(CourseStatus courseStatus);

  /**
   * 指定された条件に基づき、受講生を検索します。
   *
   * @param name            名前
   * @param furigana        フリガナ
   * @param residentialArea 住所
   * @param age             年齢
   * @param gender          性別
   * @param remark          備考
   * @return 該当する受講生
   */
  List<Student> searchStudentCondition(
      @Param("name") String name,
      @Param("furigana") String furigana,
      @Param("residentialArea") String residentialArea,
      @Param("age") Integer age,
      @Param("gender") String gender,
      @Param("remark") String remark
  );

  /**
   * コース名を指定し、該当する受講コース情報を返します
   *
   * @param courseName
   * @return　該当する受講コース情報
   */
  List<StudentCourse> searchCourseCondition(
      @Param("courseName") String courseName
  );

  /**
   * 受講状況を指定し、該当する申込状況を返します
   *
   * @param status
   * @return　該当する申込状況
   */
  List<CourseStatus> searchStatusCondition(
      @Param("status") String status
  );

}
