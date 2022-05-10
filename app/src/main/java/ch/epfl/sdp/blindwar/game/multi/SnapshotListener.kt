package ch.epfl.sdp.blindwar.game.multi

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.multi.model.Match
import ch.epfl.sdp.blindwar.game.solo.fragments.DemoFragment
import com.google.firebase.firestore.DocumentSnapshot

object SnapshotListener {

    fun getInstance(
        snapshot: DocumentSnapshot?,
        context: Context,
        dialog: AlertDialog,
        viewFragment: View
    ): Match? {

        if (snapshot != null && snapshot.exists()) {
            val match = snapshot.toObject(Match::class.java)!!
            val nbPlayers = match.listPlayers!!.size
            if ((match.isPrivate && nbPlayers == match.maxPlayer) ||                //private match wait for all players to join
                (!match.isPrivate && nbPlayers > (0.75 * match.maxPlayer).toInt())  //public match wait for 3/4 of the max players number
            ) {
                //launch game //TODO realtime update
                dialog.hide()
                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(
                        (viewFragment.parent as ViewGroup).id,
                        DemoFragment(),
                        "DEMO"
                    )
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            } else {
                dialog.findViewById<TextView>(R.id.textView_multi_loading)?.text =
                    context.getString(R.string.multi_wait_players_nb)
                        .format(nbPlayers, match.maxPlayer)
            }
            return match
        }
        return null
    }
}