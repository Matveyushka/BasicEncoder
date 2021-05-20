package com.example.basicencoder.cipher

import com.example.basicencoder.utils.*
import java.lang.StringBuilder
import java.math.BigInteger

private val usedAlphabet = byteAlphabet

private fun ceilLog(source: BigInteger, key: BigInteger): BigInteger {
    var result = BigInteger.valueOf(1)
    var currentValue = key
    while (currentValue < source) {
        currentValue *= key
        result += BigInteger.valueOf(1)
    }
    return result
}

private fun pow(source: BigInteger, degree: BigInteger): BigInteger {
    var result = BigInteger.valueOf(1)
    var currentDegree = BigInteger.valueOf(0)
    while (currentDegree != degree) {
        result *= source
        currentDegree += BigInteger.valueOf(1)
    }
    return result
}

private fun restoreCeilLog(source: BigInteger, key: BigInteger): BigInteger {
    var result = BigInteger.valueOf(1)
    while (source - result > pow(key, result)) {
        result += BigInteger.valueOf(1)
    }
    return result
}

private fun convertStringToBigInteger(source: String): BigInteger {
    var result = BigInteger.valueOf(0)
    for (symbol in source) {
        result += BigInteger.valueOf(usedAlphabet.getLetterNumber(symbol)!!.toLong() + 1)
        result *= BigInteger.valueOf(usedAlphabet.size.toLong())
    }
    result /= BigInteger.valueOf(usedAlphabet.size.toLong())
    return result
}

private fun convertBigIntegerToString(source: BigInteger): String {
    var result = ""
    var processedSource = source
    while (processedSource != BigInteger.valueOf(0)) {
        processedSource -= BigInteger.valueOf(1)
        val nextLetterValue = processedSource % BigInteger.valueOf(usedAlphabet.size.toLong())
        result = usedAlphabet[nextLetterValue.toInt()] + result
        processedSource -= nextLetterValue
        processedSource /= BigInteger.valueOf(usedAlphabet.size.toLong())
    }
    return result
}

val Matthew = object : ICipher {
    override val encodeAlphabets = listOf(byteAlphabet)
    override val decodeAlphabets = listOf(byteAlphabet)
    override val keyDescriptions = listOf(
        KeyDescription("Key", String::class.java, listOf(byteAlphabet))
    )

    override fun encode(source: String, arguments: Map<String, Any>): String {
        val key = arguments["Key"] as String
        val sourceNumeric = convertStringToBigInteger(source)
        val keyNumeric = convertStringToBigInteger(key)
        if (keyNumeric == BigInteger.valueOf(0)) {
            throw Exception("Key cannot be empty")
        }
        val log = ceilLog(sourceNumeric, keyNumeric)
        val power = pow(keyNumeric, log)
        val numberResult = (-sourceNumeric + power + log) *
                (keyNumeric / BigInteger.valueOf(7))
        return convertBigIntegerToString(
            numberResult
        )
    }

    override fun decode(source: String, arguments: Map<String, Any>): String {
        val key = arguments["Key"] as String
        val keyNumeric = convertStringToBigInteger(key)
        val encodedNumeric = convertStringToBigInteger(source) /
                (keyNumeric / BigInteger.valueOf(7))
        if (keyNumeric == BigInteger.valueOf(0)) {
            throw Exception("Key cannot be empty")
        }
        val restoredCeilLog = restoreCeilLog(encodedNumeric, keyNumeric)
        val power = pow(keyNumeric, restoredCeilLog)
        return convertBigIntegerToString(
            -encodedNumeric + power + restoredCeilLog
        )
    }
}