package com.example.basicencoder.cipher

import android.widget.Toast
import com.example.basicencoder.utils.Alphabet
import java.lang.Exception

val Playfair = object : ICipher {
    override fun encode(source: String, arguments: Map<String, Any>): String {
        val key: String = arguments["Key"] as String
        if (!alphabetSourceString.toList().containsAll((source + key).toList() ))
        {
            throw Exception("Only lowcase cyrillic symbols")
        }

        var result = ""
        for (i in source.indices step 2) {
            if (i != source.length - 1) {
                val encodedPair = encodeLetterPair(source[i], source[i+1], key)
                result += encodedPair.first
                result += encodedPair.second
            } else {
                result += source[i]
            }
        }
        return result
    }

    override fun decode(source: String, arguments: Map<String, Any>): String {
        val key: String = arguments["Key"] as String
        if (!alphabetSourceString.toList().containsAll((source + key).toList() ))
        {
            throw Exception("Only lowcase cyrillic symbols")
        }

        var result = ""
        for (i in source.indices step 2) {
            if (i != source.length - 1) {
                val encodedPair = decodeLetterPair(source[i], source[i+1], key)
                result += encodedPair.first
                result += encodedPair.second
            } else {
                result += source[i]
            }
        }
        return result
    }

    private val alphabetSourceString = "абвгдежзийклмнопрстуфхцчшщъыьэюя"
    private val rectangularCyrillicAlphabet = Alphabet(alphabetSourceString)
    private val extraLetter = 'ё'
    private val alphabetWidth = 8
    private val alphabetHeight = 4

    private fun getLetterPosition(letter: Char, alphabet: Alphabet): Pair<Int, Int> {
        val letterIndex = alphabet.getLetterNumber(letter) ?: 0
        val x = letterIndex % alphabetWidth
        val y = letterIndex / alphabetWidth
        return x to y
    }

    private fun encodeLetterPair(first: Char, second: Char, key: String): Pair<Char, Char> {
        val alphabet = rectangularCyrillicAlphabet.getKeyedAlphabet(key) ?: rectangularCyrillicAlphabet

        val firstLetterPosition = getLetterPosition(first, alphabet)
        val secondLetterPosition = getLetterPosition(second, alphabet)

        return if (firstLetterPosition.first != secondLetterPosition.first && firstLetterPosition.second != secondLetterPosition.second) {
            alphabet[secondLetterPosition.first + firstLetterPosition.second * alphabetWidth] to
                    alphabet[firstLetterPosition.first + secondLetterPosition.second * alphabetWidth]
        } else if (firstLetterPosition.first == secondLetterPosition.first && firstLetterPosition.second != secondLetterPosition.second) {
            alphabet[firstLetterPosition.first + (firstLetterPosition.second + 1 + alphabetHeight) % alphabetHeight * alphabetWidth] to
                    alphabet[secondLetterPosition.first + (secondLetterPosition.second + 1 + alphabetHeight) % alphabetHeight * alphabetWidth]
        } else if (firstLetterPosition.first != secondLetterPosition.first && firstLetterPosition.second == secondLetterPosition.second) {
            alphabet[(firstLetterPosition.first + 1 + alphabetWidth) % alphabetWidth + firstLetterPosition.second * alphabetWidth] to
                    alphabet[(secondLetterPosition.first + 1 + alphabetWidth) % alphabetWidth + secondLetterPosition.second * alphabetWidth]
        } else {
            first to extraLetter
        }
    }

    private fun decodeLetterPair(first: Char, second: Char, key: String): Pair<Char, Char> {
        val alphabet = rectangularCyrillicAlphabet.getKeyedAlphabet(key) ?: rectangularCyrillicAlphabet

        if (second == extraLetter) {
            return first to first
        }

        val firstLetterPosition = getLetterPosition(first, alphabet)
        val secondLetterPosition = getLetterPosition(second, alphabet)

        return if (firstLetterPosition.first != secondLetterPosition.first && firstLetterPosition.second != secondLetterPosition.second) {
            alphabet[secondLetterPosition.first + firstLetterPosition.second * alphabetWidth] to
                    alphabet[firstLetterPosition.first + secondLetterPosition.second * alphabetWidth]
        } else if (firstLetterPosition.first == secondLetterPosition.first && firstLetterPosition.second != secondLetterPosition.second) {
            alphabet[firstLetterPosition.first + (firstLetterPosition.second - 1 + alphabetHeight) % alphabetHeight * alphabetWidth] to
                    alphabet[secondLetterPosition.first + (secondLetterPosition.second - 1 + alphabetHeight) % alphabetHeight * alphabetWidth]
        } else if (firstLetterPosition.first != secondLetterPosition.first && firstLetterPosition.second == secondLetterPosition.second) {
            alphabet[(firstLetterPosition.first - 1 + alphabetWidth) % alphabetWidth + firstLetterPosition.second * alphabetWidth] to
                    alphabet[(secondLetterPosition.first - 1 + alphabetWidth) % alphabetWidth + secondLetterPosition.second * alphabetWidth]
        } else {
            first to extraLetter
        }
    }
}