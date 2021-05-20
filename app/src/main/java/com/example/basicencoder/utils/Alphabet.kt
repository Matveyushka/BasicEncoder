package com.example.basicencoder.utils

import kotlin.math.ceil

class Alphabet(alphabet: String, val name: String) {
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
        return Alphabet(key + this.letters.filter { char -> !key.contains(char) }.joinToString(""), name)
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

    fun isStringInAlphabet(source: String) : Boolean = source.all { symbol -> isLetterInAlphabet(symbol)}

    fun combine(alphabetPart: String) : Alphabet = Alphabet(letters.joinToString("") + alphabetPart, name)

    fun combine(alphabet: Alphabet) : Alphabet = Alphabet(letters.joinToString("") + alphabet.letters, name)

    fun nameIt(newName: String): Alphabet = Alphabet(letters.joinToString("") , newName)
}

val englishLowerCaseAlphabet = Alphabet("abcdefghijklmnopqrstuvwxyz", "English lower case")

val englishUpperCaseAlphabet = Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "English UPPER case")

val cyrillicLowerCaseAlphabet = Alphabet("абвгдеёжзийклмнопрстуфхцчшщъыьэюя", "Cyrillic lower case")

val cyrillicUpperCaseAlphabet = Alphabet("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ", "Cyrillic UPPER case")

val byteAlphabet = Alphabet(" йцукеёнгшщзхфывапролджэячсмитьбюЙЦУКЕЁНГШЩЗХФЫВАПРОЛДЖЭЯЧСМИТЬБЮqwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890@#₽_&-+()/*\"':;!?,.~`|•√π÷×¶∆£\$¢€¥^°={}\\%©®⁜⁛™✓[]<>ↀↈαβγδεζηΘθΛλμπρστφχΨψΩω⇒→⊃⇔∧∨¬∀∃∅∈∉⊆⊂⊇⊃∪⋂↦ℕℤℚℝℂ≈≤≥∝√∞⊲×⊕⊗∫∑∏↕↹↺↻ᚠᚢᚣᛊᚺᛒᛉᚤᚦᛋᚨᚬᚭᚮᚱᚳᚴ", "Byte alphabet")

val numbersAlphabet = Alphabet("0123456789", "Numbers")

val commonAlphabet = englishLowerCaseAlphabet
    .combine(englishUpperCaseAlphabet)
    .combine(cyrillicLowerCaseAlphabet)
    .combine(cyrillicUpperCaseAlphabet)
    .combine(numbersAlphabet)

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

fun List<Alphabet>.isSymbolInAlphabets(symbol: Char): Boolean {
    return this.any { alphabet -> alphabet.isLetterInAlphabet(symbol) }
}

fun List<Alphabet>.isStringInAlphabets(source: String): Boolean {
    return this.any { alphabet -> alphabet.isStringInAlphabet(source) }
}