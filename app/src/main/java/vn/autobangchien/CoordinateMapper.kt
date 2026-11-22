package vn.autobangchien

/** Ánh xạ hệ tọa độ logic 2400x1080 của V2 vào vùng game đã lưu trên màn hình thật. */
class CoordinateMapper(val region: PixelRegion) {
    fun screenX(logicalX: Int): Int = region.left + (logicalX.toFloat() / Cfg.REF_W * region.width).toInt()
    fun screenY(logicalY: Int): Int = region.top + (logicalY.toFloat() / Cfg.REF_H * region.height).toInt()
    fun logicalX(screenX: Int): Int = ((screenX - region.left).toFloat() / region.width * Cfg.REF_W).toInt()
    fun logicalY(screenY: Int): Int = ((screenY - region.top).toFloat() / region.height * Cfg.REF_H).toInt()
}
