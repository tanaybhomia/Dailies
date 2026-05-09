package com.dailies.widget

import android.content.Context
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.text.FontWeight
import androidx.glance.material3.ColorProviders
import android.content.ComponentName
import androidx.compose.ui.graphics.Color
import androidx.glance.unit.ColorProvider
import com.dailies.MainActivity

class HabitWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val primaryContainer = ColorProvider(Color(0xFFEADDFF))
            val onPrimaryContainer = ColorProvider(Color(0xFF21005D))
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(primaryContainer)
                    .clickable(actionStartActivity(ComponentName(context, MainActivity::class.java)))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dailies",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = onPrimaryContainer
                    )
                )
                Text(
                    text = "Keep up your streaks!",
                    style = TextStyle(
                        color = onPrimaryContainer
                    )
                )
            }
        }
    }
}
