package com.example.basicencoder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.basicencoder.databinding.FragmentCaesarCipherBinding
import com.example.basicencoder.databinding.FragmentCombinedCipherBinding
import com.example.basicencoder.utils.Alphabet
import com.example.basicencoder.utils.cyrillicLowerCaseAlphabet

class CombinedCipherFragment : BasicCipherFragment() {
    override val innerLayoutId: Int = R.layout.fragment_combined_cipher

    override val title: String = "Combined cipher"

    private var _binding: FragmentCombinedCipherBinding? = null
    private val binding get() = _binding!!

    override fun getBindedParamsView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCombinedCipherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getMinKeySize(alphabetSize: Int): Int {
        for (i in 0..9) {
            if (Math.ceil((alphabetSize - i).toDouble() / 10) > 10 - i) {
                return i - 1;
            }
        }
        return 9;
    }

    override fun encode(source: String): String {
        val key = binding.inputCombinedKey.text.toString()

        val minKeySize = getMinKeySize(cyrillicLowerCaseAlphabet.size)
        if (key.length > minKeySize) {
            return "Key should be shorter than " + (minKeySize + 1) + " symbols.";
        }

        val alphabetWithoutKeyword = cyrillicLowerCaseAlphabet
                .getAsCharArray()
                .filter { letter -> !key.toList().contains(letter) }

        var result = ""

        println(source)

        val nums = intArrayOf(0, 9, 8, 7, 6, 5, 4, 3, 2, 1)

        for (element in source) {
            if (key.toList().contains(element)) {
                result += nums[key.indexOf(element)].toString();
            } else {
                val cIndex = alphabetWithoutKeyword.indexOf(element)

                result += ((cIndex / 10) * 10 + nums[cIndex % 10] + 10).toString()
            }
        }

        return result;
    }

    override fun decode(source: String): String {
        return source
    }
}