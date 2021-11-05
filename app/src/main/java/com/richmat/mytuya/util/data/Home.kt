package com.richmat.mytuya.util.data

import android.os.Parcelable
import com.richmat.mytuya.ui.newHome.DevicePage
import kotlinx.parcelize.Parcelize

const val TAG = "colin"

//device id，可能在别的地方有用，最后实在无用的话，可以放到DeviceListener
//TODO 这个可以放在，各自页面或者viewmodel的里边
const val ZIGBEE_GATE = "6c80187158c8e57d0dcu4n"
const val BODY_SENSOR = "6c270c207031f293a8byl7"
const val Bulb_Light = "6cbbc8ea04e98834d9vcpd"

/**
 * 因为数字转换失败，暂时只能通过map获取
 */
//data class BodySensorDsp(val hasPerson: String?, val power: Int?)

sealed class DeviceListener(val deviceId: String, val route: String) {
    object BodySensorListener :
        DeviceListener(deviceId = BODY_SENSOR, route = DevicePage.BodySensor.route)

    object GateListener : DeviceListener(deviceId = ZIGBEE_GATE, route = DevicePage.Gateway.route)
    object BulbLightListener :
        DeviceListener(deviceId = Bulb_Light, route = DevicePage.BulbLight.route)
}

//sealed class DeviceListener(val route: String) {
//    data class BodySensorListener(
//        val deviceId: String = BODY_SENSOR,
//        val route1: String = DevicePage.BodySensor.route,
//    ) : DeviceListener(route = route1)
//
//    object GateListener()
//}

/**
 * deviceId仅作移除用，一些地方可能为空
 */
@Parcelize
data class DevResultMassage(
    val name: String, val route: String,
    val deviceId: String = "",
    val argument: String? = "",
    val iconUri: String? = "",
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (other is DevResultMassage) {
            return deviceId == other.deviceId
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return deviceId.hashCode()
    }
}

fun emptyDevResultMassage() = DevResultMassage(name = "", route = "")
//{
//    //暂时为了统一只能，都发数据了，不然每个需要用的地方都要判断
//    fun getNavigationArgumentRoute() = if (argument != null) {
//        "$route/$argument"
//    } else {
//        "$route/${""}"
//    }
//}

/**
 * 获取设备路由,不需要了，密封类使用val后，可直接获取
 */
//fun DeviceListener.getPageRoute(): String = when (this) {
//    is DeviceListener.BodySensorListener -> this.route
//}
