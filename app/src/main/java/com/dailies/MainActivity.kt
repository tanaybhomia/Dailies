package com.dailies

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.dailies.ui.screens.HomeScreen
import com.dailies.ui.theme.DailiesTheme
import com.dailies.viewmodel.HabitViewModel
import com.dailies.viewmodel.HabitViewModelFactory
import com.dailies.worker.NotificationWorker
import java.util.Calendar

class MainActivity : ComponentActivity() {

    private val habitViewModel: HabitViewModel by viewModels {
        HabitViewModelFactory((application as DailiesApplication).repository)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            NotificationWorker.scheduleDailyReminder(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Reset daily progress on open
        habitViewModel.resetDailyProgress()

        // Ask for notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                NotificationWorker.scheduleDailyReminder(this)
            }
        } else {
            NotificationWorker.scheduleDailyReminder(this)
        }

        setContent {
            DailiesTheme {
                HomeScreen(viewModel = habitViewModel)
            }
        }
    }
}
