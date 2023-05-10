package cz.muni.fi.pv239.juliajamnicka.playlyst.util

import android.graphics.*
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

fun Bitmap.isLight(skipPixel: Int = 10): Boolean {
    var r = 0
    var g = 0
    var b = 0
    val height: Int = height
    val width: Int = width

    var n = 0
    val pixels = IntArray(width * height)
    this.getPixels(pixels, 0, width, 0, 0, width, height)

    for (i in pixels.indices step skipPixel) {
        val color = pixels[i]
        r += Color.red(color)
        g += Color.green(color)
        b += Color.blue(color)
        n++
    }

    val lightThreshold = 255 / 2

    return (r + b + g) / (n * 3) > lightThreshold
}