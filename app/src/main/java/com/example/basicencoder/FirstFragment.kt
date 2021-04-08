package com.example.basicencoder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.to_transposition_button).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_TranspositionCipherFragment)
        }

        view.findViewById<Button>(R.id.to_caesar_button).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_CaesarCipherFragment)
        }

        view.findViewById<Button>(R.id.to_vigenere_button).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_VigenereCipherFragment)
        }

        view.findViewById<Button>(R.id.to_magic_square_button).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_MagicSquareCipherFragment)
        }

        view.findViewById<Button>(R.id.to_trithemus_button).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_TrithemusCipherFragment)
        }

        view.findViewById<Button>(R.id.to_playfair_button).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_PlayfairCipherFragment)
        }

        view.findViewById<Button>(R.id.to_combined_button).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_CombinedCipherFragment)
        }
    }
}