package vn.autobangchien

import android.content.Context
import org.json.JSONObject
import kotlin.math.roundToInt

data class NormalizedRegion(val left: Float = 0f, val top: Float = 0f, val right: Float = 1f, val bottom: Float = 1f) {
    fun validate(): Boolean = left >= 0f && top >= 0f && right <= 1f && bottom <= 1f && right - left >= 0.25f && bottom - top >= 0.25f
    fun toPixels(width: Int, height: Int): PixelRegion = PixelRegion(
        (left * width).roundToInt().coerceIn(0, width - 1),
        (top * height).roundToInt().coerceIn(0, height - 1),
        (right * width).roundToInt().coerceIn(1, width),
        (bottom * height).roundToInt().coerceIn(1, height)
    )
}

data class PixelRegion(val left: Int, val top: Int, val right: Int, val bottom: Int) {
    val width: Int get() = right - left
    val height: Int get() = bottom - top
    fun valid(): Boolean = left >= 0 && top >= 0 && width > 0 && height > 0
}

data class BotProfile(
    val id: String,
    val name: String,
    val region: NormalizedRegion = NormalizedRegion(),
    val referenceWidth: Int = 0,
    val referenceHeight: Int = 0,
    val points: Map<String, Pair<Float, Float>> = emptyMap()
) {
    fun toJson(): JSONObject {
        val r = JSONObject().put("left", region.left.toDouble()).put("top", region.top.toDouble())
            .put("right", region.right.toDouble()).put("bottom", region.bottom.toDouble())
        val p = JSONObject()
        points.forEach { (key, value) -> p.put(key, JSONObject().put("x", value.first.toDouble()).put("y", value.second.toDouble())) }
        return JSONObject().put("id", id).put("name", name).put("region", r)
            .put("referenceWidth", referenceWidth).put("referenceHeight", referenceHeight).put("points", p)
    }

    companion object {
        fun fromJson(j: JSONObject): BotProfile {
            val r = j.optJSONObject("region") ?: JSONObject()
            val p = linkedMapOf<String, Pair<Float, Float>>()
            val jp = j.optJSONObject("points") ?: JSONObject()
            jp.keys().forEach { key -> jp.optJSONObject(key)?.let { p[key] = it.optDouble("x").toFloat() to it.optDouble("y").toFloat() } }
            return BotProfile(j.optString("id", "default"), j.optString("name", "Mặc định"),
                NormalizedRegion(r.optDouble("left", 0.0).toFloat(), r.optDouble("top", 0.0).toFloat(),
                    r.optDouble("right", 1.0).toFloat(), r.optDouble("bottom", 1.0).toFloat()),
                j.optInt("referenceWidth", 0), j.optInt("referenceHeight", 0), p)
        }
    }
}

class ProfileManager(context: Context) {
    private val prefs = context.getSharedPreferences("v4_profiles", Context.MODE_PRIVATE)
    private val profiles = "profiles"
    private val active = "active"

    @Synchronized fun all(): List<BotProfile> {
        val raw = prefs.getString(profiles, null)
        if (raw.isNullOrBlank()) {
            val initial = BotProfile("default", "Mặc định – toàn màn hình")
            save(initial, true)
            return listOf(initial)
        }
        val loaded = try {
            val a = org.json.JSONArray(raw)
            (0 until a.length()).mapNotNull { i -> try { BotProfile.fromJson(a.getJSONObject(i)) } catch (_: Exception) { null } }
        } catch (_: Exception) { emptyList() }
        if (loaded.isNotEmpty()) return loaded
        val initial = BotProfile("default", "Mặc định – toàn màn hình")
        save(initial, true)
        return listOf(initial)
    }

    fun active(): BotProfile = all().firstOrNull { it.id == prefs.getString(active, "default") } ?: all().first()

    @Synchronized fun save(profile: BotProfile, makeActive: Boolean = true) {
        val list = allWithoutInitialization().filterNot { it.id == profile.id }.toMutableList()
        list.add(profile)
        val a = org.json.JSONArray()
        list.forEach { a.put(it.toJson()) }
        prefs.edit().putString(profiles, a.toString()).apply()
        if (makeActive) prefs.edit().putString(active, profile.id).apply()
    }

    fun select(id: String) { if (all().any { it.id == id }) prefs.edit().putString(active, id).apply() }

    private fun allWithoutInitialization(): List<BotProfile> {
        val raw = prefs.getString(profiles, null) ?: return emptyList()
        return try { val a = org.json.JSONArray(raw); (0 until a.length()).map { BotProfile.fromJson(a.getJSONObject(it)) } } catch (_: Exception) { emptyList() }
    }
}
