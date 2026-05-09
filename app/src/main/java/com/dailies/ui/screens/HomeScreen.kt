package com.dailies.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailies.ui.components.HabitCard
import com.dailies.viewmodel.HabitViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HabitViewModel) {
    val habits by viewModel.allHabits.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Dailies") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(habits) { habit ->
                HabitCard(
                    habit = habit,
                    onCompleteClick = {
                        viewModel.markHabitCompleted(habit)
                    }
                )
            }
        }
    }
}
