package cz.muni.fi.pv239.juliajamnicka.playlyst.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.Base64
import java.io.ByteArrayOutputStream

fun String.capitalizeFirstLetter(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}

 fun Bitmap.encodeToBase64String(): String {
     val outputStream = ByteArrayOutputStream()
     this.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
     val output = outputStream.toByteArray()
     return Base64.encodeToString(output, Base64.NO_WRAP)
}

fun Bitmap.getResizedBitmap(newWidth: Int, newHeight: Int): Bitmap {
    val resizedBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
    val scaleX = newWidth / width.toFloat()
    val scaleY = newHeight / height.toFloat()
    val pivotX = 0f
    val pivotY = 0f
    val scaleMatrix = Matrix()
    scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY)
    val canvas = Canvas(resizedBitmap)
    canvas.setMatrix(scaleMatrix)
    canvas.drawBitmap(this, 0.toFloat(), 0.toFloat(), Paint(Paint.FILTER_BITMAP_FLAG))

    return resizedBitmap
}