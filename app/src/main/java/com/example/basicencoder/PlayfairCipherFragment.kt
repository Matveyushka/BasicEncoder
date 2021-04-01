package com.example.basicencoder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.basicencoder.databinding.FragmentCaesarCipherBinding
import com.example.basicencoder.databinding.FragmentPlayfairCipherBinding
import com.example.basicencoder.utils.Alphabet
import kotlin.math.floor
import kotlin.math.sign

class PlayfairCipherFragment : BasicCipherFragment() {
    override val innerLayoutId: Int = R.layout.fragment_playfair_cipher

    override val title: String = "Playfair cipher"

    private var _binding: FragmentPlayfairCipherBinding? = null
    private val binding get() = _binding!!

    override fun getBindedParamsView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentPlayfairCipherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val alphabetSourceString = "абвгдежзийклмнопрстуфхцчшщъыьэюя"
    private val rectangularCyrillicAlphabet = Alphabet(alphabetSourceString);
    private val extraLetter = 'ё';
    private val alphabetWidth = 8;
    private val alphabetHeight = 4;

    private fun getLetterPosition(letter: Char, alphabet: Alphabet): Pair<Int, Int> {
        val letterIndex = alphabet.getLetterNumber(letter) ?: 0;
        val x = letterIndex % alphabetWidth
        val y = letterIndex / alphabetWidth
        return x to y;
    }

    private fun encodeLetterPair(first: Char, second: Char): Pair<Char, Char> {
        val key: String = binding.inputPlayfairKey.text.toString()

        val alphabet = rectangularCyrillicAlphabet.getKeyedAlphabet(key) ?: rectangularCyrillicAlphabet

        val firstLetterPosition = getLetterPosition(first, alphabet);
        val secondLetterPosition = getLetterPosition(second, alphabet);

        var newFirstPosition: Pair<Int, Int> = 0 to 0;
        var newSecondPosition: Pair<Int, Int> = 0 to 0;

        return if (firstLetterPosition.first != secondLetterPosition.first && firstLetterPosition.second != secondLetterPosition.second) {
            alphabet[secondLetterPosition.first + firstLetterPosition.second * alphabetWidth] to
            alphabet[firstLetterPosition.first + secondLetterPosition.second * alphabetWidth]
        } else if (firstLetterPosition.first == secondLetterPosition.first && firstLetterPosition.second != secondLetterPosition.second) {
            alphabet[firstLetterPosition.first + (firstLetterPosition.second + 1 + alphabetHeight) % alphabetHeight * alphabetWidth] to
            alphabet[secondLetterPosition.first + (secondLetterPosition.second + 1 + alphabetHeight) % alphabetHeight * alphabetWidth]
        } else if (firstLetterPosition.first != secondLetterPosition.first && firstLetterPosition.second == secondLetterPosition.second) {
            alphabet[(firstLetterPosition.first + 1 + alphabetWidth) % alphabetWidth + firstLetterPosition.second * alphabetWidth] to
            alphabet[(secondLetterPosition.first + 1 + alphabetWidth) % alphabetWidth + secondLetterPosition.second * alphabetWidth]
        } else {
            first to extraLetter
        }
    }

    private fun decodeLetterPair(first: Char, second: Char): Pair<Char, Char> {
        val key = binding.inputPlayfairKey.text.toString()

        val alphabet = rectangularCyrillicAlphabet.getKeyedAlphabet(key) ?: rectangularCyrillicAlphabet

        if (second == extraLetter) {
            return first to first
        }

        val firstLetterPosition = getLetterPosition(first, alphabet);
        val secondLetterPosition = getLetterPosition(second, alphabet);

        var newFirstPosition: Pair<Int, Int> = 0 to 0;
        var newSecondPosition: Pair<Int, Int> = 0 to 0;

        return if (firstLetterPosition.first != secondLetterPosition.first && firstLetterPosition.second != secondLetterPosition.second) {
            alphabet[secondLetterPosition.first + firstLetterPosition.second * alphabetWidth] to
            alphabet[firstLetterPosition.first + secondLetterPosition.second * alphabetWidth]
        } else if (firstLetterPosition.first == secondLetterPosition.first && firstLetterPosition.second != secondLetterPosition.second) {
            alphabet[firstLetterPosition.first + (firstLetterPosition.second - 1 + alphabetHeight) % alphabetHeight * alphabetWidth] to
            alphabet[secondLetterPosition.first + (secondLetterPosition.second - 1 + alphabetHeight) % alphabetHeight * alphabetWidth]
        } else if (firstLetterPosition.first != secondLetterPosition.first && firstLetterPosition.second == secondLetterPosition.second) {
            alphabet[(firstLetterPosition.first - 1 + alphabetWidth) % alphabetWidth + firstLetterPosition.second * alphabetWidth] to
            alphabet[(secondLetterPosition.first - 1 + alphabetWidth) % alphabetWidth + secondLetterPosition.second * alphabetWidth]
        } else {
            first to extraLetter
        }
    }

    override fun encode(source: String): String {
        val key: String = binding.inputPlayfairKey.text.toString()
        if (!alphabetSourceString.toList().containsAll((source + key).toList() ))
        {
            Toast.makeText(this.context, "Only lowcase cyrillic symbols", Toast.LENGTH_SHORT).show()
            return ""
        }

        var result = "";
        for (i in source.indices) {
            if (i % 2 == 0 && i != source.length - 1) {
                val encodedPair = encodeLetterPair(source[i], source[i+1])
                result += encodedPair.first
                result += encodedPair.second
            }
        }
        return result
    }

    override fun decode(source: String): String {
        val key: String = binding.inputPlayfairKey.text.toString()
        if (!alphabetSourceString.toList().containsAll((source + key).toList() ))
        {
            Toast.makeText(this.context, "Only lowcase cyrillic symbols", Toast.LENGTH_SHORT).show()
            return ""
        }

        var result = "";
        for (i in source.indices) {
            if (i % 2 == 0 && i != source.length - 1) {
                val encodedPair = decodeLetterPair(source[i], source[i+1])
                result += encodedPair.first
                result += encodedPair.second
            }
        }
        return result
    }
}

