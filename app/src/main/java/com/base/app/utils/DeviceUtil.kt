package com.base.app.utils

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.base.app.App
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*
import kotlin.collections.ArrayList


object DeviceUtil {

    fun getDeviceCode(): String {
        try {
            if (getIMEI(App.instance).isNotEmpty()) return getIMEI(App.instance)
            return Settings.System.getString(App.instance.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            return "null"
        }
    }

    private fun getIMEI(context: Context): String {
        try {
            val tm =
                context.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tm.imei
            } else {
                tm.deviceId
            }
            return ""
        } catch (e: Exception) {
            return ""
        }
    }

    fun getIp(context: Context): String {
        val conMann =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mobileNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        val wifiNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (mobileNetworkInfo.isConnected) {
            return getLocalIpAddress()
        } else if (wifiNetworkInfo.isConnected) {
            val wifiManager =
                context.getSystemService(Context.WIFI_SERVICE) as WifiManager?
            val wifiInfo = wifiManager!!.connectionInfo
            val ipAddress = wifiInfo.ipAddress
            return intToIp(ipAddress)
        }
        return "null"
    }

    private fun getLocalIpAddress(): String {
        try {
            val nilist: ArrayList<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (ni in nilist) {
                val ialist: ArrayList<InetAddress> = Collections.list(ni.inetAddresses)
                for (address in ialist) {
                    if (!address.isLoopbackAddress) {
                        return address.hostAddress
                    }
                }
            }
        } catch (ex: SocketException) {
        }
        return "null"
    }

    private fun intToIp(ipInt: Int): String {
        val sb = StringBuilder()
        sb.append(ipInt and 0xFF).append(".")
        sb.append(ipInt shr 8 and 0xFF).append(".")
        sb.append(ipInt shr 16 and 0xFF).append(".")
        sb.append(ipInt shr 24 and 0xFF)
        return sb.toString()
    }
}