package com.example.basicencoder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.basicencoder.databinding.FragmentCaesarCipherBinding
import com.example.basicencoder.databinding.FragmentTrithemusCipherBinding
import com.example.basicencoder.utils.*

class TrithemusCipherFragment : BasicCipherFragment() {
    override val innerLayoutId: Int = R.layout.fragment_trithemus_cipher
    override val title: String = "Trithemus cipher"

    private var _binding: FragmentTrithemusCipherBinding? = null
    private val binding get() = _binding!!
    override fun getBindedParamsView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        _binding = FragmentTrithemusCipherBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    override fun encode(source: String): String {
        val key = binding.trithemusKeyInput.text.toString()

        val processedSource = source.mapIndexed { index, _ ->
            tryToCalculateKey(key, index)
        }

        return if (processedSource.contains(null)) {
            Toast.makeText(context, "Bad key", Toast.LENGTH_SHORT).show()
            ""
        } else {
            source.toCharArray().mapIndexed { index, symbol ->
                getSourceAlphabet(symbol, standardAlphabets)
                        ?.getWithOffset(symbol, processedSource[index]!!) ?: symbol
            }.joinToString("")
        }
    }

    override fun decode(source: String): String {
        val key = binding.trithemusKeyInput.text.toString()

        val processedSource = source.mapIndexed { index, _ ->
            tryToCalculateKey(key, index)
        }

        return if (processedSource.contains(null)) {
            Toast.makeText(context, "Bad key", Toast.LENGTH_SHORT).show()
            ""
        } else {
            source.toCharArray().mapIndexed { index, symbol ->
                getSourceAlphabet(symbol, standardAlphabets)
                        ?.getWithOffset(symbol, -processedSource[index]!!) ?: symbol
            }.joinToString("")
        }
    }

    private val operations: Map<String, (operand1: Int, operand2: Int) -> Int> = mapOf(
            "*" to { a, b -> a * b },
            "+" to { a, b -> a + b },
            "-" to { a, b -> a - b }
    )

    private fun tryToPerformOperation(operationIndex: Int, elements: MutableList<String>): String? {
        if (operationIndex == 0 || operationIndex == elements.count() - 1) {
            return null
        }
        val leftOperand = elements[operationIndex - 1]
        val operator = elements[operationIndex]
        val rightOperand = elements[operationIndex + 1]

        val leftNumber = leftOperand.toIntOrNull()
        val rightNumber = rightOperand.toIntOrNull()
        if (leftNumber == null || rightNumber == null) {
            return null
        }
        return operations[operator]?.invoke(leftNumber, rightNumber).toString() ?: null
    }

    private fun parseStringToElements(source: String) : MutableList<String> {
        var trimmedKey = source.filter { symbol -> symbol != ' ' }
        var elements: MutableList<String> = mutableListOf()
        var currentNumber = ""
        for (symbol in trimmedKey) {
            if (symbol in '0'..'9') {
                currentNumber += symbol
            } else {
                if (currentNumber.isNotEmpty()) {
                    elements.add(currentNumber)
                    currentNumber = ""
                }
                elements.add(symbol.toString())
            }
        }
        if (currentNumber.isNotEmpty()) {
            elements.add(currentNumber)
        }
        return elements
    }

    private fun runOperators(elements: MutableList<String>, operators: Array<String>) : Boolean {
        var badKey = false

        var operationCompleted = false
        var index = 0
        while (index < elements.count() && !badKey) {
            for (operator in operators) {
                if (elements[index] == operator) {
                    val result = tryToPerformOperation(index, elements)
                    if (result == null) {
                        badKey = true
                    } else {
                        elements[index - 1] = result
                        elements.removeAt(index)
                        elements.removeAt(index)
                        operationCompleted = true
                        break
                    }
                }
            }
            index++
            if (operationCompleted) {
                index--
                operationCompleted = false
            }
            if (!(elements.contains("*") || elements.contains("+") || elements.contains("-")) && elements.count() > 1) {
                badKey = true
            }
        }
        return badKey
    }

    private fun tryToCalculateKey(key: String, position: Int = 1) : Int? {
        var elements = parseStringToElements(key)

        elements = elements.map { symbol ->
            if (symbol[0] == 'x' || symbol[0] == 'X') {
                position.toString()
            } else {
                symbol
            }
        }.toMutableList()

        var badKey = false
        while (elements.count() != 1 && !badKey) {
            badKey = runOperators(elements, arrayOf("*")) || runOperators(elements, arrayOf("+", "-"))
        }
        return if (badKey) {
            null
        } else {
            elements[0].toIntOrNull()
        }
    }
}