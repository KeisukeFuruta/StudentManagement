package raisetech.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
import raisetech.StudentManagement.exception.ErrorResponse;
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
   * 受講生詳細の一覧検索です。
   * 全件検索を行うので、条件指定は行わないません。
   *
   * @return　受講生詳細一覧（全件）
   */
  @Operation(summary = "一覧検索", description = "受講生の一覧を検索します。")
  @GetMapping("/students")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * 受講生詳細の検索です。
   * IDに紐づく任意の受講生の情報を取得します。
   *
   * @param studentId 　受講生ID
   * @return 受講生詳細
   */
  @Operation(summary = "受講生詳細検索", description = "IDに紐づく任意の受講生の情報を取得します。")
  @ApiResponses(value =
      {
          @ApiResponse(responseCode = "200", description = "成功"),
          @ApiResponse(responseCode = "400", description = "リクエストされたIDが存在しません。", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "サーバー側でエラーが起きた可能性があります。", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      }
  )
  @GetMapping("/students/{id}")
  public StudentDetail getStudent(
      @PathVariable("id")
      @Size(min = 1, max = 3, message = "IDは1から3桁の数字で入力してください。")
      @Pattern(regexp = "^\\d+$", message = "IDは数字のみ入力してください。") String studentId) {
    return service.searchStudentDetail(studentId);
  }


  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生登録", description = "受講生詳細の登録を行います。")
  @ApiResponses(value =
      {
          @ApiResponse(responseCode = "200", description = "成功"),
          @ApiResponse(responseCode = "400", description = "入力値が不正です", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      }
  )
  @PostMapping("/students")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    // コース名重複チェック
    service.validateDuplicateStudentCourse(studentDetail);

    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生更新を行います。
   * キャンセルフラグの更新もここで行います。（論理削除）
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(summary = "受講生更新", description = "受講生詳細の更新を行います。キャンセルフラグの更新もここで行います。（論理削除）")
  @ApiResponses(value =
      {
          @ApiResponse(responseCode = "200", description = "更新処理が成功しました。"),
          @ApiResponse(responseCode = "400", description = "入力値が不正です", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
      }
  )
  @PutMapping("/students")
  public ResponseEntity<String> updateStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  // エラーを投げる用のメソッド
  private boolean shouldThrowError() {
    return true; // 常にエラーを発生させる
  }

}
