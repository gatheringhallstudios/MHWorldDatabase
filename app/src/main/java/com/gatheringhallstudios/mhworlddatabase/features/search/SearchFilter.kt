package com.gatheringhallstudios.mhworlddatabase.features.search

fun normalize(str: String): String {
    // todo: support additional characters like greek ones, support locale transformations
    val newString = StringBuilder()
    for (i in 0 until str.length) {
        val char = str[i]
        newString.append(when(char) {
            'α' -> "alpha"
            'β' -> "beta"
            'γ' -> "gamma"
            'á' -> 'a'
            else -> char.toLowerCase()
        })
    }

    return newString.toString()
}

/**
 * Class that accepts a filter string and matches it against test string.
 */
class SearchFilter(filterString: String) {
    private val words = normalize(filterString).split(' ')

    /**
     * Checks if the filter string is a match for the text string after normalization.
     */
    fun matches(test: String?): Boolean {
        test ?: return false
        return matchesNormalized(normalize(test))
    }

    /**
     * Checks if the filter string is a match for the test string without normalization.
     * Use this if comparing to an already normalized test string.
     */
    fun matchesNormalized(test: String?): Boolean {
        test ?: return false

        for (word in words) {
            if (!test.contains(word)) {
                return false
            }
        }

        return true
    }
}