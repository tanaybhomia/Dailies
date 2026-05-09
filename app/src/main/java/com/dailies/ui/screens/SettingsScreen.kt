package com.dailies.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.dailies.data.Habit
import com.dailies.data.SettingsManager
import com.dailies.viewmodel.HabitViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(settingsManager: SettingsManager, viewModel: HabitViewModel) {
    val habits by viewModel.allHabits.collectAsState()
    val context = LocalContext.current

    var highlightDay by remember { mutableStateOf(settingsManager.highlightCurrentDay) }
    var showStreak by remember { mutableStateOf(settingsManager.showStreakCount) }
    var showMonth by remember { mutableStateOf(settingsManager.showMonthLabels) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Preferences", style = MaterialTheme.typography.titleLarge)
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Highlight Current Day")
                Switch(
                    checked = highlightDay,
                    onCheckedChange = {
                        highlightDay = it
                        settingsManager.highlightCurrentDay = it
                    }
                )
            }
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Show Streak Count on Cards")
                Switch(
                    checked = showStreak,
                    onCheckedChange = {
                        showStreak = it
                        settingsManager.showStreakCount = it
                    }
                )
            }
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Show Month Labels")
                Switch(
                    checked = showMonth,
                    onCheckedChange = {
                        showMonth = it
                        settingsManager.showMonthLabels = it
                    }
                )
            }
            
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            
            Text("Data Management", style = MaterialTheme.typography.titleLarge)
            
            Button(
                onClick = { exportCsv(context, habits) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Export Habits as CSV")
            }
        }
    }
}

private fun exportCsv(context: Context, habits: List<Habit>) {
    val csvHeader = "ID,Name,Description,Target,Current,Streak,Total Completions,Tags\n"
    val csvData = habits.joinToString(separator = "\n") {
        "${it.id},\"${it.name}\",\"${it.description}\",${it.targetCompletions},${it.currentCompletions},${it.streak},${it.totalCompletions},\"${it.tags.joinToString(",")}\""
    }
    
    val file = File(context.cacheDir, "habits_export.csv")
    file.writeText(csvHeader + csvData)
    
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "text/csv"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    
    val shareIntent = Intent.createChooser(sendIntent, "Export CSV")
    context.startActivity(shareIntent)
}
