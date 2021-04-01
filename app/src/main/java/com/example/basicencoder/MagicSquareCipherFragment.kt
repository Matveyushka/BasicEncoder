package com.example.basicencoder

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.basicencoder.databinding.FragmentMagicSquareCipherBinding
import kotlin.math.ceil
import kotlin.math.sqrt

class MagicSquareCipherFragment : BasicCipherFragment() {
    override val innerLayoutId: Int = R.layout.fragment_magic_square_cipher
    override val title: String = "Magic square cipher"

    private var _binding: FragmentMagicSquareCipherBinding? = null
    private val binding get() = _binding!!

    override fun getBindedParamsView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        _binding = FragmentMagicSquareCipherBinding.inflate(
                inflater,
                container,
                false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun encode(source: String): String {
        var squareSize: Int = ceil(sqrt(source.length.toDouble())).toInt()
        if (squareSize % 2 == 0) { squareSize++ }

        val numberSquare = generateSquare(squareSize)
        val symbolSquare = generateSquare(squareSize, source)
        showMagicSquare(numberSquare, binding.magicSquareTableNumbers)
        showMagicSquare(symbolSquare, binding.magicSquareTableSymbols)

        return symbolSquare.fold("") { result, current ->
            result + current.fold("") { subresult, symbol ->
                subresult + (symbol ?: "")
            }
        }
    }

    override fun decode(source: String): String {
        var squareSize: Int = ceil(sqrt(source.length.toDouble())).toInt()
        if (squareSize % 2 == 0) { squareSize++ }

        val numberSquare = generateSquare(squareSize)
        val intSquare = numberSquare.map { row -> row.map { index -> index?.toInt() ?: 0 }.toTypedArray() }.toTypedArray()

        val decodeSquare = getDecodeSquare(intSquare, source)

        val squareTraveller = MagicSquareTraveller(squareSize)

        var result = ""

        while (decodeSquare[squareTraveller.y][squareTraveller.x] != null && result.length != squareSize * squareSize) {
            result += decodeSquare[squareTraveller.y][squareTraveller.x]
            squareTraveller.getNextCoordinates()
        }

        showMagicSquare(numberSquare, binding.magicSquareTableNumbers)
        showMagicSquare(decodeSquare, binding.magicSquareTableSymbols)
        return result
    }

    private fun showMagicSquare(square: Array<Array<String?>>, table: TableLayout) {
        table.removeAllViews()

        for (row in square)
        {
            val tableRow: TableRow = TableRow(context)
            tableRow.layoutParams = ViewGroup.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
            for (element in row) {
                val text: TextView = TextView(context)
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

    private fun generateSquare(n: Int, source: String? = null) : Array<Array<String?>> {
        val magicSquare = Array(n) { arrayOfNulls<String>(n) }
        val squareTraveller = MagicSquareTraveller(n)

        for (num in 1..n*n) {
            magicSquare[squareTraveller.y][squareTraveller.x] = if (source == null) {
                num.toString()
            } else if (source.length < num) {
                null
            } else {
                source[num - 1].toString()
            }
            squareTraveller.getNextCoordinates()
        }
        return magicSquare
    }

    private fun getDecodeSquare(numberSquare: Array<Array<Int>>, source: String) : Array<Array<String?>> {
        var sourceCounter = 0
        return (numberSquare.map { row ->
            (row.map { index ->
                if (source.length >= index) {
                    source[sourceCounter++].toString()
                } else {
                    null
                }
            }).toTypedArray()
        }).toTypedArray()
    }
}

class MagicSquareTraveller(private val size: Int) {
    private var squareMap = Array(size) { Array(size) {false} }
    var x: Int = size - 1
        private set
    var y: Int = size / 2
        private set

    fun getNextCoordinates() {
        squareMap[y][x] = true
        y = (y - 1 + size) % size
        x = (x + 1) % size
        if (squareMap[y][x]) {
            x = (x - 2 + size) % size
            y = (y + 1 + size) % size
            if (squareMap[y][x]) {
                squareMap = Array(size) { Array(size) {false} }
                x = size - 1
                y = size / 2
            }
        }
    }
}