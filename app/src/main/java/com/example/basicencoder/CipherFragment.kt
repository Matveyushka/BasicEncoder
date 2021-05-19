package com.example.basicencoder

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import com.example.basicencoder.databinding.FragmentCipherBinding
import java.lang.Exception


class CipherFragment : Fragment() {
    private val usedCipherModel: UsedCipherModel by activityViewModels()

    private var _binding: FragmentCipherBinding? = null
    private val binding get() = _binding!!

    private val argumentValueGetters: MutableList<() -> Pair<String, Any>> = mutableListOf()

    private fun buildArgumentsLayout(table: TableLayout) {
        for (argument in usedCipherModel.arguments) {
            val argumentRow = TableRow(context)
            argumentRow.layoutParams = ViewGroup.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )

            val argumentLabel = TextView(context)
            argumentLabel.text = argument.key

            val argumentInput = EditText(context)
            val params = TableRow.LayoutParams()
            params.weight = 1.0f
            argumentInput.layoutParams = params
            if (argument.value == String::class.java) {
                argumentInput.inputType = InputType.TYPE_CLASS_TEXT
                argumentValueGetters.add {
                    val input = argumentInput.text.toString()

                    if (input.isEmpty() && !usedCipherModel.defaultArguments.containsKey(argument.key)) {
                        throw IllegalArgumentException("Key ${argument.key} cannot be empty")
                    } else if (usedCipherModel.cipher?.alphabet?.isStringInAlphabet(input) == true) {
                        argument.key to input
                    } else {
                        throw IllegalArgumentException("Illegal symbols")
                    }
                }
            } else if (argument.value == Int::class.java) {
                argumentInput.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                argumentValueGetters.add { argument.key to argumentInput.text.toString().toInt() }
            } else if (argument.value == Any::class.java) {
                argumentInput.inputType = InputType.TYPE_CLASS_TEXT
                argumentValueGetters.add { argument.key to argumentInput.text.toString() }
            } else {
                throw IllegalArgumentException("The behavior for ${argument.value} argument is not defined")
            }

            argumentInput.setText(usedCipherModel.defaultArguments[argument.key]?.toString() ?: "")

            argumentRow.addView(argumentLabel)
            argumentRow.addView(argumentInput)
            table.addView(argumentRow)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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
            usedCipherModel.defaultArguments
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

                if (usedCipherModel.cipher?.alphabet?.isStringInAlphabet(input) == true) {
                    encodedMessage = usedCipherModel.cipher?.encode(
                        input,
                        arguments
                    )
                } else {
                    throw Exception("Illegal symbols in message input")
                }
            } catch (exception: Exception) {
                Toast.makeText(context, exception.message, LENGTH_SHORT).show()
            }

            binding.outputPlace.text = encodedMessage

            usedCipherModel.visualizer?.showEncode(
                view.findViewById(R.id.visualize_place),
                binding.inputPlace.text.toString(),
                encodedMessage ?: "",
                usedCipherModel.defaultArguments
            )
        }

        binding.decodeButton.setOnClickListener{
            val arguments = argumentValueGetters.map { getter -> getter() }.toMap()

            var decodedMessage: String? = null

            try {
                decodedMessage = usedCipherModel.cipher?.decode(
                    binding.inputPlace.text.toString(),
                    arguments
                )
            } catch (exception: Exception) {
                Toast.makeText(context, exception.message, LENGTH_SHORT).show()
            }

            binding.outputPlace.text = decodedMessage

            usedCipherModel.visualizer?.showDecode(
                view.findViewById(R.id.visualize_place),
                binding.inputPlace.text.toString(),
                decodedMessage ?: "",
                usedCipherModel.defaultArguments
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