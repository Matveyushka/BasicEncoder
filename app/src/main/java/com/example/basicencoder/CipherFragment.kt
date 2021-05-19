package com.example.basicencoder

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.basicencoder.databinding.FragmentCipherBinding
import com.example.basicencoder.utils.alert
import com.example.basicencoder.utils.isStringInAlphabets


class CipherFragment : Fragment() {
    private val usedCipherModel: UsedCipherModel by activityViewModels()

    private var _binding: FragmentCipherBinding? = null
    private val binding get() = _binding!!

    private val argumentValueGetters: MutableList<() -> Pair<String, Any>> = mutableListOf()

    private fun buildArgumentsLayout(table: TableLayout) {
        for (keyDescription in usedCipherModel.cipher?.keyDescriptions!!) {
            val argumentRow = TableRow(context)
            argumentRow.layoutParams = ViewGroup.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )

            val argumentLabel = TextView(context)
            argumentLabel.text = keyDescription.name

            val argumentInput = EditText(context)
            val params = TableRow.LayoutParams()
            params.weight = 1.0f
            argumentInput.layoutParams = params
            when (keyDescription.type) {
                String::class.java -> {
                    argumentInput.inputType = InputType.TYPE_CLASS_TEXT
                }
                Int::class.java -> {
                    argumentInput.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                }
                Any::class.java -> {
                    argumentInput.inputType = InputType.TYPE_CLASS_TEXT
                }
                else -> {
                    throw IllegalArgumentException("The behavior for ${keyDescription.type} argument is not defined")
                }
            }
            argumentValueGetters.add {
                val input = argumentInput.text.toString()
                if (input.isEmpty() && keyDescription.defaultValue == null) {
                    throw IllegalArgumentException("Key ${keyDescription.name} cannot be empty")
                } else if (input.isEmpty()) {
                    keyDescription.name to keyDescription.defaultValue!!
                } else if (keyDescription.alphabets?.isStringInAlphabets(input) != false) {
                    keyDescription.name to input
                } else {
                    throw IllegalArgumentException("Illegal symbols")
                }
            }
            argumentInput.setText(keyDescription.defaultValue?.toString() ?: "")

            argumentRow.addView(argumentLabel)
            argumentRow.addView(argumentInput)
            table.addView(argumentRow)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentCipherBinding.inflate(inflater, container, false)

        val view = binding.root

        binding.cipherTtitle.text = usedCipherModel.title ?: "<NO TITLE>"

        val argumentsLayout = view.findViewById<TableLayout>(R.id.argumentsLayout)

        buildArgumentsLayout(argumentsLayout)

        val visualizeLayout = view.findViewById<ConstraintLayout>(R.id.visualize_place)

        usedCipherModel.visualizer?.showEncode(
            visualizeLayout,
            "",
            "",
            (usedCipherModel.cipher?.keyDescriptions!!.map { keyDescription ->
                keyDescription.name to (keyDescription.defaultValue.toString() ?: "")
            }).toMap()
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.encodeButton.setOnClickListener{
            var encodedMessage: String? = null

            try {
                val arguments = argumentValueGetters.map { getter -> getter() }.toMap()

                val input = binding.inputPlace.text.toString()

                if (usedCipherModel.cipher?.encodeAlphabets?.isStringInAlphabets(input) == true) {
                    encodedMessage = usedCipherModel.cipher?.encode(
                        input,
                        arguments
                    )
                } else {
                    throw Exception("Illegal symbols in message input")
                }
            } catch (exception: Exception) {
                alert(exception.message ?: "", this.requireContext())
            }

            binding.outputPlace.text = encodedMessage

            usedCipherModel.visualizer?.showEncode(
                view.findViewById(R.id.visualize_place),
                binding.inputPlace.text.toString(),
                encodedMessage ?: "",
                argumentValueGetters.map { getter -> getter() }.toMap()
            )
        }

        binding.decodeButton.setOnClickListener{
            var decodedMessage: String? = null

            try {
                val arguments = argumentValueGetters.map { getter -> getter() }.toMap()

                val input = binding.inputPlace.text.toString()

                if (usedCipherModel.cipher?.decodeAlphabets?.isStringInAlphabets(input) == true) {
                    decodedMessage = usedCipherModel.cipher?.decode(
                        binding.inputPlace.text.toString(),
                        arguments
                    )
                } else {
                    throw Exception("Illegal symbols in message input")
                }
            } catch (exception: Exception) {
                alert(exception.message ?: "", this.requireContext())
            }

            binding.outputPlace.text = decodedMessage

            usedCipherModel.visualizer?.showDecode(
                view.findViewById(R.id.visualize_place),
                binding.inputPlace.text.toString(),
                decodedMessage ?: "",
                argumentValueGetters.map { getter -> getter() }.toMap()
            )
        }

        binding.swapButton.setOnClickListener {
            binding.inputPlace.setText(view.findViewById<TextView>(R.id.outputPlace).text.toString())
            binding.outputPlace.text = ""
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}