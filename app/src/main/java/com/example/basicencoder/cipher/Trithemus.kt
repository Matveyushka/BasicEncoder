package com.example.basicencoder.cipher

import com.example.basicencoder.utils.*
import java.lang.Exception

val Trithemus = object : ICipher {
    override val encodeAlphabets = standardAlphabets
    override val decodeAlphabets = standardAlphabets
    override val keyDescriptions = listOf(
        KeyDescription(
            "Function",
            String::class.java,
            listOf(
                numbersAlphabet
                    .combine("x+-*/")
                    .nameIt("Formula")),
            "3*x*x+5*x+2")
    )

    override fun encode(source: String, arguments: Map<String, Any>): String {
        val key = arguments["Function"] as String

        val processedSource = source.mapIndexed { index, _ ->
            tryToCalculateKey(key, index)
        }

        return if (processedSource.contains(null)) {
            throw Exception("Bad key")
        } else {
            source.toCharArray().mapIndexed { index, symbol ->
                getSourceAlphabet(symbol, encodeAlphabets)
                    ?.getWithOffset(symbol, processedSource[index]!!) ?: symbol
            }.joinToString("")
        }
    }

    override fun decode(source: String, arguments: Map<String, Any>): String {
        val key = arguments["Function"] as String

        val processedSource = source.mapIndexed { index, _ ->
            tryToCalculateKey(key, index)
        }

        return if (processedSource.contains(null)) {
            throw Exception("Bad key")
        } else {
            source.toCharArray().mapIndexed { index, symbol ->
                getSourceAlphabet(symbol, decodeAlphabets)
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
        return operations[operator]?.invoke(leftNumber, rightNumber).toString()
    }

    private fun parseStringToElements(source: String) : MutableList<String> {
        val trimmedKey = source.filter { symbol -> symbol != ' ' }
        val elements: MutableList<String> = mutableListOf()
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
            if (!(elements.contains("*")
                        || elements.contains("+")
                        || elements.contains("-"))
                && elements.count() > 1) {
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
            badKey = runOperators(elements, arrayOf("*"))
                    || runOperators(elements, arrayOf("+", "-"))
        }
        return if (badKey) {
            null
        } else {
            elements[0].toIntOrNull()
        }
    }


}