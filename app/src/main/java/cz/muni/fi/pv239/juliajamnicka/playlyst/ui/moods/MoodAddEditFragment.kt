package cz.muni.fi.pv239.juliajamnicka.playlyst.ui.moods

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.*
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentMoodAddEditBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.MoodRepository
import cz.muni.fi.pv239.juliajamnicka.playlyst.util.RandomColorUtil


class MoodAddEditFragment : Fragment() {
    private lateinit var binding: FragmentMoodAddEditBinding

    private val moodRepository: MoodRepository by lazy {
        MoodRepository(requireContext())
    }

    private val adapter : MoodAttributesAdapter by lazy {
        MoodAttributesAdapter(
            onItemClick = { _ -> {}
            },
            onSliderChange = { moodAttribute, value, lowerValue, upperValue ->
                saveChangedValues(moodAttribute, value, lowerValue, upperValue)
            }
        )
    }
    private val args: MoodAddEditFragmentArgs by navArgs()

    private val pickedAttributes: MutableList<MoodAttribute> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoodAddEditBinding.inflate(layoutInflater, container, false)

        for (attribute in args.mood?.attributes ?: emptyList()) {
            pickedAttributes.add(attribute.copy())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        setInitialValues()

        binding.colorEditText.setOnFocusChangeListener { _, hasFocus ->
            val text = binding.colorEditText.text
            if (!hasFocus && text?.isNotEmpty() == true) {
                binding.colorWheel.imageTintList =
                    ColorStateList.valueOf(Color.parseColor("#$text"))
            }
        }

        binding.colorWheel.setOnClickListener {
            val hexColor = RandomColorUtil.getRandomHexColor()

            binding.colorEditText.setText(hexColor)
            binding.colorWheel.imageTintList =
                ColorStateList.valueOf(Color.parseColor("#$hexColor"))
        }

        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val color = "#${binding.colorEditText.text.toString()}"

            moodRepository.saveOrUpdate(name, color, pickedAttributes, args.mood?.id)
            findNavController().popBackStack()
        }

    }

    private fun setInitialValues() {
        val mood = args.mood

        if (mood == null) {
            adapter.submitList(createDefaultAttributesList())
        } else {
            adapter.submitList(mood.attributes)
            binding.colorEditText.setText(mood.color.drop(1))
            binding.colorWheel.imageTintList = ColorStateList.valueOf(Color.parseColor(mood.color))
            binding.nameEditText.setText(mood.name)
        }
    }

    private fun createDefaultAttributesList(): MutableList<MoodAttribute> {
        return MoodAttributeType.values().map {
            val thresholds: AttributeThresholds = it.getThresholds()
            MoodAttribute(
                id = 0,
                moodId = 0,
                name = it.name,
                minValue = thresholds.minValue,
                maxValue = thresholds.maxValue,
                stepSize = thresholds.stepSize,
                canHaveRange = thresholds.canHaveRange,
                defaultValue = thresholds.defaultValue,
                lowerDefaultValue = thresholds.lowerDefaultValue,
                upperDefaultValue = thresholds.upperDefaultValue
            )
        }.toMutableList()
    }

    private fun saveChangedValues(moodAttribute: MoodAttribute, value: Float?,
        lowerValue: Float?, upperValue: Float?) {
        val changedAttribute =
            moodAttribute.copyNewWithChangedValues(value, lowerValue, upperValue)

        if (pickedAttributes.find { it.name === changedAttribute.name } === null) {
            pickedAttributes.add(changedAttribute)
        } else {
            pickedAttributes.replaceAll {
                if (it.name === changedAttribute.name) changedAttribute else it  }
        }
    }


}