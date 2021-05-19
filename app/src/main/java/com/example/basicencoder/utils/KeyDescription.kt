package com.example.basicencoder.utils

import java.lang.reflect.Type

class KeyDescription(
    val name: String,
    val type: Type,
    val alphabets: List<Alphabet>? = null,
    val defaultValue: Any? = null
) { }