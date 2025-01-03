package raisetech.StudentManagement.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Student {

  private String studentId;

  @Size(min = 1, max = 10, message = "名前は必須です")
  private String name;

  private String furigana;
  private String nickname;

  @Email
  private String emailAddress;

  private String residentialArea;
  private int age;
  private String gender;
  private String remark;
  private boolean isDeleted;

}
