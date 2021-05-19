package com.example.basicencoder

import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.basicencoder.cipher.*
import com.example.basicencoder.cipher.visualizer.IVisualizer
import com.example.basicencoder.cipher.visualizer.MagicSquareVisualize
import java.lang.reflect.Type

class UsedCipherModel(
    var title: String? = null,
    var cipher: ICipher? = null,
    var visualizer: IVisualizer? = null
) : ViewModel() {}

class FirstFragment : Fragment() {
    val usedCipherModel: UsedCipherModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ciphers: List<UsedCipherModel> = listOf(
            UsedCipherModel(
                "Caesar cipher",
                Caesar
            ),
            UsedCipherModel(
                "Magic square cipher",
                MagicSquare,
                MagicSquareVisualize
            ),
            UsedCipherModel(
                "Trithemus",
                Trithemus
            ),
            UsedCipherModel(
                "Vigenere",
                Vigenere
            ),
            UsedCipherModel(
                "Combined cipher",
                CombinedCipher
            ),
            UsedCipherModel(
                "Port cipher",
                Port
            ),
            UsedCipherModel(
                "Playfair cipher",
                Playfair
            ),
            UsedCipherModel (
                "Feistel cipher",
                Feistel
            ),
            UsedCipherModel (
                "Matthew cipher",
                Matthew
            )
        )

        val mainLayout = view.findViewById<LinearLayout>(R.id.main_layout)

        ciphers.forEach { cipher ->
            val button = Button(context)
            button.setOnClickListener {
                usedCipherModel.title = cipher.title
                usedCipherModel.cipher = cipher.cipher
                usedCipherModel.visualizer = cipher.visualizer
                findNavController().navigate(R.id.action_FirstFragment_to_cipherFragment)
            }
            button.text = cipher.title
            mainLayout.addView(button)
        }
    }
}