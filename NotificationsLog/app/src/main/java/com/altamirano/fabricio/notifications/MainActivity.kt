package com.altamirano.fabricio.notifications

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.altamirano.services.NotificationService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.onBoard.setAdapterDefault(AppLogic.getOnBoardItems())
        this.onBoard.setEndButton("Empezar") {
            this.openIntent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        }

        if(hasPermisionNotification(this)){
            this.startActivity(Intent(this,NotificationsLogActivity::class.java))
            this.finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

       if(!hasPermisionNotification(this)){
           Toast.makeText(this,"No se ha habilitado el permiso",Toast.LENGTH_LONG).show()
       }else{
           this.startActivity(Intent(this,NotificationsLogActivity::class.java))
           this.finish()
       }
    }

    fun hasPermisionNotification(context: Activity): Boolean {
        val cn = ComponentName(context, NotificationService::class.java)
        val flat: String? = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        )
        return flat != null && flat.contains(cn.flattenToString())
    }

    private fun openIntent(action: String) {
        try {
            val intent = Intent(action)
            startActivityForResult(intent,200)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show()
        }
    }
}