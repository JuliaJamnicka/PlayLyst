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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cz.muni.fi.pv239.juliajamnicka.playlyst.data.*
import cz.muni.fi.pv239.juliajamnicka.playlyst.databinding.FragmentMoodAddEditBinding
import cz.muni.fi.pv239.juliajamnicka.playlyst.repository.MoodRepository


class MoodAddEditFragment : Fragment() {
    private lateinit var binding: FragmentMoodAddEditBinding

    private val moodRepository: MoodRepository by lazy {
        MoodRepository(requireContext())
    }

    private val adapter : MoodAttributesAdapter by lazy {
        MoodAttributesAdapter(
            onItemClick = { moodAttribute -> {}
            }
        )
    }
    private val args: MoodAddEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoodAddEditBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        setInitialValues()

        binding.colorEditText.setOnFocusChangeListener { view, hasFocus ->
            val text = binding.colorEditText.text
            if (!hasFocus && text?.isNotEmpty() == true) {
                binding.colorInput.setStartIconTintList(
                    ColorStateList.valueOf(Color.parseColor("#$text"))
                )
            }
        }

        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val color = binding.colorEditText.text.toString()

        }

    }

    private fun setInitialValues() {
        val mood = args.mood

        if (mood == null) {
            adapter.submitList(createDefaultAttributesList())
        } else {
            adapter.submitList(mood.attributes)
            binding.colorEditText.setText(mood.color)
            binding.nameEditText.setText(mood.name)
        }
    }

    private fun createDefaultAttributesList(): List<MoodAttribute> {
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
        }
    }


}