package raisetech.StudentManagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行できて空のリストが返ってくること() throws Exception {
    mockMvc.perform(get("/students"))
        .andExpect(status().isOk());

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 正常系_受講生詳細の受講生で適正な値を入力した時に入力チェックに異常発生しないこと() {
    Student student = new Student();
    student.setStudentId("999");
    student.setName("江並公史");
    student.setFurigana("エナミコウジ");
    student.setNickname("エナミ");
    student.setEmailAddress("test@test.com");
    student.setResidentialArea("奈良県");
    student.setGender("男性");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(0);
  }

  @Test
  void 異常系_受講生詳細の受講生で名前に10文字以上を入力したときに入力チェックに掛かること() {
    Student student = new Student();
    student.setStudentId("999");
    student.setName("テストです、サイズが10以上でエラーを吐きます");
    student.setFurigana("エナミコウジ");
    student.setNickname("エナミ");
    student.setEmailAddress("test@test.com");
    student.setResidentialArea("奈良県");
    student.setGender("男性");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").containsOnly("名前は必須です");
  }

  @Test
  void 正常系_受講生詳細検索が実行できて空のリストが返ってくること()
      throws Exception {
    String studentId = "999";
    mockMvc.perform(get("/students/{id}", studentId))
        .andExpect(status().isOk());
  }

  @Test
  void 異常系_受講生詳細検索に3以上の数字idを渡すと入力チェックに掛かること()
      throws Exception {
    String studentId = "9999";
    mockMvc.perform(get("/students/{id}", studentId))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors['getStudent.studentId']")
            .value("IDは1から3桁の数字で入力してください。"));
  }

  @Test
  void 異常系_受講生詳細検索に存在しないidを渡すと入力チェックに掛かること()
      throws Exception {
    String studentId = "999";

    when(service.searchStudentDetail(studentId))
        .thenThrow(new IllegalArgumentException("リクエストされたIDが存在しません。"));

    mockMvc.perform(get("/students/{id}", studentId))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error")
            .value("リクエストされたIDが存在しません。"));
  }

  @Test
  void 異常系_受講生詳細検索に3以下の文字列idを渡すと入力チェックに掛かること()
      throws Exception {
    String studentId = "aaa";
    mockMvc.perform(get("/students/{id}", studentId))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors['getStudent.studentId']")
            .value("IDは数字のみ入力してください。"));
  }

  @Test
  void 正常系_受講生登録ができること() throws Exception {
    String studentJson = """
        {
            "student": {
                "name": "渡辺凛太郎",
                "furigana":"ワタナベリンタロウ",
                "nickname": "しんにょう",
                "emailAddress": "test@test.com",
                "residentialArea": "東京",
                "age": 15,
                "gender": "男性",
                "remark": "なし"
                },
                "studentCourseList": [
                    {
                        "courseName": "Javaコース"
                    }
                ]
        }
        """;
    mockMvc.perform(post("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(studentJson))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void 異常系_受講生登録時に性別の入力チェックにかかること() throws Exception {
    String studentJson = """
        {
            "student": {
                "name": "渡辺凛太郎",
                "furigana":"ワタナベリンタロウ",
                "nickname": "しんにょう",
                "emailAddress": "test@test.com",
                "residentialArea": "東京",
                "age": 15,
                "gender": "テスト",
                "remark": "なし"
                },
                "studentCourseList": [
                    {
                        "courseName": "Javaコース"
                    }
                ]
        }
        """;

    mockMvc.perform(post("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(studentJson))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors['student.gender']")
            .value("性別は「男性」「女性」「回答しない」のいずれかでなければなりません。"));

  }

  @Test
  void 異常系_受講生登録時にメールアドレスの入力チェックにかかること() throws Exception {
    String studentJson = """
        {
            "student": {
                "name": "渡辺凛太郎",
                "furigana":"ワタナベリンタロウ",
                "nickname": "しんにょう",
                "emailAddress": "test",
                "residentialArea": "東京",
                "age": 15,
                "gender": "男性",
                "remark": "なし"
                },
                "studentCourseList": [
                    {
                        "courseName": "Javaコース"
                    }
                ]
        }
        """;

    mockMvc.perform(post("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(studentJson))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors['student.emailAddress']")
            .value("無効なメールアドレスです。"));

  }

  // todo:コース情報が重複する際のエラーチェックテスト,200番で通ってしまう。
  @Test
  void 異常系_受講生登録時にコース名重複の入力チェックにかかること() throws Exception {
    String studentJson = """
        {
            "student": {
                "name": "渡辺凛太郎",
                "furigana":"ワタナベリンタロウ",
                "nickname": "しんにょう",
                "emailAddress": "test@test.com",
                "residentialArea": "東京",
                "age": 15,
                "gender": "男性",
                "remark": "なし",
                "isDeleted": false
                },
                "studentCourseList": [
                    {
                        "courseName": "Javaコース"
                    },
                    {
                        "courseName": "Javaコース"
                    }
                ]
        }
        """;
    doThrow(new IllegalArgumentException("重複したコース名は登録できません"))
        .when(service)
        .validateStudentDetail(any());

    mockMvc.perform(post("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(studentJson))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("重複したコース名は登録できません"));
  }

  @Test
  void 正常系_受講生更新ができること() throws Exception {
    String studentJson = """
        {
            "student": {
                "studentId": "56",
                "name": "渡辺凛太郎",
                "furigana":"ワタナベリンタロウ",
                "nickname": "しんにょう",
                "emailAddress": "test@test.com",
                "residentialArea": "東京",
                "age": 15,
                "gender": "男性",
                "remark": "なし"
                },
                "studentCourseList": [
                    {
                        "courseId": "56",
                        "courseName": "Javaコース"
                    }
                ]
        }
        """;

    mockMvc.perform(put("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(studentJson))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string("更新処理が成功しました。"));

    verify(service, times(1)).updateStudent(any(StudentDetail.class));
  }

  @Test
  void 異常系_受講生更新時にメールアドレスの入力チェックにかかること() throws Exception {
    String studentJson = """
        {
            "student": {
                "studentId": "56",
                "name": "渡辺凛太郎",
                "furigana":"ワタナベリンタロウ",
                "nickname": "しんにょう",
                "emailAddress": "test",
                "residentialArea": "東京",
                "age": 15,
                "gender": "男性",
                "remark": "なし"
                },
                "studentCourseList": [
                    {
                        "courseId": "56",
                        "courseName": "Javaコース"
                    }
                ]
        }
        """;

    mockMvc.perform(put("/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(studentJson))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors['student.emailAddress']")
            .value("無効なメールアドレスです。"));
  }

}
