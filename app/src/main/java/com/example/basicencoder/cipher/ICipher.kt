package com.example.basicencoder.cipher

interface ICipher {
    fun encode(source: String, arguments: Map<String, Any>): String
    fun decode(source: String, arguments: Map<String, Any>): String
}