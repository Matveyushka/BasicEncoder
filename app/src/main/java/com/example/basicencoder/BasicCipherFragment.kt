package com.example.basicencoder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.basicencoder.databinding.FragmentBasicCipherBinding
import org.w3c.dom.Text

abstract class BasicCipherFragment : Fragment() {
    abstract val innerLayoutId: Int
    abstract val title: String

    private var _binding: FragmentBasicCipherBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentBasicCipherBinding.inflate(inflater, container, false)
        val view = binding.root

        view.findViewById<TextView>(R.id.cipherTitle).text = title

        val layout = getBindedParamsView(inflater, container, savedInstanceState)

        val constraintLayout = view.findViewById<FrameLayout>(R.id.basicCipherPlaceholder)
        constraintLayout.addView(layout)

        binding.buttonEncode.setOnClickListener {
            binding.outputCipher.text = encode(binding.inputCipher.text.toString())
        }

        binding.buttonDecode.setOnClickListener {
            binding.outputCipher.text = decode(binding.inputCipher.text.toString())
        }

        binding.buttonSwap.setOnClickListener {
            binding.inputCipher.setText(view.findViewById<TextView>(R.id.outputCipher).text.toString())
            binding.outputCipher.text = ""
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun getBindedParamsView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?

    abstract fun encode(source: String): String

    abstract fun decode(source: String): String
}