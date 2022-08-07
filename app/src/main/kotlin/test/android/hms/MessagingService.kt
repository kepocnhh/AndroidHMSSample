package test.android.hms

import android.os.Bundle
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage

class MessagingService : HmsMessageService() {
    companion object {
        var token: String? = null
    }

    override fun onCreate() {
        println("[MessagingService] on -> create")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("[MessagingService] on -> destroy")
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        println("[MessagingService] token \"$p0\"")
        if (p0.isNullOrEmpty()) error("Token error!")
        token = p0
    }

//    override fun onNewToken(p0: String?, p1: Bundle?) {
//        super.onNewToken(p0, p1)
//        println("[MessagingService] token \"$p0\" bundle \"$p1\"")
//    }

    override fun onMessageReceived(message: RemoteMessage?) {
        println("[MessagingService] on -> message received: " + message?.data)
    }
}
