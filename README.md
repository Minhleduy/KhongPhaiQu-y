### 🎮 Game Arkanoid - Dự án Lập trình Hướng đối tượng (OOP)

# 👨‍💻 Thành viên nhóm
**Nhóm:** 12 - **Lớp:** INT 2204 6 
1. Lê Duy Minh - 24020231
2. Lê Văn Huy - 24020159
3. Trần Nam Khánh - 24020177


**Giảng viên hướng dẫn:** Kiều Văn Tuyên - Trương Xuân Hiếu

**Học kỳ:** HK1 - Năm học 2025 - 2026

---

## 🧾 Giới thiệu chung
**Arkanoid** là trò chơi cổ điển mà người chơi điều khiển thanh chắn (paddle) để đánh bóng phá vỡ các viên gạch.  
Dự án này được phát triển bằng **Java**, sử dụng các kỹ thuật **Lập trình Hướng đối tượng (OOP)** nhằm minh họa việc áp dụng các nguyên lý và mẫu thiết kế phần mềm (Design Patterns) trong lập trình thực tế.

### 🎯 Mục tiêu dự án
- Áp dụng kiến thức OOP để xây dựng một game hoàn chỉnh.  
- Sử dụng các **Design Pattern** phổ biến trong phát triển phần mềm.  
- Thực hành kỹ năng làm việc nhóm, quản lý dự án và tổ chức mã nguồn.
---

## ⚙️ Các tính năng nổi bật

1. Phát triển hoàn toàn bằng **Java 17+**, sử dụng **JavaFX** để xây dựng giao diện.  
2. Thể hiện đầy đủ **4 nguyên lý OOP**:
   - **Encapsulation (Đóng gói)**
   - **Inheritance (Kế thừa)**
   - **Polymorphism (Đa hình)**
   - **Abstraction (Trừu tượng hóa)**
3. Ứng dụng nhiều **Design Pattern** như:
   - Singleton
   - Factory Method
   - Template Method
   - State
4. Sử dụng **đa luồng (Multithreading)** giúp trò chơi chạy mượt mà và giao diện phản hồi nhanh.
5. Có **hiệu ứng âm thanh, hình ảnh động và vật phẩm tăng sức mạnh (Power-up)**.  
6. Hỗ trợ **lưu/tải tiến trình** và **bảng xếp hạng (Leaderboard)**.  

---

## 🧱 Cơ chế trò chơi

- Người chơi điều khiển thanh chắn để nảy quả bóng và phá các viên gạch trên màn hình.  
- Mỗi viên gạch bị phá có thể rơi ra **vật phẩm (Power-up)**.
- Khi phá hết toàn bộ gạch có thể phá, người chơi sẽ **qua màn tiếp theo**.  
- Mỗi màn sẽ **tăng dần độ khó** với các kiểu gạch sẽ đa dạng hơn.  
- Người chơi sẽ thua cuộc khi **hết mạng (Lives)** (mỗi người có 3 mạng khởi đầu).  
- Người chơi sẽ thắng khi **tiêu diệt được boss**
- Boss sẽ liên tục bắn đạn vào người chơi và gạch sẽ sinh ra ngẫu nhiên trong màn đánh boss

## 📊 Biểu đồ UML (Sơ đồ Thiết kế)

Biểu đồ UML (Unified Modeling Language) giúp trực quan hóa cấu trúc và luồng hoạt động của dự án.

### 1. Class Diagram (Biểu đồ Lớp)

Biểu đồ Lớp cho thấy cấu trúc tĩnh và mối quan hệ kế thừa của các đối tượng trong game.

![Class Diagram](docs/uml/class-diagram.png)


* **Lớp Cha (Base Class):** Lớp trừu tượng `GameObject` là gốc, định nghĩa các thuộc tính cơ bản (vị trí `x`, `y`, `width`, `height`, `imageView`).
* **Phân nhánh Kế thừa:**
    * `MovableObject` (vật thể di chuyển) và `Brick` (gạch) kế thừa trực tiếp từ `GameObject`.
    * Các lớp `Ball`, `Paddle`, `Boss`, và `PowerUp` (vật phẩm) kế thừa từ `MovableObject`.
    * Các loại gạch cụ thể (`NormalBrick`, `StrongBrick`, `UnbreakableBrick`, v.v.) kế thừa từ `Brick`.
    * Các loại vật phẩm cụ thể (`HeartItem`, `StrongBallItem`, `DoubleBallItem`, v.v.) kế thừa từ `PowerUp`.
* **Quan hệ Thành phần (Composition):**
    * `GameManager` là lớp trung tâm, chứa một danh sách các `GameObject` (`List<GameObject>`).
    * `GameUIController` giữ một tham chiếu (Singleton) đến `GameManager`.
    * `LevelManager` sử dụng `BrickMapLoader` để "sản xuất" ra các đối tượng `Brick`.

---
### 2. Sequence Diagram (Biểu đồ Tuần tự)

Biểu đồ tuần tự mô tả luồng hoạt động của một chức năng. Dưới đây là luồng "Khởi động màn chơi" (khi nhấn nút Start).


1.  `MainMenuController` (sau khi người dùng nhấn "Start") gọi `SceneManager.showGameScene()`.
2.  `SceneManager` tải `GameUI.fxml`.
3.  `FXMLLoader` kích hoạt `GameUIController.initialize()`.
4.  `GameUIController` gọi `GameManager.setGameRoot(pane)` và `setGameUIController(this)` để "cài đặt" `GameManager`.
5.  `GameUIController` gọi `GameManager.startGame()`.
6.  `GameManager` gọi `LevelManager.loadLevel(1)`.
7.  `LevelManager` tạo `Paddle`, `Ball` và gọi `BrickMapLoader.load(...)` để tạo ra các đối tượng `Brick`.
8.  `GameUIController` khởi động `AnimationTimer` (vòng lặp game).
9.  `AnimationTimer` liên tục gọi `GameManager.update(deltaTime)` và `GameUIController.updateUI()`.
   
---

## 🧠 Triển khai Design Pattern

### 1. Singleton
* **Được sử dụng trong:** `GameManager.java`, `LevelManager.java`, `SceneManager.java`, `SoundManager.java`
* **Mục đích:** Đảm bảo chỉ tồn tại duy nhất một thể hiện (instance) của các lớp quản lý tài nguyên hoặc điều khiển trò chơi, giúp đồng bộ hóa dữ liệu trong toàn bộ ứng dụng.

### 2. Factory Method
* **Được sử dụng trong:** `BrickMapLoader.java`
* **Mục đích:** Cung cấp một phương thức (`load`) để "sản xuất" ra các đối tượng gạch (`Brick`) khác nhau. Nó đọc một ký tự (`'N'`, `'S'`, `'T'`, v.v.) từ bản đồ và trả về một thể hiện của lớp gạch tương ứng, che giấu logic tạo đối tượng phức tạp.

### 3. Template Method
* **Được sử dụng trong:** `GameObject.java`, `MovableObject.java`, `PowerUp.java`, `Brick.java` (và các lớp con của chúng).
* **Mục đích:** Lớp cha (`PowerUp`) định nghĩa một "khuôn mẫu" cho một thuật toán (ví dụ: `startFalling`, `checkCollision`) và định nghĩa một bước trừu tượng (`abstract void applyEffect`). Các lớp con (`HeartItem`, `StrongBallItem`, v.v.) bắt buộc phải "điền vào" bước đó bằng cách cung cấp logic hiệu ứng của riêng mình.

### 4. State
* **Được sử dụng trong:** `GameManager.java`
* **Mục đích:** Cho phép `GameManager` thay đổi hành vi của nó một cách linh hoạt. Hành vi của phương thức `update()` thay đổi hoàn toàn dựa trên giá trị của biến trạng thái `currentState` (ví dụ: `GameState.PLAYING` sẽ kiểm tra gạch, trong khi `GameState.BOSS_FIGHT` sẽ kiểm tra va chạm với boss).

### 5. фаса Facad
* **Được sử dụng trong:** `SoundManager.java`, `BackgroundManager.java`, `ImageLoader.java`
* **Mục đích:** Cung cấp một giao diện đơn giản (`playBackgroundMusic()`, `setBackgroundForLevel()`) để che giấu các logic phức tạp bên trong (quản lý `MediaPlayer`, tạo `BackgroundImage`, xử lý cache `HashMap`, v.v.), giúp các file controller gọn gàng hơn.

---

## ⚡ Đa luồng (Multithreading)

Trò chơi sử dụng nhiều luồng xử lý riêng biệt để tăng hiệu năng và độ mượt:
1. **Game Loop Thread:** Cập nhật logic trò chơi ở tốc độ 60 FPS.  
2. **Rendering Thread:** Hiển thị đồ họa (JavaFX Application Thread).  
3. **Audio Thread Pool:** Chạy âm thanh không đồng bộ, không làm giật khung hình.  
4. **I/O Thread:** Xử lý lưu và tải dữ liệu mà không làm đơ giao diện.  

---

## 🧩 Cài đặt và chạy chương trình

### 1️⃣ Cài đặt
- Yêu cầu cài đặt:
  - Java 17 trở lên
  - Maven 3.9+ hoặc IDE hỗ trợ Maven (IntelliJ, Eclipse, VS Code,...)
 
    ## 🎮 Hướng dẫn sử dụng (Usage)

### 🕹️ Điều khiển (Controls)

- Sử dụng chuột để di chuyển thanh đỡ (paddle)

---

### 🧩 Cách chơi 

1. **Bắt đầu trò chơi:** Chọn “Start” trong menu chính.  
2. **Điều khiển paddle:** Dùng chuột để di chuyển trái - phải  
3. **Phá gạch:** Dùng bóng để phá hủy các viên gạch trên màn hình.  
4. **Thu thập vật phẩm:** Hứng các vật phẩm rơi xuống để nhận hiệu ứng đặc biệt.  
5. **Tránh mất bóng:** Không để bóng rơi xuống dưới paddle, nếu không bạn sẽ mất một mạng.  
6. **Hoàn thành màn chơi:** Phá hết các viên gạch có thể phá để qua màn tiếp theo, giết boss để thắng cuộc.

## 📸 Demo

### 📷 Ảnh minh họa

**Menu chính**  
![Main Menu](resources/images/background/Menu.png)
![GameOver](resources/images/background/GameOver.png)

**Giao diện trò chơi**  
![Màn 1](resources/images/background/BackGround1.png)
![Màn 2](resources/images/background/BackGround2.png)
![Màn 3](resources/images/background/BackGround3.png)
![Màn 4](resources/images/background/BackGround4.png)

**Hiệu ứng vật phẩm**  
![Items](resources?/images/items)

**Các loại gạch**  
![Brick](resources/images/brick)

**Giao diện Boss**
![Boss](resources/images/npc/Boss.png)
![Đạn Boss](resources/images/npc/boss_shoot.png)

---

### 🎬 Video minh họa (Video Demo)
![Video Demo](resources\video/full_video.mp4)

> 🎥 Toàn bộ video chơi thử được lưu tại thư mục: `docs/demo/gameplay.mp4`

---

## 🧰 Công nghệ sử dụng (Technologies Used)

| Công nghệ | Phiên bản | Mục đích |
|------------|------------|----------|
| **Java** | 17+ | Ngôn ngữ lập trình chính |
| **JavaFX** | 19.0.2 | Xây dựng giao diện người dùng |
| **Maven** | 3.9+ | Quản lý thư viện và biên dịch dự án |
| **Jackson** | 2.15.0 | Xử lý dữ liệu JSON (lưu điểm, cấu hình, v.v.) |

---

## ⚖️ Giấy phép (License)

Dự án này được phát triển **cho mục đích học tập**, không nhằm mục đích thương mại.  

**Lưu ý về đạo đức học thuật:**  
Mã nguồn này chỉ nên được **tham khảo**. Vui lòng tuân thủ **chính sách trung thực học thuật** của trường hoặc giảng viên hướng dẫn.  

---

## 📝 Notes

- Trò chơi được phát triển trong khuôn khổ môn **Lập trình Hướng đối tượng với Java**.  
- Tất cả mã nguồn do **các thành viên trong nhóm** thực hiện, dưới sự hướng dẫn của giảng viên.  
- Một số tài nguyên (hình ảnh, âm thanh) được sử dụng **cho mục đích học tập**, theo quy định *fair use*.  
- Dự án minh họa rõ **các khái niệm OOP** (Kế thừa, Đa hình, Đóng gói, Trừu tượng hóa) và **các mẫu thiết kế phần mềm (Design Patterns)**.  

---

*🕓 Cập nhật lần cuối: [Ngày/Tháng/Năm]*
