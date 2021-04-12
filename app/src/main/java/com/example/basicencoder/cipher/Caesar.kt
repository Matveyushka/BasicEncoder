package com.example.basicencoder.cipher

import com.example.basicencoder.utils.*

val Caesar = object : ICipher {
    override fun encode(source: String, arguments: Map<String, Any>): String {
        val bias = arguments["Key"].toString().toInt()

        return (source.map { symbol ->
            getSourceAlphabet(symbol, standardAlphabets)?.getWithOffset(symbol, bias) ?: symbol
        }).joinToString("")
    }

    override fun decode(source: String, arguments: Map<String, Any>): String {
        val bias = arguments["Key"].toString().toInt()

        return encode(source, mapOf("Key" to (-bias).toString()))
    }
}

