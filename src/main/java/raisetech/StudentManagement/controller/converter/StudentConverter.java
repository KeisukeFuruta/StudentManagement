package raisetech.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentCourseDetail;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * 受講生詳細を受講生や受講生コース情報、申込状況についてマッピングを行うコンバーターです。
 */
@Component
public class StudentConverter {

  /**
   * 受講生に紐づく受講生コース情報をマッピングする。
   * 受講生コース情報は受講生に対して複数存在するので、ループを回して受講生詳細情報を組み立てる。
   * 受講生コース情報と申込状況は1対1の関係にあるため、
   *
   * @param studentList       受講生一覧
   * @param studentCourseList 受講生コース情報のリスト
   * @param courseStatusList  申込状況のリスト
   * @return 受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> studentList,
      List<StudentCourse> studentCourseList, List<CourseStatus> courseStatusList) {
    List<StudentDetail> studentDetails = new ArrayList<>();

    studentList.forEach(student -> {
      StudentDetail studentDetail = buildStudentDetail(student, studentCourseList,
          courseStatusList);
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }

  /**
   * 受講生詳細を構築するメソッドです。
   *
   * @param student           受講生
   * @param studentCourseList 受講生に紐づく受講生コース情報
   * @param courseStatusList  受講生コース情報に紐づく申込状況
   * @return　受講生詳細
   */
  public StudentDetail buildStudentDetail(Student student, List<StudentCourse> studentCourseList,
      List<CourseStatus> courseStatusList) {

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);

    // studentに紐づくstudentCourseをフィルタリング
    List<StudentCourse> convertStudentCourseList = studentCourseList.stream()
        .filter(studentCourse -> student.getStudentId().equals(studentCourse.getStudentId()))
        .toList();

    // フィルタリング後のstudentCourseをstudentCourseDetailにセット
    List<StudentCourseDetail> studentCourseDetailList = convertStudentCourseList.stream()
        .map(studentCourse ->
            buildStudentCourseDetail(studentCourse, courseStatusList))
        .collect(Collectors.toList());

    studentDetail.setStudentCourseDetailList(studentCourseDetailList);
    return studentDetail;
  }

  /**
   * 受講生コース情報詳細を構築するメソッドです。
   *
   * @param studentCourse
   * @param courseStatusList
   * @return 受講生コース情報詳細
   */
  private StudentCourseDetail buildStudentCourseDetail(StudentCourse studentCourse,
      List<CourseStatus> courseStatusList) {

    StudentCourseDetail studentCourseDetail = new StudentCourseDetail();
    studentCourseDetail.setStudentCourse(studentCourse);

    CourseStatus courseStatus = findCourseStatus(studentCourse, courseStatusList);

    studentCourseDetail.setCourseStatus(courseStatus);
    return studentCourseDetail;
  }

  /**
   * 受講生コース情報に紐づく申込状況を検索するメソッドです。
   *
   * @param studentCourse
   * @param courseStatusList
   * @return 受講生コース情報に紐づく申申込状況
   */
  private CourseStatus findCourseStatus(StudentCourse studentCourse,
      List<CourseStatus> courseStatusList) {

    // todo:コンテキストアクションの改善
    CourseStatus courseStatus = courseStatusList.stream()
        .filter(status -> status.getCourseId().equals(studentCourse.getCourseId()))
        .findFirst()
        .get();
    return courseStatus;
  }
}
