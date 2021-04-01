package com.example.basicencoder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.basicencoder.databinding.FragmentCaesarCipherBinding
import com.example.basicencoder.databinding.FragmentTranspositionCipherBinding

class TranspositionCipherFragment : BasicCipherFragment() {
    override val innerLayoutId: Int = R.layout.fragment_transposition_cipher
    override val title: String = "Transposition cipher"

    private var _binding: FragmentTranspositionCipherBinding? = null
    private val binding get() = _binding!!

    override fun getBindedParamsView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTranspositionCipherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun encode(source: String): String {
        return (source.mapIndexed { index, symbol ->
            if (index % 2 == 0) {
                if (source.length > index + 1) source[index + 1] else symbol
            } else {
                source[index - 1]
            }
        }).joinToString("")
    }

    override fun decode(source: String): String {
        return encode(source)
    }
}