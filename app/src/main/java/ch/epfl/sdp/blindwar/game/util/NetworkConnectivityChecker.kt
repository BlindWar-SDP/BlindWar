package ch.epfl.sdp.blindwar.game.util

object NetworkConnectivityChecker {
    /**
     * Check if internet is working via one ping
     *
     * @return
     */
    fun isOnline(): Boolean {
        return try {
            Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8").waitFor() == 0
        } catch (_: Exception) {
            false
        }
    }
}