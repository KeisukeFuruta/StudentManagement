package raisetech.StudentManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import raisetech.StudentManagement.repository.StudentRepository;

@SpringBootApplication

public class Application {

  @Autowired
  private StudentRepository repository;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  // todo:is_deletedをupdateに追加
  // todo:is_deletedされたものを受講生一覧に表示させない
}
