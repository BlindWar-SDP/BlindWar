package ch.epfl.sdp.blindwar.game.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.database.MatchDatabase
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object DynamicLinkHelper {
    private var dialog: AlertDialog? = null

    /**
     * Create a long dynamic link
     *
     * @param matchUID
     * @return
     */
    private fun createLongDynamicLink(matchUID: String): DynamicLink {
        return Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://blindwar.ch/game?uid=$matchUID")
            domainUriPrefix = "https://blindwar.page.link"
            // Open links with this app on Android
            androidParameters("ch.epfl.sdp.blindwar") { }
            socialMetaTagParameters {
                title = "Join me to play !"
                imageUrl =
                    Uri.parse("https://github.com/BlindWar-SDP/BlindWar/wiki/img/logo.png")
            }
        }
    }

    /**
     * Create a short dynamic link and display it when ready
     *
     * @param matchUID
     */
    private fun createShortDynamicLink(matchUID: String, context: Context) {
        Firebase.dynamicLinks.shortLinkAsync(ShortDynamicLink.Suffix.SHORT) {
            link = Uri.parse("https://blindwar.ch/game?uid=$matchUID")
            domainUriPrefix = "https://blindwar.page.link"
            // Open links with this app on Android
            androidParameters("ch.epfl.sdp.blindwar") { }
        }.addOnSuccessListener { (shortLink, _) ->
            if (shortLink != null) {
                dialog?.findViewById<TextView>(R.id.textView_dynamic_link)
                    ?.setOnClickListener {
                        createShareIntent(
                            shortLink,
                            context
                        )
                    }
                //no need to modify QR as the user don't really see the uri -> it would be too resourceful
            }
        }.addOnFailureListener {
            it.printStackTrace()
        }
    }

    /**
     * Create and run the Share intent to share dynamic link
     *
     * @param uri
     * @param context
     */
    private fun createShareIntent(uri: Uri, context: Context) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, uri.toString())
        context.startActivity(
            Intent.createChooser(
                shareIntent,
                context.getString(R.string.share_title)
            )
        )
    }

    /**
     * Display progressDialog cancelable for any messages
     *
     * @param message
     */
    fun setDynamicLinkDialog(
        message: String,
        matchUID: String,
        context: Context
    ): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setPositiveButton(
            context.getString(R.string.cancel_btn)
        ) { it, _ -> it.cancel() }
        builder.setOnCancelListener {
            Toast.makeText(
                context,
                context.getString(R.string.toast_canceled_match_creation),
                Toast.LENGTH_SHORT
            ).show()
            MatchDatabase.removeMatch(matchUID, Firebase.firestore)
        }
        val view = View.inflate(context, R.layout.fragment_dialog_loading_creation, null)

        //setup dynamic link
        val dynamicLink = createLongDynamicLink(matchUID)
        view.findViewById<TextView>(R.id.textView_dynamic_link)
            .setOnClickListener {
                createShareIntent(
                    dynamicLink.uri,
                    context
                )
            }
        createShortDynamicLink(matchUID, context)
        //setup QR code
        val qrCode = view.findViewById<ImageView>(R.id.QR_code)
        qrCode.setImageBitmap(QRCodeGenerator.encodeUrl(dynamicLink.uri.toString()))
        val qrButton = view.findViewById<Button>(R.id.show_qr_button)
        qrButton.setOnClickListener {
            qrCode.visibility = if (qrCode.isVisible) View.GONE
            else View.VISIBLE
            qrButton.text = context.getString(
                if (qrCode.isVisible) R.string.hide_qr else R.string.show_qr
            )
        }
        builder.setView(view)
        (view.findViewById<TextView>(R.id.textView_multi_loading)).text = message
        dialog = builder.create()
        dialog!!.setCanceledOnTouchOutside(false)
        return dialog!!
    }
}