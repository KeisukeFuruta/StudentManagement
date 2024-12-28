package raisetech.StudentManagement.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@Validated
@Controller
public class StudentController {

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentCourse> studentCourses = service.searchStudentsCourseList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentCourses));

    // デバッグ用
    StudentDetail studentDetail = new StudentDetail();
    System.out.println(studentDetail.getStudent());

    return "studentList";
  }

  @GetMapping("/studentCourseList")
  public String getStudentsCourseList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentCourse> studentCourses = service.searchStudentsCourseList();

    model.addAttribute("studentCourseList",
        converter.convertStudentDetails(students, studentCourses));
    return "studentCourseList";
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


  @PostMapping("/updateStudent")
  public String updateStudent(@ModelAttribute @Valid StudentDetail studentDetail,
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
    service.updateStudent(studentDetail);
    return "redirect:/studentList";
  }

}
