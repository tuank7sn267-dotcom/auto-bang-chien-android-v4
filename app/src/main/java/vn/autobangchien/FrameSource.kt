package vn.autobangchien

import android.media.Image
import android.media.ImageReader
import java.nio.ByteOrder

/**
 * Lấy khung hình mới nhất từ ImageReader và cắt đúng vùng game của hồ sơ. Màn hình tĩnh thì không có khung hình mới,
 * khi đó trả lại khung hình gần nhất (vì màn hình không đổi).
 */
class FrameSource(private val reader: ImageReader, private val screenW: Int, private val screenH: Int, val region: PixelRegion) {
    val w: Int get() = region.width
    val h: Int get() = region.height
    val mapper = CoordinateMapper(region)
    @Volatile private var lastFrame: Frame? = null

    private fun acquire(): Image? {
        return try {
            reader.acquireLatestImage()
        } catch (e: Exception) {
            null
        }
    }

    fun grab(): Frame? {
        val img = acquire()
        if (img != null) {
            try {
                val plane = img.planes[0]
                val ps = plane.pixelStride
                val rs = plane.rowStride
                if (ps == 4) {
                    val ib = plane.buffer.duplicate().order(ByteOrder.LITTLE_ENDIAN).asIntBuffer()
                    val rowInts = rs / 4
                    val left = region.left.coerceIn(0, screenW - 1)
                    val top = region.top.coerceIn(0, screenH - 1)
                    val right = region.right.coerceIn(left + 1, screenW)
                    val bottom = region.bottom.coerceIn(top + 1, screenH)
                    val cw = right - left
                    val ch = bottom - top
                    val px = IntArray(cw * ch)
                    for (y in 0 until ch) {
                        ib.position((top + y) * rowInts + left)
                        ib.get(px, y * cw, cw)
                    }
                    // Bộ nhớ là R,G,B,A -> đổi sang ARGB
                    for (i in px.indices) {
                        val c = px[i]
                        px[i] = (c and 0xFF00FF00.toInt()) or ((c and 0xFF) shl 16) or ((c shr 16) and 0xFF)
                    }
                    lastFrame = Frame(cw, ch, px)
                }
            } catch (e: Exception) {
                BotLog.e("Lỗi đọc ảnh màn hình: " + e.message)
            } finally {
                img.close()
            }
        }
        return lastFrame
    }
}
