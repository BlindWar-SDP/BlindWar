package ch.epfl.sdp.blindwar.game

import android.util.Log
import kotlin.math.absoluteValue

object GameHelper {

    private const val keyboardRatio = 0.9
    private const val vocalRatio = 0.75
    private const val wordEpsilon = 50

    /**
     *
     * TODO : upgrade it with lemmatisation
     * Check if the typed or speech recognized title is the good one
     *
     * @param answerString
     * @param title
     * @param isVocal
     * @return Boolean
     */
    fun isTheCorrectTitle(answerString: String, title: String, isVocal: Boolean): Boolean {
        var counter = 0.0
        val wordsTitle = title.uppercase().split(" ")
        for (word in wordsTitle) {
            for (answer in answerString.uppercase().split(" ")) {
                if (word.compareTo(answer).absoluteValue < wordEpsilon)
                    counter++
            }
        }
        counter /= wordsTitle.size
        if (counter > 1) return false
        if (isVocal) return counter >= vocalRatio
        return counter >= keyboardRatio
    }
}