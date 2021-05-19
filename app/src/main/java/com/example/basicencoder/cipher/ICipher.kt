package com.example.basicencoder.cipher

import com.example.basicencoder.utils.Alphabet
import com.example.basicencoder.utils.KeyDescription

interface ICipher {
    val encodeAlphabets: List<Alphabet>
    val decodeAlphabets: List<Alphabet>
    val keyDescriptions: List<KeyDescription>
    fun encode(source: String, arguments: Map<String, Any>): String
    fun decode(source: String, arguments: Map<String, Any>): String
}