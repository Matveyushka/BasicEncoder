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

    private fun validateUsedCipherModel() {
        if (usedCipherModel.encode == null || usedCipherModel.decode == null) {
            throw IllegalArgumentException("Encode and Decode functions must be provided to the Cipher Fragment")
        }
    }

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
            } else if (argument.value == Int::class.java) {
                argumentInput.inputType =  InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
            } else {
                throw IllegalArgumentException("Teh behavior for ${argument.value} argument is not defined")
            }

            argumentInput.setText(usedCipherModel.defaultArguments[argument.key]?.toString() ?: "")

            argumentValueGetters.add { argument.key to argumentInput.text.toString() }

            argumentRow.addView(argumentLabel)
            argumentRow.addView(argumentInput)
            table.addView(argumentRow)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentCipherBinding.inflate(inflater, container, false)

        val view = binding.root

        validateUsedCipherModel()

        binding.cipherTtitle.text = usedCipherModel.title ?: "<NO TITLE>"

        val argumentsLayout = view.findViewById<TableLayout>(R.id.argumentsLayout)

        buildArgumentsLayout(argumentsLayout)

        val visualizeLayout = view.findViewById<ConstraintLayout>(R.id.visualize_place)

        usedCipherModel.visualize?.invoke(
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
            val arguments = argumentValueGetters.map { getter -> getter() }.toMap()

            var encodedMessage: String? = null

            try {
                encodedMessage = usedCipherModel.encode?.invoke(
                    binding.inputPlace.text.toString(),
                    arguments
                )
            } catch (exception: Exception) {
                Toast.makeText(context, exception.message, LENGTH_SHORT).show()
            }

            binding.outputPlace.setText(encodedMessage)

            usedCipherModel.visualize?.invoke(
                view.findViewById<ConstraintLayout>(R.id.visualize_place),
                binding.inputPlace.text.toString(),
                encodedMessage ?: "",
                usedCipherModel.defaultArguments
            )
        }

        binding.decodeButton.setOnClickListener{
            val arguments = argumentValueGetters.map { getter -> getter() }.toMap()

            var decodedMessage: String? = null

            try {
                decodedMessage = usedCipherModel.decode?.invoke(
                    binding.inputPlace.text.toString(),
                    arguments
                )
            } catch (exception: Exception) {
                Toast.makeText(context, exception.message, LENGTH_SHORT).show()
            }

            binding.outputPlace.setText(decodedMessage)

            usedCipherModel.visualize?.invoke(
                view.findViewById<ConstraintLayout>(R.id.visualize_place),
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