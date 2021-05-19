package com.example.basicencoder.cipher

import com.example.basicencoder.utils.Alphabet

interface ICipher {
    val alphabet: Alphabet
    fun encode(source: String, arguments: Map<String, Any>): String
    fun decode(source: String, arguments: Map<String, Any>): String
}