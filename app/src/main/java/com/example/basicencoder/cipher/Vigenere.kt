package com.example.basicencoder.cipher

val Vigenere = object : ICipher {
    override fun encode(source: String, arguments: Map<String, Any>): String {
        val keyword = arguments["Keyword"] as String

        if (keyword.isEmpty()) {
            throw Exception("Keyword must be not empty")
        }
        if (keyword.contains("[^a-z]".toRegex())) {
            throw Exception("Keyword must contain only low case english letters")
        }
        return vigenereEncode(source, keyword)
    }

    override fun decode(source: String, arguments: Map<String, Any>): String {
        val keyword = arguments["Keyword"] as String

        if (keyword.isEmpty()) {
            throw Exception("Keyword must be not empty")
        }
        if (keyword.contains("[^a-z]".toRegex())) {
            throw Exception("Keyword must contain only low case english letters")
        }
        return vigenereDecode(source, keyword)
    }

    private fun forwardVigenereTableConversion(sourceChar: Char, keywordChar: Char): Char {
        return if (sourceChar in 'a'..'z') {
            ('a'.toInt() + ((sourceChar - 'a') + (keywordChar - 'a')) % ('z' - 'a' + 1)).toChar()
        } else if (sourceChar in 'A'..'Z') {
            ('A'.toInt() + ((sourceChar - 'A') + (keywordChar - 'a')) % ('z' - 'a' + 1)).toChar()
        } else {
            sourceChar
        }
    }

    private fun reverseVigenereTableConversation(sourceChar: Char, keywordChar: Char): Char {
        return if (sourceChar in 'a'..'z') {
            ('a'.toInt() + ((sourceChar - 'a') + ('z' - 'a' + 1) - (keywordChar - 'a')) % ('z' - 'a' + 1)).toChar()
        } else if (sourceChar in 'A'..'Z') {
            ('A'.toInt() + ((sourceChar - 'A') + ('z' - 'a' + 1) - (keywordChar - 'a')) % ('z' - 'a' + 1)).toChar()
        } else {
            sourceChar
        }
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