@file:Suppress("EXPERIMENTAL_API_USAGE")

package com.example.basicencoder.cipher

import com.example.basicencoder.utils.*
import java.lang.IllegalArgumentException
import java.lang.Integer.MAX_VALUE
import kotlin.math.pow

private fun stringToByteArray(original: String): List<UByte?> {
    return original.map { symbol -> byteAlphabet.getLetterNumber(symbol)?.toUByte() }
}

private fun xorStrings(firstString: String, secondString: String): String? {
    val firstByteString = stringToByteArray(firstString)
    val secondByteString = stringToByteArray(secondString)
    var result: String? = null
    if (!firstByteString.contains(null) && !secondByteString.contains(null)) {
        val xorResult = firstByteString.mapIndexed {
                index, value -> value!!.toInt() xor secondByteString[index]!!.toInt()
            }
        result = xorResult.map { byte -> byteAlphabet[byte] }.joinToString("")
    }
    return result
}

private fun getNextBlock(
    block: String,
    key: String,
    convertSubBlock: (rightBlockPart: String, key: String) -> String
) : String {
    if (block.length != 8) {
        throw IllegalArgumentException("Block must be 8 chars length.")
    }
    val left = block.substring(0, block.length / 2)
    val right = block.substring(block.length / 2, block.length)
    val nextRight = xorStrings(convertSubBlock(right, key), left)
    return "$right$nextRight"
}

private fun getPrevBlock(
    block: String,
    key: String,
    convertSubBlock: (leftBlockPart: String, key: String) -> String
) : String {
    if (block.length != 8) {
        throw IllegalArgumentException("Block must be 8 chars length.")
    }
    val left = block.substring(0, block.length / 2)
    val right = block.substring(block.length / 2, block.length)
    val nextLeft = xorStrings(convertSubBlock(left, key), right)
    return "$nextLeft$left"
}

private fun multMod(blockPart: String, key: String): String {
    val partArray = stringToByteArray(blockPart)
    val keyArray = stringToByteArray(key)
    var result: String? = null
    if (!partArray.contains(null) && !keyArray.contains(null)) {
        result = partArray
            .mapIndexed { index, value -> (value!!.toInt() * keyArray[index]!!.toInt()) % 256 }
            .map { number -> byteAlphabet[number] }.joinToString("")
    }
    return result!!
}

private fun getKeyHash(key: String): String {
    val numberHash = (key.hashCode().toDouble().pow(30) % MAX_VALUE).toInt()
    return byteAlphabet[numberHash % 256].toString() +
            byteAlphabet[numberHash / 256 % 256] +
            byteAlphabet[numberHash / 256 / 256 % 256] +
            byteAlphabet[numberHash / 256 / 256 / 256 % 256]
}

private fun getRoundKey(round: Int, key: String) : String {
    val numberHash = (key.hashCode().toDouble().pow(6 + round) % MAX_VALUE).toInt()
    return byteAlphabet[numberHash % 256].toString() +
            byteAlphabet[numberHash / 256 % 256] +
            byteAlphabet[numberHash / 256 / 256 % 256] +
            byteAlphabet[numberHash / 256 / 256 / 256 % 256]
}

val Feistel = object : ICipher {
    override val encodeAlphabets = listOf(byteAlphabet)
    override val decodeAlphabets = listOf(byteAlphabet)
    override val keyDescriptions = listOf(
        KeyDescription("Key", String::class.java, listOf(byteAlphabet))
    )

    override fun encode(source: String, arguments: Map<String, Any>): String {
        val key = getKeyHash(arguments["Key"] as String)

        var stringToEncode = source + " ".repeat((8 - source.length % 8) % 8)

        val rounds = 32

        val blocksAmount = stringToEncode.length / 8

        for (round in 0 until rounds) {
            val blocks : MutableList<String> = mutableListOf()
            for (blockNumber in 0 until blocksAmount) {
                blocks.add(getNextBlock(
                    stringToEncode.substring(blockNumber * 8, blockNumber * 8 + 8),
                    getRoundKey(round, key),
                    ::multMod
                    ))
            }
            stringToEncode = blocks.joinToString("")
        }

        return stringToEncode
    }

    override fun decode(source: String, arguments: Map<String, Any>): String {
        val key = getKeyHash(arguments["Key"] as String)

        var stringToDecode = source + " ".repeat((8 - source.length % 8) % 8)

        val rounds = 32

        val blocksAmount = stringToDecode.length / 8

        for (round in 0 until rounds) {
            val blocks : MutableList<String> = mutableListOf()
            for (blockNumber in 0 until blocksAmount) {
                blocks.add(getPrevBlock(
                    stringToDecode.substring(blockNumber * 8, blockNumber * 8 + 8),
                    getRoundKey(31 - round, key),
                    ::multMod
                    ))
            }
            stringToDecode = blocks.joinToString("")
        }

        return stringToDecode
    }
}