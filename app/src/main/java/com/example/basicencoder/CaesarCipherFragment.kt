package com.example.basicencoder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.basicencoder.databinding.FragmentCaesarCipherBinding
import com.example.basicencoder.utils.*

class CaesarCipherFragment : BasicCipherFragment() {
    override val innerLayoutId: Int = R.layout.fragment_caesar_cipher
    override val title: String = "Caesar cipher"

    private var _binding: FragmentCaesarCipherBinding? = null
    private val binding get() = _binding!!

    override fun getBindedParamsView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCaesarCipherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun encode(source: String): String {
        val biasInput = binding.inputCaesarBias.text.toString().toIntOrNull()

        return caesarEncode(source, biasInput ?: 1)
    }

    override fun decode(source: String): String {
        val biasInput = binding.inputCaesarBias.text.toString().toIntOrNull()

        return caesarDecode(source, biasInput ?: 1)
    }

    private val usedAlphabets = listOf(
            englishLowerCaseAlphabet,
            englishUpperCaseAlphabet,
            cyrillicLowerCaseAlphabet,
            cyrillicUpperCaseAlphabet,
            numbersAlphabet
    )

    private fun caesarEncode(source: String, bias: Int): String {
        return (source.map { symbol ->
            getSourceAlphabet(symbol, usedAlphabets)?.getWithOffset(symbol, bias) ?: symbol
        }).joinToString("")
    }

    private fun caesarDecode(source: String, bias: Int): String {
        return caesarEncode(source, -bias)
    }
}