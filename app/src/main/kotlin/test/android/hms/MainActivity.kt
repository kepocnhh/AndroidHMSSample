package test.android.hms

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.huawei.hms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.widget.Button
import com.huawei.agconnect.AGConnectOptionsBuilder
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.api.ConnectionResult
import com.huawei.hms.api.HuaweiApiAvailability
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.LocationAvailability
import com.huawei.hms.location.LocationCallback
import com.huawei.hms.location.LocationRequest
import com.huawei.hms.location.LocationResult
import com.huawei.hms.push.HmsMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resumeWithException

class MainActivity : Activity() {
    companion object {
        private fun getToken(timeout: Long): String {
            val now = System.currentTimeMillis()
            while (true) {
                if (System.currentTimeMillis() - now > timeout) error("Timeout error!")
                val token = MessagingService.token
                if (!token.isNullOrEmpty()) return token
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context: Context = this
        val root = LinearLayout(this).also {
            it.orientation = LinearLayout.VERTICAL
        }
        setContentView(root)
//        HmsMessaging.getInstance(context).isAutoInitEnabled = false
//        HmsMessaging.getInstance(context).isAutoInitEnabled = true
//        startService(Intent(context, MessagingService::class.java))
        /*
        val isGranted = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (!isGranted) {
            Toast.makeText(context, "Permission error!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        Button(this).also {
            it.text = "to market"
            it.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("appmarket://details?id=com.huawei.hwid")
                    )
                )
            }
            root.addView(it)
        }
//        return
        val code = HuaweiApiAvailability.getInstance().isHuaweiMobileServicesAvailable(context)
        check(code == ConnectionResult.SUCCESS)
        val info = packageManager.getPackageInfo("com.huawei.hwid", PackageManager.GET_PERMISSIONS or PackageManager.GET_PROVIDERS)
        TextView(this).also {
//            val expected = "android.permission.ACCESS_COARSE_LOCATION"
//            val expected = "android.permission.ACCESS_FINE_LOCATION"
//            val (index, title) = info.requestedPermissions.mapIndexed { index, title ->
//                index to title
//            }.firstOrNull { (index, title) ->
//                val result = (info.requestedPermissionsFlags[index] and PackageInfo.REQUESTED_PERMISSION_GRANTED) == PackageInfo.REQUESTED_PERMISSION_GRANTED
//                println("$title: $result")
//                title == expected
//            }!!
//            val result = (info.requestedPermissionsFlags[index] and PackageInfo.REQUESTED_PERMISSION_GRANTED) == PackageInfo.REQUESTED_PERMISSION_GRANTED
//            val postfix = "$title: $result"
            val postfix = info.requestedPermissions.mapIndexed { index, title ->
                val result = (info.requestedPermissionsFlags[index] and PackageInfo.REQUESTED_PERMISSION_GRANTED) == PackageInfo.REQUESTED_PERMISSION_GRANTED
                println("$title: $result")
                title to result
            }.filter { (_, result) -> result }.filter { (title, _) ->
//                title.startsWith("android.permission.")
                title.contains("location", ignoreCase = true)
            }.sortedBy { (title, _) -> title }.joinToString(separator = "\n") { (title, _) -> title }
            it.text = "version: ${info.versionName}\n$postfix"
            root.addView(it)
        }
        val lTextView = TextView(this).also {
            root.addView(it)
        }
        lTextView.text = "location request..."
        CoroutineScope(Dispatchers.Main).launch {
            val message = withContext(Dispatchers.IO) {
                try {
                    val coordinate = LocationProvider(context).getCoordinate()
                    "$coordinate"
                } catch (e: Throwable) {
                    "error: $e"
                }
            }
            println(message)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            lTextView.text = message
        }
        */
        val appId = AGConnectOptionsBuilder().build(context).getString("client/app_id")
//        val appId = BuildConfig.APPLICATION_ID
        TextView(this).also {
            it.text = "HMS App id: $appId"
            root.addView(it)
        }
        val id = HmsInstanceId.getInstance(context)
        val pTextView = TextView(this).also {
            root.addView(it)
        }
        val scope = "HCM"
        pTextView.text = "token request..."
        CoroutineScope(Dispatchers.Main).launch {
            val token = withContext(Dispatchers.IO) {
//                delay(10_000)
                try {
                    withTimeout(10_000) {
                        MessagingService.token = null
                        id.deleteToken(appId, scope)
                    }
                    "token delete success"
                } catch (e: Throwable) {
                    "token delete error: $e"
                }
                try {
                    val tmp = withTimeout(10_000) {
                        id.getToken(appId, scope)
                    }
                    if (tmp.isNullOrEmpty()) {
                        getToken(timeout = 10_000)
                    } else {
                        tmp
                    }
                } catch (e: Throwable) {
                    "token error: $e"
                }
            }
            println(token)
            Toast.makeText(context, token, Toast.LENGTH_SHORT).show()
            pTextView.text = token
        }
    }
}
