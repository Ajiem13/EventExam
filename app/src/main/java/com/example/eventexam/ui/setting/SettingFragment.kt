package com.example.eventexam.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.eventexam.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

        val pref = SettingPreferences.getInstance(requireActivity().applicationContext.datasore)
        val viewModel = ViewModelProvider(requireActivity(),SettingViewModelFactory(pref)).get(SettingViewModel::class.java)

        viewModel.getThemeSetting().observe(requireActivity()) { isDarkModeActive : Boolean ->
            if(isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchBar.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchBar.isChecked = false
            }
        }

        binding.switchBar.setOnCheckedChangeListener{ _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }

        return binding.root
    }

}