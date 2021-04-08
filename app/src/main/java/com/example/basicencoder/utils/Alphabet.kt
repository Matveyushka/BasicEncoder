package com.example.basicencoder.utils

import kotlin.math.ceil

class Alphabet(alphabet: String) {
    private var letters: Array<Char> = alphabet.toCharArray().distinct().toTypedArray()

    var size: Int = letters.size
        private set

    operator fun get(index: Int) : Char {
        return letters[index]
    }

    fun getAsCharArray() : Array<Char> {
        return this.letters
    }

    fun getKeyedAlphabet(key: String) : Alphabet? {
        if (!this.letters.toList().containsAll(key.toList())) {
            return null
        }
        return Alphabet(key + this.letters.filter { char -> !key.contains(char) }.joinToString(""))
    }

    fun getLetterNumber(letter: Char) : Int? {
        return if (letter in letters) {
            letters.indexOf(letter)
        } else {
            null
        }
    }

    fun getWithOffset(index: Int, offset: Int) : Char {
        val positiveOffset = if (offset >= 0) {
            offset
        } else {
            (offset + size * ceil(-offset.toDouble() / size)).toInt()
        }

        return letters[(index + positiveOffset) % size]
    }

    fun getWithOffset(letter: Char, offset: Int) : Char? {
        val letterIndex = getLetterNumber(letter)

        return if (letterIndex != null) {
            getWithOffset(letterIndex, offset)
        } else {
            null
        }
    }

    fun isLetterInAlphabet(letter: Char) : Boolean = letters.contains(letter)
}

val englishLowerCaseAlphabet = Alphabet("abcdefghijklmnopqrstuvwxyz")

val englishUpperCaseAlphabet = Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ")

val cyrillicLowerCaseAlphabet = Alphabet("абвгдеёжзийклмнопрстуфхцчшщъыьэюя")

val cyrillicUpperCaseAlphabet = Alphabet("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ")

val numbersAlphabet = Alphabet("0123456789")

val standardAlphabets = listOf(
        englishLowerCaseAlphabet,
        englishUpperCaseAlphabet,
        cyrillicLowerCaseAlphabet,
        cyrillicUpperCaseAlphabet,
        numbersAlphabet
)

fun getSourceAlphabet(letter: Char, alphabets: List<Alphabet> ) : Alphabet? {
    val validAlphabets = alphabets.filter { alphabet -> alphabet.isLetterInAlphabet(letter) }
    return if (validAlphabets.count() > 0) {
        validAlphabets[0]
    } else {
        null
    }
}