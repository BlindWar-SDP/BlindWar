package ch.epfl.sdp.blindwar.login

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ch.epfl.sdp.blindwar.game.util.GameActivity

object PermissionHandler {
    /** Permission handling **/
    fun handle(context: Context, activity: Activity) {
        val permissionCheck =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                GameActivity.PERMISSIONS_REQUEST_RECORD_AUDIO
            )
    }
}