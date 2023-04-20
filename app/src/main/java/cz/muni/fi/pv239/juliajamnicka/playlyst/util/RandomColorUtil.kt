package cz.muni.fi.pv239.juliajamnicka.playlyst.util


import android.graphics.Color
import kotlin.random.Random

object RandomColorUtil {
    fun getRandomHexColor(): String {
        val random = Random.Default
        val color = Color.argb(255,
            random.nextInt(256), random.nextInt(256), random.nextInt(256))
        return Integer.toHexString(color)
    }

}