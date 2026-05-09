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
import com.dailies.MainActivity

class HabitWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val colors = ColorProviders(context)
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(colors.primaryContainer)
                    .clickable(actionStartActivity<MainActivity>())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dailies",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = colors.onPrimaryContainer
                    )
                )
                Text(
                    text = "Keep up your streaks!",
                    style = TextStyle(
                        color = colors.onPrimaryContainer
                    )
                )
            }
        }
    }
}
