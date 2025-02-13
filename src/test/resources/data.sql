INSERT INTO students(name, furigana, nickname, email_address, residential_area, age, gender, remark, is_deleted)
VALUES('山田太郎', 'ヤマダタロウ', 'タロちゃん', 'taro.yamada@example.com', '東京都', 25, '男性','なし',false),
      ('鈴木花子', 'スズキハナコ', 'ハナちゃん', 'hanako.suzuki@example.com', '大阪府', 30, '女性','なし',false),
      ('佐藤一郎', 'サトウイチロウ', 'イッチー', 'ichiro.sato@example.com', '北海道', 28, '男性','なし',false),
      ('田中美咲', 'タナカミサキ', 'ミサキ', 'misaki.tanaka@example.com', '福岡県', 22, '女性','なし',false),
      ('高橋健二', 'タカハシケンジ', 'ケンジ', 'kenji.takahashi@example.com', '神奈川県', 35, '男性','なし',false);

INSERT INTO students_courses(student_id, course_name,start_date,expected_end_date)
VALUES(1, 'Java', '2024-01-01','2024-08-10'),
      (1,'AWS','2024-05-10',NULL),
      (2,'デザイン','2022-04-01','2023-07-07'),
      (3,'webマーケティング','2024-08-04',NULL),
      (4,'Java','2021-10-01','2022-01-01');
