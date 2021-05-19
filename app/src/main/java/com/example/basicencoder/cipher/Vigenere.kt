package com.example.basicencoder.cipher

import com.example.basicencoder.utils.Alphabet
import com.example.basicencoder.utils.englishLowerCaseAlphabet

val Vigenere = object : ICipher {
    override val alphabet: Alphabet = englishLowerCaseAlphabet

    override fun encode(source: String, arguments: Map<String, Any>): String {
        val keyword = arguments["Keyword"] as String
        return vigenereEncode(source, keyword)
    }

    override fun decode(source: String, arguments: Map<String, Any>): String {
        val keyword = arguments["Keyword"] as String
        return vigenereDecode(source, keyword)
    }

    private fun forwardVigenereTableConversion(sourceChar: Char, keywordChar: Char): Char {
        return alphabet[(alphabet.getLetterNumber(sourceChar)!! + alphabet.getLetterNumber(keywordChar)!!) % alphabet.size]
    }

    private fun reverseVigenereTableConversation(sourceChar: Char, keywordChar: Char): Char {
        return alphabet[(alphabet.getLetterNumber(sourceChar)!! + alphabet.size - alphabet.getLetterNumber(keywordChar)!!) % alphabet.size]
    }

    private fun vigenereEncode(source: String, key: String): String {
        return source.mapIndexed { index, symbol ->
            forwardVigenereTableConversion(symbol, key[index % key.length])
        }.joinToString("")
    }

    private fun vigenereDecode(source: String, key: String): String {
        return source.mapIndexed { index, symbol ->
            reverseVigenereTableConversation(symbol, key[index % key.length])
        }.joinToString("")
    }
}