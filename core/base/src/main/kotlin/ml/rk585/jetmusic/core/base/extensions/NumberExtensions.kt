package ml.rk585.jetmusic.core.base.extensions

import android.icu.text.CompactDecimalFormat
import android.os.Build
import java.text.DecimalFormat
import java.util.Locale
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

fun Number.toCompactView(): String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return CompactDecimalFormat.getInstance(
            Locale.getDefault(),
            CompactDecimalFormat.CompactStyle.SHORT
        ).format(toInt())
    }

    val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
    val numValue: Long = toLong()
    val value = floor(log10(numValue.toDouble())).toInt()
    val base = value / 3
    return if (value >= 3 && base < suffix.size) {
        DecimalFormat("#0.0").format(
            numValue / 10.0.pow((base * 3).toDouble())
        ) + suffix[base]
    } else {
        DecimalFormat("#,##0").format(numValue)
    }
}
