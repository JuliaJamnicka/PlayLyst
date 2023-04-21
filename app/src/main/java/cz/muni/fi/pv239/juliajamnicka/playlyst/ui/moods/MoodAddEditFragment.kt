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
            onSliderChange = { changedAttribute ->
                pickedAttributes[changedAttribute.name] = changedAttribute
                adapter.submitList(getSortedAttributes())
            }
        )
    }
    private val args: MoodAddEditFragmentArgs by navArgs()

    private lateinit var pickedAttributes: MutableMap<String, MoodAttribute>;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoodAddEditBinding.inflate(layoutInflater, container, false)

        val attributes = args.mood?.attributes ?: createDefaultAttributesList()
        pickedAttributes = attributes
            .sortedBy { it.name }
            .associate { Pair(it.name, it.copy()) }
            .toMutableMap()

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

            moodRepository.saveOrUpdate(name, color, getSortedAttributes(),
                args.mood?.id)
            findNavController().popBackStack()
        }

    }

    private fun setInitialValues() {
        val mood = args.mood
        adapter.submitList(getSortedAttributes())

        if (mood !== null) {
            binding.colorEditText.setText(mood.color.drop(1))
            binding.colorWheel.imageTintList = ColorStateList.valueOf(Color.parseColor(mood.color))
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
                upperDefaultValue = thresholds.upperDefaultValue,
                value = thresholds.defaultValue
            )
        }
    }

    private fun refreshList() {
        val before = adapter.itemCount
        adapter.submitList(getSortedAttributes())
        val after = adapter.itemCount
        assert(before == after)
    }

    private fun getSortedAttributes(): List<MoodAttribute> {
        return pickedAttributes
            .values
            .toList()
            .sortedBy { it.name }
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }
}