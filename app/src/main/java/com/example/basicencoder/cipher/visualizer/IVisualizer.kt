package com.example.basicencoder.cipher.visualizer

import androidx.constraintlayout.widget.ConstraintLayout

interface IVisualizer {
    fun showEncode(
        layout: ConstraintLayout,
        source: String,
        result: String,
        arguments: Map<String, Any>)

    fun showDecode(
        layout: ConstraintLayout,
        source: String,
        result: String,
        arguments: Map<String, Any>)
}