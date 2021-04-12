package com.example.basicencoder.cipher.visualizer

import android.graphics.Color
import android.graphics.Color.parseColor
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.basicencoder.R
import com.example.basicencoder.cipher.generateSquare
import kotlin.math.ceil
import kotlin.math.sqrt

val MagicSquareVisualize = object : IVisualizer {
    override fun visualize(
        layout: ConstraintLayout,
        source: String,
        result: String,
        arguments: Map<String, Any>
    ) {
        val abc = LayoutInflater.from(layout.context).inflate(
            R.layout.magic_square_visualize_part, layout, false)

        layout.removeAllViews()

        layout.addView(abc)

        val params = abc.layoutParams as ConstraintLayout.LayoutParams
        params.rightToRight = layout.id
        params.topToTop = layout.id
        params.leftToLeft = layout.id
        params.bottomToBottom = layout.id
        abc.layoutParams = params
        abc.requestLayout()

        val numberTable = abc.findViewById<TableLayout>(R.id.number_table)
        val letterTable = abc.findViewById<TableLayout>(R.id.letter_table)

        println(numberTable)

        var squareSize: Int = ceil(sqrt(source.length.toDouble())).toInt()
        if (squareSize % 2 == 0) { squareSize++ }

        val numberSquare = generateSquare(squareSize)
        val symbolSquare = generateSquare(squareSize, source)
        showMagicSquare(numberSquare, numberTable)
        showMagicSquare(symbolSquare, letterTable)
    }

    private fun showMagicSquare(square: Array<Array<String?>>, table: TableLayout) {
        table.removeAllViews()

        for (row in square)
        {
            val tableRow = TableRow(table.context)
            tableRow.layoutParams = ViewGroup.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
            for (element in row) {
                val text = TextView(table.context)
                text.text = if (element == null) {
                    "_"
                } else {
                    element.toString()
                }
                text.textSize = 18.toFloat()
                text.setPadding(16, 2, 16, 2)
                text.gravity = Gravity.CENTER_HORIZONTAL
                tableRow.addView(text)
            }
            table.addView(tableRow)
        }
    }
}