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
import cz.muni.fi.pv239.juliajamnicka.playlyst.MainActivity
import cz.muni.fi.pv239.juliajamnicka.playlyst.R
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

    private lateinit var pickedAttributes: MutableMap<String, MoodAttribute>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoodAddEditBinding.inflate(layoutInflater, container, false)

        setUpAppBar()

        val mainActivity = requireActivity() as MainActivity
        mainActivity.setBottomNavigationVisibility(View.GONE)

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

        binding.nameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.isEmpty() == false) {
                    binding.nameInput.error = null
                }
            }

        })

        binding.colorEditText.setOnFocusChangeListener { _, hasFocus ->
            val color = "#${binding.colorEditText.text}"

            if (!hasFocus && isColorValid(color)) {
                binding.colorInput.error = null
                binding.colorWheel.imageTintList =
                    ColorStateList.valueOf(Color.parseColor(color))
            }
        }

        binding.colorWheel.setOnClickListener {
            val hexColor = RandomColorUtil.getRandomHexColor()

            binding.colorInput.error = null

            binding.colorEditText.setText(hexColor)
            binding.colorWheel.imageTintList =
                ColorStateList.valueOf(Color.parseColor("#$hexColor"))
        }

        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val color = "#${binding.colorEditText.text.toString()}"

            if (isMoodValid(name, color)) {
                val mood = moodRepository.saveOrUpdate(name, color, getSortedAttributes(),
                    args.mood?.id)

                val navController = findNavController()
                navController.previousBackStackEntry?.savedStateHandle?.set("mood", mood)
                findNavController().popBackStack()
            }
        }

    }

    private fun setUpAppBar() {
        val mainActivity = requireActivity() as MainActivity
        mainActivity.title = mainActivity.getString(if (args.mood !== null)
            R.string.edit_mood_title else R.string.create_mood_title)
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

    private fun isMoodValid(name: String, hexColor: String): Boolean {
        if (name.isEmpty()) {
            binding.nameInput.error = getString(R.string.empty_field)
            return false
        }
        return isColorValid(hexColor)
    }

    private fun isColorValid(hexColor: String): Boolean
    {
        try {
            Color.parseColor(hexColor)
        } catch (_: java.lang.IllegalArgumentException) {
            binding.colorInput.error = getString(R.string.invalid_color_input)
            return false
        }
        return true
    }

    private fun refreshList() {
        adapter.submitList(getSortedAttributes())
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

    override fun onDestroyView() {
        super.onDestroyView()
        val mainActivity = requireActivity() as MainActivity
        mainActivity.setBottomNavigationVisibility(View.VISIBLE)
    }
}