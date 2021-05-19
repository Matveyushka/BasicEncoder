package com.example.basicencoder.cipher.visualizer

import android.graphics.Color
import android.graphics.Color.parseColor
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
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
    private fun prepareView(
        layout: ConstraintLayout
    ) : View {
        val view = LayoutInflater.from(layout.context).inflate(
            R.layout.magic_square_visualize_part, layout, false)

        layout.removeAllViews()

        layout.addView(view)

        val params = view.layoutParams as ConstraintLayout.LayoutParams
        params.rightToRight = layout.id
        params.topToTop = layout.id
        params.leftToLeft = layout.id
        params.bottomToBottom = layout.id
        view.layoutParams = params
        view.requestLayout()

        return view
    }

    private fun showSquares(
        sourceView: View,
        message: String
    ) {
        val numberTable = sourceView.findViewById<TableLayout>(R.id.number_table)
        val letterTable = sourceView.findViewById<TableLayout>(R.id.letter_table)

        var squareSize: Int = ceil(sqrt(message.length.toDouble())).toInt()
        if (squareSize % 2 == 0) { squareSize++ }

        val numberSquare = generateSquare(squareSize)
        val symbolSquare = generateSquare(squareSize, message)
        showMagicSquare(numberSquare, numberTable)
        showMagicSquare(symbolSquare, letterTable)
    }

    override fun showEncode(
        layout: ConstraintLayout,
        source: String,
        result: String,
        arguments: Map<String, Any>
    ) {
        val view = prepareView(layout)
        showSquares(view, source)
    }

    override fun showDecode(
        layout: ConstraintLayout,
        source: String,
        result: String,
        arguments: Map<String, Any>
    ) {
        val view = prepareView(layout)
        showSquares(view, result)
    }

    private fun showMagicSquare(square: Array<Array<String?>>, table: TableLayout) {
        table.removeAllViews()

        for (row in square)
        {
            val tableRow = TableRow(table.context)
            tableRow.layoutParams = ViewGroup.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
            for (element in row) {
                val text = TextView(table.context)
                text.text = element ?: "_"
                text.textSize = 18.toFloat()
                text.setPadding(16, 2, 16, 2)
                text.gravity = Gravity.CENTER_HORIZONTAL
                tableRow.addView(text)
            }
            table.addView(tableRow)
        }
    }
}