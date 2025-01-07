package raisetech.StudentManagement.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@Validated
@RestController
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/students")
  public List<StudentDetail> getStudent() {
    List<Student> students = service.searchStudentList();
    List<StudentCourse> studentCourses = service.searchStudentsCourseList();
    return converter.convertStudentDetails(students, studentCourses);
  }

  // 受講生の新規登録画面です。
  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudent";
  }

  // 受講生の新規登録を行います。
  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute @Valid StudentDetail studentDetail,
      BindingResult result, Model model) {
    // 重複チェック
    if (service.hasDuplicateCourses(studentDetail)) {
      result.rejectValue("studentCourses", "error.studentCourses",
          "重複したコース名は登録できません");
    }
    // 入力エラーチェック
    if (result.hasErrors()) {
      result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
      return "registerStudent";
    }

    service.registerStudent(studentDetail);
    service.registerStudentCourse(studentDetail);

    return "redirect:/studentList";
  }

  @GetMapping("/student/{studentId}")
  public String getStudent(@PathVariable String studentId, Model model) {
    StudentDetail studentDetail = service.searchStudentDetail(studentId);
    model.addAttribute("studentDetail", studentDetail);
    return "updateStudent";
  }

  /**
   * 受講生情報更新です。
   *
   * @param studentDetail 受講生詳細
   * @return 成功コメント
   */
  @PostMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

}
