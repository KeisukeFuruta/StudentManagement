package raisetech.StudentManagement.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * 受講生の情報を扱うオブジェクトです。
 */
@Getter
@Setter
@Validated

public class Student {

  private String studentId;

  @Size(min = 1, max = 10, message = "名前は必須です")
  private String name;

  @NotNull
  private String furigana;

  private String nickname;

  @Email(message = "無効なメールアドレスです")
  private String emailAddress;

  private String residentialArea;
  private int age;
  @Pattern(regexp = "男性|女性|回答しない", message = "性別は「男性」「女性」「回答しない」のいずれかでなければなりません")
  private String gender;
  private String remark;
  private boolean isDeleted;

}
