package ch.epfl.sdp.blindwar.domain.game

object GameHelper {

    private const val vocalCostMax = 5
    private const val keyboardCostMax = 3

    /**
     *
     * Check if the typed or speech recognized title is the good one
     *
     * @param answerString
     * @param title
     * @param isVocal
     * @return Boolean
     */
    fun isTheCorrectTitle(answerString: String, title: String, isVocal: Boolean): Boolean {
        return if (isVocal) levensteinDistance(
            answerString.lowercase().trim(),
            title.lowercase().trim()
        ) < vocalCostMax
        else levensteinDistance(answerString.lowercase(), title.lowercase()) < keyboardCostMax
    }

    /**
     * Boolean to Int
     *
     * @return true = 1 / false = 0
     */
    private fun Boolean.toInt() = if (this) 1 else 0


    /**
     * Levenstein function to calculate distance between 2 strings
     *
     * @param x
     * @param y
     * @return the cost to go from x to y
     */
    private fun levensteinDistance(x: String, y: String): Int {
        val dp = Array(x.length + 1) { IntArray(y.length + 1) }
        for (i in 0..x.length) {
            for (j in 0..y.length) {
                when {
                    i == 0 -> dp[i][j] = j
                    j == 0 -> dp[i][j] = i
                    else ->
                        dp[i][j] = minOf(
                            dp[i - 1][j - 1]
                                    + (x[i - 1] != y[j - 1]).toInt(),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1
                        )
                }
            }
        }
        return dp[x.length][y.length]
    }
}