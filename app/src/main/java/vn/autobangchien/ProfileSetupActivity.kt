package vn.autobangchien

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.graphics.Color
import android.content.Context
import android.util.DisplayMetrics
import android.view.ViewGroup
import java.util.UUID

/** V4.1: cấu hình vùng game cố định theo phần trăm màn hình; trình chọn kéo-thả sẽ được bổ sung ở bước kế tiếp. */
class ProfileSetupActivity : Activity() {
    private val manager by lazy { ProfileManager(this) }
    private lateinit var name: EditText
    private lateinit var left: EditText
    private lateinit var top: EditText
    private lateinit var right: EditText
    private lateinit var bottom: EditText
    private var editing: BotProfile? = null
    private var displayW = 0
    private var displayH = 0

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
    private fun field(hint: String, value: String): EditText = EditText(this).apply {
        this.hint = hint; setText(value); setSingleLine(true); setTextColor(Color.WHITE); setHintTextColor(0xFF9AA7B2.toInt())
    }
    private fun row(label: String, edit: EditText): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        addView(TextView(this@ProfileSetupActivity).apply { text = label; setTextColor(0xFFB8C4CE.toInt()) })
        addView(edit, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(16, 24, 32); window.navigationBarColor = Color.rgb(16, 24, 32)
        val dm = DisplayMetrics()
        @Suppress("DEPRECATION")
        (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getRealMetrics(dm)
        displayW = maxOf(dm.widthPixels, dm.heightPixels); displayH = minOf(dm.widthPixels, dm.heightPixels)
        editing = manager.active()
        val p = editing!!
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(16), dp(16), dp(16), dp(16)); setBackgroundColor(Color.rgb(16,24,32)) }
        root.addView(TextView(this).apply { text = "THIẾT LẬP HỒ SƠ V4"; textSize = 22f; setTextColor(0xFF55D6A0.toInt()) })
        root.addView(TextView(this).apply {
            text = "Màn hình hiện tại: ${displayW} × ${displayH} px\nVùng game được lưu theo phần trăm màn hình. Hãy để điện thoại đúng hướng và mở game ở bố cục sẽ dùng khi chạy bot."
            setTextColor(0xFFB8C4CE.toInt()); setPadding(0, dp(8), 0, dp(12))
        })
        name = field("Tên hồ sơ", p.name)
        left = field("Trái (%)", (p.region.left * 100).toString())
        top = field("Trên (%)", (p.region.top * 100).toString())
        right = field("Phải (%)", (p.region.right * 100).toString())
        bottom = field("Dưới (%)", (p.region.bottom * 100).toString())
        root.addView(TextView(this).apply {
            text = "CÁCH NHẬP DỄ NHẤT
Nếu mới dùng, hãy giữ nguyên 4 giá trị mặc định 0 – 0 – 100 – 100 để lấy toàn bộ màn hình. Chỉ thay đổi khi bạn biết chắc vùng game nằm trong một phần màn hình. Các số là phần trăm: 0 = mép đầu, 100 = mép cuối. Ví dụ Phải = 90 nghĩa là kết thúc ở 90% chiều ngang màn hình.
Lưu ý: bản hiện tại chưa có công cụ kéo khung trên ảnh; bạn không cần tự đo pixel."
            setTextColor(0xFF55D6A0.toInt()); setPadding(0, dp(4), 0, dp(14))
        })
        root.addView(row("Tên cấu hình", name))
        root.addView(row("Cạnh trái – mặc định 0", left))
        root.addView(row("Cạnh trên – mặc định 0", top))
        root.addView(row("Cạnh phải – mặc định 100", right))
        root.addView(row("Cạnh dưới – mặc định 100", bottom))
        root.addView(TextView(this).apply {
            text = "Lưu ý: V4.1 hiện dùng nhập tỷ lệ để kiểm tra nền tảng vùng cố định. Công cụ chụp ảnh mẫu, kéo khung trực tiếp và chạm đánh dấu từng nút sẽ được triển khai tiếp theo."
            setTextColor(0xFFFFC857.toInt()); setPadding(0, dp(12), 0, dp(12))
        })
        root.addView(Button(this).apply { text = "LƯU HỒ SƠ"; setOnClickListener { saveProfile() } })
        root.addView(Button(this).apply { text = "CHỌN HỒ SƠ KHÁC"; setOnClickListener { chooseProfile() } })
        root.addView(Button(this).apply { text = "QUAY LẠI"; setOnClickListener { finish() } })
        val scroll = ScrollView(this); scroll.addView(root); setContentView(scroll)
    }

    private fun readPercent(edit: EditText): Float? = edit.text.toString().trim().replace(',', '.').toFloatOrNull()?.div(100f)

    private fun saveProfile() {
        val l = readPercent(left); val t = readPercent(top); val r = readPercent(right); val b = readPercent(bottom)
        if (l == null || t == null || r == null || b == null) { message("Vui lòng nhập đủ 4 cạnh bằng số phần trăm."); return }
        val region = NormalizedRegion(l, t, r, b)
        if (!region.validate()) { message("Vùng game không hợp lệ. Cần nằm trong 0–100%, rộng và cao tối thiểu 25%, cạnh phải lớn hơn cạnh trái và cạnh dưới lớn hơn cạnh trên."); return }
        val old = editing
        val profile = BotProfile(old?.id ?: UUID.randomUUID().toString(), name.text.toString().ifBlank { "Hồ sơ mới" }, region, displayW, displayH, old?.points ?: emptyMap())
        manager.save(profile, true)
        message("Đã lưu hồ sơ '${profile.name}'. Vùng game: ${region.toPixels(displayW, displayH).width} × ${region.toPixels(displayW, displayH).height} px.")
    }

    private fun chooseProfile() {
        val items = manager.all()
        AlertDialog.Builder(this).setTitle("Chọn hồ sơ")
            .setItems(items.map { it.name }.toTypedArray()) { _, which ->
                manager.select(items[which].id); editing = items[which]; recreate()
            }.setNegativeButton("Hủy", null).show()
    }

    private fun message(s: String) { AlertDialog.Builder(this).setMessage(s).setPositiveButton("Đóng", null).show() }
}
