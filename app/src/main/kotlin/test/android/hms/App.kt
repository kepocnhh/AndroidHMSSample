package test.android.hms

import android.app.Application
import android.content.Context
import com.huawei.agconnect.AGConnectOptionsBuilder
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.push.HmsMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class App : Application() {
    companion object {
        private fun log(message: String) {
            println("[App] $message")
        }
    }

    override fun onCreate() {
        super.onCreate()
        val context: Context = this
//        val appId = AGConnectOptionsBuilder().build(context).getString("client/app_id")
//        log("app id: $appId")
//        HmsMessaging.getInstance(context).isAutoInitEnabled = false
//        HmsMessaging.getInstance(context).isAutoInitEnabled = true
        /*
        val scope = "HCM"
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val token = withContext(Dispatchers.IO) {
                    withTimeout(10_000) {
                        HmsInstanceId.getInstance(context).getToken(appId, scope)
                    }
                }
                log("token: $token")
            } catch (e: Throwable) {
                log("get token error: $e")
            }
        }
        */
    }
}
