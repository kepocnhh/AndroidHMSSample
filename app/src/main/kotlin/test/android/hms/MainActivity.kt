package test.android.hms

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).also {
            it.orientation = LinearLayout.VERTICAL
        }
        val textView = TextView(this).also {
            root.addView(it)
        }
        setContentView(root)
        val context: Context = this
        textView.text = "location request..."
        CoroutineScope(Dispatchers.Main).launch {
            val message = withContext(Dispatchers.IO) {
                try {
                    val location = LocationProvider(context).getLocation()
                    "lt: ${location.latitude} / lg: ${location.longitude}"
                } catch (e: Throwable) {
                    "error: $e"
                }
            }
            println(message)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            textView.text = message
        }
    }
}
