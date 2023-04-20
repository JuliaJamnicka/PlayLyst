package cz.muni.fi.pv239.juliajamnicka.playlyst.util

fun String.capitalizeFirstLetter(): String {
    return this.lowercase().replaceFirstChar { it.uppercase() }
}