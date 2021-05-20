package com.example.basicencoder.cipher

import com.example.basicencoder.utils.Alphabet
import com.example.basicencoder.utils.KeyDescription
import com.example.basicencoder.utils.englishLowerCaseAlphabet

val Vigenere = object : ICipher {
    override val encodeAlphabets = listOf(englishLowerCaseAlphabet)
    override val decodeAlphabets = listOf(englishLowerCaseAlphabet)
    override val keyDescriptions = listOf(
        KeyDescription("Keyword", String::class.java, listOf(englishLowerCaseAlphabet))
    )

    private val alphabet = encodeAlphabets[0]

    override fun encode(source: String, arguments: Map<String, Any>): String {
        val keyword = arguments["Keyword"] as String
        return source.mapIndexed { index, symbol ->
            forwardVigenereTableConversion(symbol, keyword[index % keyword.length])
        }.joinToString("")
    }

    override fun decode(source: String, arguments: Map<String, Any>): String {
        val keyword = arguments["Keyword"] as String
        return source.mapIndexed { index, symbol ->
            reverseVigenereTableConversation(symbol, keyword[index % keyword.length])
        }.joinToString("")
    }

    private fun forwardVigenereTableConversion(sourceChar: Char, keywordChar: Char): Char {
        return alphabet[(alphabet.getLetterNumber(sourceChar)!!
                + alphabet.getLetterNumber(keywordChar)!!) % alphabet.size]
    }

    private fun reverseVigenereTableConversation(sourceChar: Char, keywordChar: Char): Char {
        return alphabet[(alphabet.getLetterNumber(sourceChar)!!
                + alphabet.size - alphabet.getLetterNumber(keywordChar)!!) % alphabet.size]
    }
}