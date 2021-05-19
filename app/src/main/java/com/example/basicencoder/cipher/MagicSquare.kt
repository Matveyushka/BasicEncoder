package com.example.basicencoder.cipher

import com.example.basicencoder.utils.Alphabet
import com.example.basicencoder.utils.KeyDescription
import com.example.basicencoder.utils.byteAlphabet
import kotlin.math.ceil
import kotlin.math.sqrt

class MagicSquareTraveller(private val size: Int) {
    private var squareMap = Array(size) { Array(size) {false} }
    var x: Int = size - 1
        private set
    var y: Int = size / 2
        private set

    fun getNextCoordinates() {
        squareMap[y][x] = true
        y = (y - 1 + size) % size
        x = (x + 1) % size
        if (squareMap[y][x]) {
            x = (x - 2 + size) % size
            y = (y + 1 + size) % size
            if (squareMap[y][x]) {
                squareMap = Array(size) { Array(size) {false} }
                x = size - 1
                y = size / 2
            }
        }
    }
}

fun generateSquare(n: Int, source: String? = null) : Array<Array<String?>> {
    val magicSquare = Array(n) { arrayOfNulls<String>(n) }
    val squareTraveller = MagicSquareTraveller(n)

    for (num in 1..n*n) {
        magicSquare[squareTraveller.y][squareTraveller.x] = if (source == null) {
            num.toString()
        } else if (source.length < num) {
            null
        } else {
            source[num - 1].toString()
        }
        squareTraveller.getNextCoordinates()
    }
    return magicSquare
}

fun getDecodeSquare(numberSquare: Array<Array<Int>>, source: String) : Array<Array<String?>> {
    var sourceCounter = 0
    return (numberSquare.map { row ->
        (row.map { index ->
            if (source.length >= index) {
                source[sourceCounter++].toString()
            } else {
                null
            }
        }).toTypedArray()
    }).toTypedArray()
}

val MagicSquare = object: ICipher {
    override val encodeAlphabets = listOf(byteAlphabet)
    override val decodeAlphabets = listOf(byteAlphabet)
    override val keyDescriptions = listOf<KeyDescription>()

    override fun encode(source: String, arguments: Map<String, Any>): String {
        var squareSize: Int = ceil(sqrt(source.length.toDouble())).toInt()
        if (squareSize % 2 == 0) { squareSize++ }

        val symbolSquare = generateSquare(squareSize, source)

        return symbolSquare.fold("") { result, current ->
            result + current.fold("") { subresult, symbol ->
                subresult + (symbol ?: "")
            }
        }
    }

    override fun decode(source: String, arguments: Map<String, Any>): String {
        var squareSize: Int = ceil(sqrt(source.length.toDouble())).toInt()
        if (squareSize % 2 == 0) { squareSize++ }

        val numberSquare = generateSquare(squareSize)
        val intSquare = numberSquare.map { row -> row.map { index -> index?.toInt() ?: 0 }.toTypedArray() }.toTypedArray()

        val decodeSquare = getDecodeSquare(intSquare, source)

        val squareTraveller = MagicSquareTraveller(squareSize)

        var result = ""

        while (decodeSquare[squareTraveller.y][squareTraveller.x] != null && result.length != squareSize * squareSize) {
            result += decodeSquare[squareTraveller.y][squareTraveller.x]
            squareTraveller.getNextCoordinates()
        }

        return result
    }
}