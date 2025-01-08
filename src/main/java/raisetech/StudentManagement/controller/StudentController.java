package raisetech.StudentManagement.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIを受付けるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生一覧検索です。
   * 全件検索を行うので、条件指定は行わないません。
   *
   * @return　受講生一覧（全件）
   */
  @GetMapping("/students")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * 受講生検索です。
   * IDに紐づく任意の受講生の情報を取得します。
   *
   * @param studentId 　受講生ID
   * @return 受講生単体の情報
   */
  @GetMapping("/student/{studentId}")
  public StudentDetail getStudent(@PathVariable String studentId) {
    return service.searchStudentDetail(studentId);
  }

  /**
   * 受講生の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 登録された受講生詳細
   */
  @PostMapping("/student")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生更新です。
   *
   * @param studentDetail 受講生詳細
   * @return 成功コメント
   */
  @PutMapping("/student")
  public ResponseEntity<String> updateStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

}
