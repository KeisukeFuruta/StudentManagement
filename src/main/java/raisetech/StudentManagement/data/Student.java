package raisetech.StudentManagement.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Student {

  private String studentId;
  private String name;
  private String furigana;
  private String nickname;
  private String emailAddless;
  private String residentalArea;
  private int age;
  private String gender;
  private String remark;
  private boolean isDeleted;

}