package ch.epfl.sdp.blindwar.game.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter

object QRCodeGenerator {

    fun encodeUrl(url : String): Bitmap {
        val writer = QRCodeWriter()
        val height = 512; val width = 512
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        try {
            val bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, 512, 512)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            e.printStackTrace()
        }

        return bitmap
    }
}