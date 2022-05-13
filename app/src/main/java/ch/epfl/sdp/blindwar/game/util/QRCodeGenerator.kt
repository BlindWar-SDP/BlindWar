package ch.epfl.sdp.blindwar.game.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

object QRCodeGenerator {
    const val size = 512

    /**
     * Create a QR code from a string (URL)
     *
     * @param url
     * @return Bitmap
     */
    fun encodeUrl(url: String): Bitmap {
        val writer = QRCodeWriter()
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        try {
            val bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, size, size)
            for (x in 0 until size) {
                for (y in 0 until size) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
        return bitmap
    }
}