package com.example.basicencoder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.basicencoder.databinding.FragmentVigenereCipherBinding


class VigenereCipherFragment : BasicCipherFragment() {
    override val innerLayoutId: Int = R.layout.fragment_vigenere_cipher
    override val title: String = "Vigenere cipher"

    private var _binding: FragmentVigenereCipherBinding? = null
    private val binding get() = _binding!!

    override fun getBindedParamsView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVigenereCipherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun encode(source: String): String {
        val keyword = binding.vigenereKeywordInput.text.toString()
        val result: String by lazy { vigenereEncode(source, keyword) }

        return if (keyword.isEmpty()) {
            Toast.makeText(context, "Keyword must be not empty", Toast.LENGTH_SHORT).show()
            ""
        } else if (keyword.contains("[^a-z]".toRegex())) {
            Toast.makeText(context, "Keyword must contain only low case english letters", Toast.LENGTH_SHORT).show()
            ""
        } else {
            vigenereEncode(source, keyword)
        }
    }

    override fun decode(source: String): String {
        val keyword = binding.vigenereKeywordInput.text.toString()

        return if (keyword.isEmpty()) {
            Toast.makeText(context, "Keyword must be not empty", Toast.LENGTH_SHORT).show()
            ""
        } else if (keyword.contains("[^a-z]".toRegex())) {
            Toast.makeText(context, "Keyword must contain only low case english letters", Toast.LENGTH_SHORT).show()
            ""
        } else {
            vigenereDecode(source, keyword)
        }
    }

    private fun forwardVigenereTableConversion(sourceChar: Char, keywordChar: Char): Char {
        return if (sourceChar in 'a'..'z') {
            ('a'.toInt() + ((sourceChar - 'a') + (keywordChar - 'a')) % ('z' - 'a' + 1)).toChar()
        } else if (sourceChar in 'A'..'Z') {
            ('A'.toInt() + ((sourceChar - 'A') + (keywordChar - 'a')) % ('z' - 'a' + 1)).toChar()
        } else {
            sourceChar
        }
    }

    private fun reverseVigenereTableConversation(sourceChar: Char, keywordChar: Char): Char {
        return if (sourceChar in 'a'..'z') {
            ('a'.toInt() + ((sourceChar - 'a') + ('z' - 'a' + 1) - (keywordChar - 'a')) % ('z' - 'a' + 1)).toChar()
        } else if (sourceChar in 'A'..'Z') {
            ('A'.toInt() + ((sourceChar - 'A') + ('z' - 'a' + 1) - (keywordChar - 'a')) % ('z' - 'a' + 1)).toChar()
        } else {
            sourceChar
        }
    }

    private fun vigenereEncode(source: String, key: String): String {
        return source.mapIndexed { index, symbol ->
            forwardVigenereTableConversion(symbol, key[index % key.length])
        }.joinToString("")
    }

    private fun vigenereDecode(source: String, key: String): String {
        return source.mapIndexed { index, symbol ->
            reverseVigenereTableConversation(symbol, key[index % key.length])
        }.joinToString("")
    }
}
