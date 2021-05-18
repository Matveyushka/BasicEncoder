package com.example.basicencoder.cipher

import com.example.basicencoder.utils.Alphabet
import com.example.basicencoder.utils.cyrillicLowerCaseAlphabet

val CombinedCipher = object : ICipher {
    override val alphabet: Alphabet = cyrillicLowerCaseAlphabet

    override fun encode(source: String, arguments: Map<String, Any>): String {
        val key = arguments["Keyword"] as String

        val maxKeySize = getMaxKeySize(cyrillicLowerCaseAlphabet.size)
        if (key.length > maxKeySize) {
            throw Exception("Key should be shorter than " + (maxKeySize + 1) + " symbols.")
        }

        val alphabetWithoutKeyword = cyrillicLowerCaseAlphabet
            .getAsCharArray()
            .filter { letter -> !key.toList().contains(letter) }

        var result = ""

        println(source)

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
        return source
    }

    fun getMaxKeySize(alphabetSize: Int): Int {
        for (i in 0..9) {
            if (Math.ceil((alphabetSize - i).toDouble() / 10) > 10 - i) {
                return i - 1
            }
        }
        return 9
    }
}