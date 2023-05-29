package cz.muni.fi.pv239.juliajamnicka.playlyst.api.response

data class AvailableDevicesResponse(
    val devices: List<Device>
) {
    data class Device(
        val id: String,
        val is_active: Boolean,
        val is_restricted: Boolean
    )
}
