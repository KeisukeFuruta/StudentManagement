package raisetech.StudentManagement.data;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * 受講生コース情報を扱うオブジェクトです。
 */
@Getter
@Setter
public class StudentCourse {

  private String courseId;
  private String studentId;
  private String courseName;
  private LocalDateTime startDate;
  private LocalDateTime expectedEndDate;
}
