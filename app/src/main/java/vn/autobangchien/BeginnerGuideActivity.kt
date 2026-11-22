package vn.autobangchien

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/** Hướng dẫn từng bước dành cho người lần đầu cài và sử dụng ứng dụng. */
class BeginnerGuideActivity : Activity() {
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    private fun text(value: String, size: Float = 15f, color: Int = 0xFFE6EDF3.toInt()): TextView =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            setPadding(0, dp(5), 0, dp(5))
            setLineSpacing(dp(3).toFloat(), 1f)
        }

    private fun section(root: LinearLayout, heading: String, body: String) {
        root.addView(text(heading, 18f, 0xFF55D6A0.toInt()))
        root.addView(text(body))
        root.addView(text(" ", 5f))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(16, 24, 32)
        window.navigationBarColor = Color.rgb(16, 24, 32)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(18), dp(18), dp(18))
            setBackgroundColor(Color.rgb(16, 24, 32))
        }
        root.addView(text("HƯỚNG DẪN CHO NGƯỜI MỚI", 22f, 0xFF55D6A0.toInt()))
        root.addView(text("Bạn không cần biết lập trình. Hãy làm lần lượt từng bước, không cần làm tất cả cùng lúc."))

        section(root, "BƯỚC 1 — Chuẩn bị game",
            "1. Mở game và xoay điện thoại nằm ngang.\n" +
            "2. Đưa game đến màn hình Bang chiến, nơi bạn thường muốn bot bắt đầu làm việc.\n" +
            "3. Giữ nguyên hướng màn hình và cách hiển thị này khi dùng bot. Đừng đổi độ phân giải hoặc chia đôi màn hình sau khi tạo hồ sơ.")
        section(root, "BƯỚC 2 — Bật quyền Trợ năng",
            "1. Quay về ứng dụng Auto Bang Chiến.\n" +
            "2. Bấm 'CẤP QUYỀN TRỢ NĂNG'.\n" +
            "3. Trong Cài đặt, tìm 'Auto Bang Chiến' rồi bật quyền. Nếu Android hỏi xác nhận, hãy đọc thông báo và xác nhận.\n" +
            "4. Quay lại ứng dụng. Dòng trạng thái cần hiển thị 'Trợ năng: BẬT'.\n" +
            "Nếu công tắc bị mờ, quay lại app và thử nút 'MỞ CÀI ĐẶT ỨNG DỤNG'. Trên một số máy Android 13 trở lên, cần mở dấu ba chấm và cho phép cài đặt bị hạn chế.")
        section(root, "BƯỚC 3 — Tạo hồ sơ vùng game",
            "1. Bấm 'HỒ SƠ / VÙNG GAME V4'.\n" +
            "2. Đặt tên dễ nhớ, ví dụ: 'Máy của tôi'.\n" +
            "3. V4.1 hiện yêu cầu nhập 4 cạnh theo phần trăm màn hình: Trái, Trên, Phải, Dưới. 0 là mép trái/trên; 100 là mép phải/dưới.\n" +
            "4. Ví dụ muốn dùng toàn bộ màn hình thì nhập Trái 0, Trên 0, Phải 100, Dưới 100. Nếu chưa chắc vùng game nằm ở đâu, hãy giữ giá trị mặc định này và chưa thu hẹp vùng.\n" +
            "5. Bấm 'LƯU HỒ SƠ'.")
        section(root, "BƯỚC 4 — Bắt đầu chạy",
            "1. Trở về màn hình chính của app.\n" +
            "2. Có thể nhập giới hạn số trận; để 0 nếu không giới hạn.\n" +
            "3. Bấm 'BẮT ĐẦU'. Android sẽ hỏi quyền chia sẻ màn hình. Chọn 'Toàn bộ màn hình' rồi xác nhận.\n" +
            "4. Chuyển sang game, giữ game ở phía trước và không tắt màn hình. Xem mục Nhật ký trong app để biết trạng thái.")
        section(root, "BƯỚC 5 — Dừng và xử lý khi có lỗi",
            "• Muốn dừng, quay lại app và bấm 'DỪNG'.\n" +
            "• Nếu báo sai kích thước màn hình, hãy khôi phục đúng độ phân giải/hướng màn hình lúc tạo hồ sơ. V4 chủ động dừng để tránh bấm nhầm.\n" +
            "• Nếu không chạy, kiểm tra Trợ năng đang BẬT, quyền chia sẻ màn hình đã cấp và game đang mở ở phía trước.\n" +
            "• Khi cần nhờ hỗ trợ, hãy chụp màn hình phần Nhật ký và gửi kèm tên máy, phiên bản Android.")
        section(root, "LƯU Ý QUAN TRỌNG VỀ V4.1",
            "Đây là bản nền tảng đang phát triển. Màn hình thiết lập hiện nhập tỷ lệ vùng bằng số; tính năng chạm ảnh để chọn vùng và chạm đánh dấu từng nút chưa có. Các nút thao tác vẫn dựa trên tọa độ kế thừa từ V2, nên hướng dẫn này giúp bạn cài và thao tác với bản hiện tại, không có nghĩa mọi thiết bị đã được hỗ trợ hoặc đã kiểm thử. Game có thể không cho phép tự động hóa và tài khoản có thể gặp rủi ro; bạn tự cân nhắc trước khi sử dụng.")

        root.addView(Button(this).apply { text = "ĐÃ HIỂU — QUAY LẠI"; setOnClickListener { finish() } })
        val scroll = ScrollView(this)
        scroll.addView(root, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        setContentView(scroll)
    }
}
