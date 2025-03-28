package com.example.eventexam.ui.setting

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.eventexam.databinding.FragmentSettingBinding
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding

    private val registerPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        val pref = SettingPreferences.getInstance(requireActivity().applicationContext.datastore)
        val viewModel = ViewModelProvider(requireActivity(), SettingViewModelFactory(pref))
            .get(SettingViewModel::class.java)

        viewModel.getThemeSetting().observe(viewLifecycleOwner) { isDarkModeActive ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
            binding.switchBar.isChecked = isDarkModeActive
        }

        binding.switchBar.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
        }

        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isReminderActive = sharedPreferences.getBoolean("daily_reminder", false)
        binding.switchBar2.isChecked = isReminderActive

        binding.switchBar2.setOnCheckedChangeListener { _, isChecked ->
            setDailyReminder(isChecked)
            sharedPreferences.edit().putBoolean("daily_reminder", isChecked).apply()

            val message = if (isChecked) "Daily Reminder Aktif!" else "Daily Reminder Dinonaktifkan!"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= 33) {
            registerPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun setDailyReminder(enable: Boolean) {
        val workManager = WorkManager.getInstance(requireContext())

        if (enable) {
            val workRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build()

            workManager.enqueueUniquePeriodicWork(
                "DailyReminderWork",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
        } else {
            workManager.cancelUniqueWork("DailyReminderWork")
        }
    }
}
