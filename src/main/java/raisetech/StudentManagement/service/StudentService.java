package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    // 検索処理
    List<Student> students = repository.search();

    // 絞り込みをする。年齢が30代以上の人のみ抽出する。
    // 抽出したリストをコントローラーに返す。
    List<Student> filteredStudents = students.stream()
        .filter(student -> student.getAge() >= 30)
        .toList();

    return filteredStudents;
  }

  public List<StudentCourse> searchStudentsCourseList() {
    // 絞り込み検索で「Javaコース」のコース情報のみを抽出する。
    // 抽出したリストをコントローラーに返す。

    List<StudentCourse> studentCourseList = repository.searchStudentsCourses();

    List<StudentCourse> javaOnly = studentCourseList.stream()
        .filter(studentCourse -> studentCourse.getCourseName().equals("Javaコース"))
        .toList();

    return javaOnly;
  }

}
