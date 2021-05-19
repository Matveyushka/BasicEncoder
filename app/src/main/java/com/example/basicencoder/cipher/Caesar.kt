package com.example.basicencoder.cipher

import com.example.basicencoder.utils.*

val Caesar = object : ICipher {
    override val encodeAlphabets = standardAlphabets
    override val decodeAlphabets = standardAlphabets
    override val keyDescriptions = listOf(
        KeyDescription("Shift", Int::class.java, null, 3)
    )

    override fun encode(source: String, arguments: Map<String, Any>): String {
        val bias = arguments["Shift"].toString().toInt()

        return (source.map { symbol ->
            getSourceAlphabet(symbol, standardAlphabets)?.getWithOffset(symbol, bias) ?: symbol
        }).joinToString("")
    }

    override fun decode(source: String, arguments: Map<String, Any>): String {
        val bias = arguments["Shift"].toString().toInt()

        return encode(source, mapOf("Shift" to -bias))
    }
}

