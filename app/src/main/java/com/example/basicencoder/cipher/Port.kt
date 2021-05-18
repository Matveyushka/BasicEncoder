package com.example.basicencoder.cipher

import com.example.basicencoder.utils.Alphabet
import com.example.basicencoder.utils.englishLowerCaseAlphabet
import kotlin.math.pow

fun generateRandomSquare(sideSize: Int, key: String) : Array<Array<Int>> {
    val size = sideSize * sideSize

    val keyValue = key.repeat(size).fold(0) { ac, el -> ac + el.toInt() }

    var result : MutableList<Int> = mutableListOf()

    for (i in 0 until size) {
        result = (result + mutableListOf(i)).toMutableList()
    }

    for (i in 0 until size * 6) {
        if (i % 2 == 0) {
            val firstIndex = keyValue / (i + 2) % size
            val secondIndex = keyValue / (i + 3) % size

            val temp = result[firstIndex]
            result[firstIndex] = result[secondIndex]
            result[secondIndex] = temp
        }
    }

    var square = Array(sideSize) { x -> Array(sideSize) { y -> result[x * sideSize + y] } }

    return square
}

fun completeToSuitableLength(source: Int, length: Int): String {
    var processingString = source.toString()
    while (processingString.length < length) {
        processingString = "0$processingString"
    }
    return processingString
}

val Port = object : ICipher {
    override val alphabet: Alphabet = englishLowerCaseAlphabet.combine(" ")

    override fun encode(source: String, arguments: Map<String, Any>): String {
        val key = arguments["Keyword"] as String

        val charCipherLength = (alphabet.size * alphabet.size).toString().length

        val square = generateRandomSquare(alphabet.size, key)

        var result = ""

        for (i in source.indices step 2) {
            if (i != source.length - 1) {
                result += completeToSuitableLength(
                    square
                            [alphabet.getLetterNumber(source[i])!!]
                            [alphabet.getLetterNumber(source[i + 1])!!],
                    charCipherLength
                )
            } else {
                result += completeToSuitableLength(
                    square
                            [alphabet.getLetterNumber(source[i])!!]
                            [alphabet.getLetterNumber(' ')!!],
                    charCipherLength
                )
            }
        }

        return result
    }

    override fun decode(source: String, arguments: Map<String, Any>): String {
        val key = arguments["Keyword"] as String

        val charCipherLength = (alphabet.size * alphabet.size).toString().length

        val square = generateRandomSquare(alphabet.size, key)

        var result = ""

        for (i in source.indices step charCipherLength) {
            var numberString = ""
            for (j in 0 until charCipherLength) {
                numberString += source[i + j]
            }
            val number = numberString.toInt()

            val firstSymbolIndex = square.indexOfFirst { array -> array.contains(number) }
            val secondSymbolIndex = square[firstSymbolIndex].indexOfFirst { num -> num == number }

            result += alphabet[firstSymbolIndex]
            result += alphabet[secondSymbolIndex]
        }

        return result
    }
}