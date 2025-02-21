package raisetech.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentCourseDetail;

@Component
public class StudentCourseConverter {

  public List<StudentCourseDetail> convertStudentCourseDetails(
      List<StudentCourse> studentCourseList, List<CourseStatus> courseStatusList) {
    List<StudentCourseDetail> studentCourseDetails = new ArrayList<>();

    studentCourseList.forEach(studentCourse -> {
      StudentCourseDetail studentCourseDetail = new StudentCourseDetail();
      studentCourseDetail.setStudentCourse(studentCourse);

      Optional<CourseStatus> convertCourseStatusList = courseStatusList.stream()
          .filter(courseStatus -> studentCourse.getCourseId().equals(courseStatus.getCourseId()))
          .findFirst();

      studentCourseDetail.setCourseStatus(convertCourseStatusList.orElse(null));
      studentCourseDetails.add(studentCourseDetail);
    });

    return studentCourseDetails;
  }

}
