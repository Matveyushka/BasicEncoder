package com.example.basicencoder.cipher

import com.example.basicencoder.utils.Alphabet
import com.example.basicencoder.utils.cyrillicLowerCaseAlphabet

private fun getMaxKeySize(alphabetSize: Int): Int {
    for (i in 0..9) {
        if (Math.ceil((alphabetSize - i).toDouble() / 10) > 10 - i) {
            return i - 1
        }
    }
    return 9
}

private fun validateKey(key: String, alphabet: Alphabet) {
    val maxKeySize = getMaxKeySize(alphabet.size)
    if (key.length > maxKeySize) {
        throw Exception("Key should be shorter than " + (maxKeySize + 1) + " symbols.")
    }
}

val CombinedCipher = object : ICipher {
    override val alphabet: Alphabet = cyrillicLowerCaseAlphabet

    override fun encode(source: String, arguments: Map<String, Any>): String {
        val key = arguments["Keyword"] as String

        validateKey(key, alphabet)

        val alphabetWithoutKeyword = cyrillicLowerCaseAlphabet
            .getAsCharArray()
            .filter { letter -> !key.toList().contains(letter) }

        var result = ""

        val nums = intArrayOf(0, 9, 8, 7, 6, 5, 4, 3, 2, 1)

        for (element in source) {
            if (key.toList().contains(element)) {
                result += nums[key.indexOf(element)].toString()
            } else {
                val cIndex = alphabetWithoutKeyword.indexOf(element)
                result += ((cIndex / 10) * 10 + nums[cIndex % 10] + 10).toString()
            }
        }
        return result
    }

    override fun decode(source: String, arguments: Map<String, Any>): String {
        val key = arguments["Keyword"] as String

        validateKey(key, alphabet)

        val alphabetWithoutKeyword = cyrillicLowerCaseAlphabet
            .getAsCharArray()
            .filter { letter -> !key.toList().contains(letter) }

        var result = ""

        val nums = intArrayOf(0, 9, 8, 7, 6, 5, 4, 3, 2, 1)

        val keyNums = nums.take(key.length)

        var currentValue = 0

        for (element in source) {
            val numElement = element.toString().toInt()
            if (currentValue > 0) {
                result += alphabetWithoutKeyword[currentValue * 10 + nums.indexOf(numElement) - 10]
                currentValue = 0
            }
            else if (keyNums.contains(numElement)) {
                result += key[keyNums.indexOf(numElement)]
            } else {
                currentValue += numElement
            }
        }

        return result
    }
}