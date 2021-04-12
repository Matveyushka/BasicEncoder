package com.example.basicencoder.cipher.visualizer

import androidx.constraintlayout.widget.ConstraintLayout

interface IVisualizer {
    fun visualize(
        layout: ConstraintLayout,
        source: String,
        result: String,
        arguments: Map<String, Any>)
}