package ch.epfl.sdp.blindwar.game.multi

import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import ch.epfl.sdp.blindwar.R
import ch.epfl.sdp.blindwar.game.multi.model.Match
import com.google.firebase.firestore.DocumentSnapshot

object SnapshotListener {


    /**
     * Listener used during lobby ot connect every players
     *
     * @param snapshot
     * @param context
     * @param dialog
     */
    fun listenerOnLobby(
        snapshot: DocumentSnapshot?,
        context: Context,
        dialog: AlertDialog
    ): Boolean {
        if (snapshot != null && snapshot.exists()) {
            val newMatch = snapshot.toObject(Match::class.java)!!
            val nbPlayers = newMatch.listPlayers!!.size
            if ((newMatch.isPrivate && nbPlayers == newMatch.maxPlayer) ||                //private match wait for all players to join
                (!newMatch.isPrivate && nbPlayers > (0.75 * newMatch.maxPlayer).toInt())  //public match wait for 3/4 of the max players number
            ) {
                dialog.hide()
                return true
            } else {
                dialog.findViewById<TextView>(R.id.textView_multi_loading)?.text =
                    context.getString(R.string.multi_wait_players_nb, nbPlayers, newMatch.maxPlayer)
            }
        }
        return false
    }

    /**
     * Listener used to update the game state in multiplayer game
     *
     */
    fun listenerOnGame() {

    }
}