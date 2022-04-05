package ch.epfl.sdp.blindwar.user

data class Match(
    var user1: User? = null,
    var user2: User? = null,
    var win1: Int = 0,
    var win2: Int = 0
    //TODO add playlist chosen and maybe type of game
)