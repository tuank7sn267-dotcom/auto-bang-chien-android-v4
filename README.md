# Auto Bang Chiến — bản Android 0.2.0

Bản Android chạy trực tiếp trên điện thoại, làm việc **giống bản máy tính mới nhất**:
nhận diện màn hình bằng ảnh mẫu, bấm → chờ màn hình đổi, tự xử lý các tình huống như bản PC.

## Khác với bản 0.1.0 (bản không tự chơi được)
- Chụp màn hình đúng cách cho Android 14/15 (đăng ký callback bắt buộc) và luôn chụp theo khung ngang.
- Bấm tuần tự, chờ từng cử chỉ xong (trước đây bấm dồn nên Android huỷ gần hết các lần bấm,
  gồm cả lần chọn deck → lỗi "chọn deck rồi mà không bấm được").
- Nhận diện nhanh (trước đây rất chậm, mỗi vòng hàng chục giây).
- Chọn deck **chờ thấy dấu tích đỏ** rồi mới bấm Tấn công.
- Banner "Đã bị đánh bại" / thanh đầy → quay lại, ghép bang hội mới (không quét ô nhầm).
- Khi đánh: bấm **5 ô nhân vật**; hết trận **huỷ ngay các lần bấm còn xếp hàng**.
- **Tự phục hồi khi bấm nhầm:** màn hình lạ có mũi tên quay lại → bấm quay lại; về màn hình chính → bấm
  **Bang hội**; ở màn Bang hội → bấm **Trò chơi**; rồi tiếp tục Game chọn → Bang chiến.
- Màn hình chính của app hiển thị nhật ký chạy và trạng thái Trợ năng.

## Build bằng GitHub Actions (không cần cài Android Studio)
1. Tạo tài khoản GitHub (miễn phí) và một kho mã mới (**New repository**; nên chọn **Private**).
2. Giải nén file zip này. Trong kho vừa tạo bấm **Add file → Upload files**, kéo thả **toàn bộ nội dung**
   thư mục đã giải nén (nhớ có thư mục `.github`), bấm **Commit changes** (nhánh `main`).
   - Hoặc dùng dòng lệnh: `git init`, `git add .`, `git commit -m "init"`, `git branch -M main`,
     `git remote add origin <địa-chỉ-kho>`, `git push -u origin main`.
3. Vào tab **Actions**, thấy lần chạy "Build APK" (khoảng 5–10 phút). Dấu ✓ xanh là xong.
4. Tải APK: vào tab **Releases** (hoặc mục "Releases" bên phải trang kho) → bản **latest** →
   tải `app-debug.apk` ngay trên điện thoại. Cũng có thể tải ở mục **Artifacts** của lần chạy.
5. Mỗi lần sửa code và đẩy lên GitHub, APK mới tự được build lại. APK mới **cài đè** được bản cũ
   (đã dùng khoá ký cố định). **Lần đầu** nếu đang có bản 0.1.0 thì phải **gỡ bản cũ** trước.
6. Nếu lần build báo lỗi đỏ ❌: bấm vào lần chạy, copy dòng lỗi (mục "Build debug APK") gửi lại để sửa.

## Cài và chạy trên điện thoại
1. Mở file APK, cho phép cài từ nguồn này.
2. **Bật Trợ năng:** Cài đặt → Trợ năng → Auto Bang Chiến → bật.
   Nếu nút bị **mờ** (Android 13+ với app cài ngoài CH Play): mở app, bấm **MỞ CÀI ĐẶT ỨNG DỤNG**,
   chọn dấu **ba chấm** → **Cho phép cài đặt bị hạn chế**, rồi bật Trợ năng lại.
3. Mở app, bấm **BẮT ĐẦU**. Khi hộp thoại chia sẻ màn hình hiện ra, chọn **Toàn bộ màn hình**
   (không chọn "một ứng dụng", nếu không app chỉ thấy chính nó).
4. Chuyển sang game, **xoay ngang**, vào màn Bang chiến. Quay lại app để xem **Nhật ký**.
5. Muốn dừng: bấm **DỪNG** trong app (hoặc kéo thanh thông báo của app).

## Lưu ý
- Giữ game ở **nền trước** và không tắt màn hình. Nên tắt tối ưu pin cho Auto Bang Chiến.
- Mỗi lần bấm BẮT ĐẦU phải cấp lại quyền chia sẻ màn hình (Android yêu cầu).
- Gặp màn hình lạ (popup, mất mạng...) quá 60 lần liên tiếp, hoặc bấm quay lại 25 lần vẫn kẹt:
  bot tự dừng và lưu ảnh vào `Android/data/vn.autobangchien/files/debug/` để bạn gửi lại.
- Game có thể cấm công cụ tự động, tài khoản có nguy cơ bị phạt. Hãy cân nhắc trước khi dùng.
- Khoá ký trong repo (`app/debug.keystore`) là khoá debug công khai, chỉ dùng cho bản cài cá nhân.
  Để kho ở chế độ Private.

## Cấu trúc mã
`Cfg.kt` (toạ độ, ảnh mẫu) · `Frame.kt` (khung hình) · `Vision.kt` (nhận diện) ·
`BangChienEngine.kt` (kịch bản) · `BotAccessibilityService.kt` (bấm) ·
`CaptureBotService.kt` + `FrameSource.kt` (chụp màn hình) · `MainActivity.kt` (giao diện).


## Hướng dẫn cực dễ cho người mới

Trong ứng dụng có nút **❔ HƯỚNG DẪN CHO NGƯỜI MỚI**. Hướng dẫn được chia thành từng bước, có giải thích các từ như Trợ năng, chia sẻ toàn bộ màn hình và phần trăm vùng game. Bạn có thể mở lại bất cứ lúc nào.

Làm nhanh theo thứ tự:
1. Mở game, xoay ngang và vào màn Bang chiến.
2. Mở Auto Bang Chiến → **CẤP QUYỀN TRỢ NĂNG** → bật Auto Bang Chiến trong Cài đặt → quay lại app.
3. Bấm **HỒ SƠ / VÙNG GAME V4**. Nếu chưa biết chỉnh thế nào, giữ nguyên 4 số mặc định: Trái `0`, Trên `0`, Phải `100`, Dưới `100`; đặt tên hồ sơ rồi bấm **LƯU HỒ SƠ**.
4. Quay lại trang chính, bấm **BẮT ĐẦU**. Khi Android hỏi, chọn **Toàn bộ màn hình** và xác nhận.
5. Chuyển về game, giữ game ở phía trước. Muốn dừng thì quay lại app và bấm **DỪNG**.

**Giải thích phần trăm:** `0` là mép trái hoặc mép trên; `100` là mép phải hoặc mép dưới. Ví dụ `Phải = 90` nghĩa là vùng được chọn kết thúc ở 90% chiều ngang màn hình. Nếu chưa chắc, không thu hẹp vùng; cứ để 0–0–100–100.

**Lưu ý trung thực về V4.1:** hướng dẫn trong app giúp cài đặt và chạy bản nền tảng hiện tại. V4.1 chưa có thao tác kéo chọn vùng trực tiếp trên ảnh và chưa có màn hình chạm để đánh dấu từng nút; các tọa độ nút vẫn kế thừa từ V2. Vì vậy đây chưa phải trải nghiệm cấu hình hoàn chỉnh như mục tiêu V4 cuối cùng.

## V4.1 – Nền tảng hồ sơ vùng game (đang phát triển)

Bản nhánh V4.1 bắt đầu tách tọa độ thao tác khỏi kích thước màn hình:

- Thêm `BotProfile`, `ProfileManager`, `NormalizedRegion` và `CoordinateMapper`.
- Hồ sơ lưu vùng game theo tỷ lệ màn hình và lưu kích thước màn hình tham chiếu.
- `FrameSource` cắt khung hình theo vùng game đã chọn trước khi chuyển cho Vision.
- `BangChienEngine` chuyển tọa độ logic 2400x1080 sang tọa độ pixel thật có tính đến gốc vùng game.
- Nếu kích thước màn hình tham chiếu thay đổi, bot dừng thay vì tự chạy bằng tọa độ có thể sai.
- Thêm màn hình quản lý hồ sơ cơ bản trong ứng dụng.

**Giới hạn của bản triển khai nền tảng:** màn hình thiết lập hiện nhập bốn cạnh vùng game theo phần trăm. Công cụ chọn vùng bằng cách kéo trực tiếp trên ảnh chụp và trình đánh dấu từng nút chưa được triển khai trong gói này. Các điểm nút vẫn đang dùng bộ tọa độ V2 theo hệ quy chiếu bên trong vùng game. Chưa coi đây là APK V4 hoàn chỉnh hoặc đã kiểm thử trên thiết bị thật.
