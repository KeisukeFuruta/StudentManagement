# StudentManagement
Javaコースで作成中の受講生管理システムです。\
現在も課題を進めている最中ですので、随時修正が加えられます。\
都度READMEを変更できているわけではありません、ご容赦ください。

## 概要
このプロジェクトはプログラミングについて教えるスクールが受講生の情報を保持・分析するための管理システムです。\
スクール運営者が使用することを想定しており、CRUD操作中心のシンプルで使いやすい設計を目指しています。

## 作成背景
JavaやSpringBootの学習成果を形にするために作成しております。\
実務で使用されているとよく耳にする以下の技術やツールを採用し、作成を進めております。
- REST APIの設計と実装：データのCRUD操作をサポート
- 自動テスト：JUnitを使用して単体テストを実装

## 主な使用技術
### バックエンド
 ![Java](https://img.shields.io/badge/Java-21-orange)
 ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-green)
### フロントエンド
未実装

### インフラ
未実装

### 使用ツール
 ![MyBatis](https://img.shields.io/badge/MyBatis-orange)
 ![JUnit5](https://img.shields.io/badge/JUnit5-green)
 ![Postman](https://img.shields.io/badge/Postman-red)
 ![Swagger](https://img.shields.io/badge/Swagger-lightblue)
 ![GitHub](https://img.shields.io/badge/-GitHub-181717.svg?logo=github&style=flat)
 ![IntelliJ IDEA](https://img.shields.io/badge/-intellij%20IDEA-000.svg?logo=intellij-idea&style=flat)

## 機能一覧
| 機能 | 詳細 |
|:---|:---|
| 受講生詳細の登録 | 氏名、住所、メールアドレス等受講生の情報と、受講生コースをセットで登録します |
| 受講生のID検索 | 受講生IDを指定し、一位の受講生詳細を取得します |
| 受講生詳細の更新 | 受講生IDを指定し、任意の受講生詳細を更新します。<br> ※削除処理については論理削除として実装しているため、更新処理として行います |

※言葉の定義は以下の通りです。
- 受講生：氏名、メールアドレス、住所、年齢、性別等を持つオブジェクト
- 受講生コース：コース名、開始日、終了日等をもつオブジェクト
- 受講生詳細：受講生、受講生コースを持つオブジェクト

## 設計書
### API仕様書
（※スクリーンショットだけなので、長いです。）
<details>
  <summary>画像を表示する</summary>
  <img src="https://github.com/user-attachments/assets/79932a2c-736d-48c4-b00d-bfdba736c649" alt="画像説明" />
</details>



### APIのURL設計
| HTTPメソッド | URL | 処理内容 |
|:---|:---|:---|
| POST | /api/students | 受講生の作成 |
| GET | /api/students | 受講生詳細の一覧取得 |
| GET | /api/students/{id} | 指定した受講生IDの受講生詳細の取得 |
| PUT | /api/students | 受講生詳細の更新 |

## 今後の展望
- 全体に関わる回収
  - 受講生コースに受講状況がわかる情報を追加します。（仮申込, 本申込, 受講中, 受講終了）
  - 検索条件を自由に設定できるよう機能を追加します。（フリガナ検索、住所別検索、年齢別検索　等）
- フロントエンドの追加
- クラウド環境へのデプロイ
