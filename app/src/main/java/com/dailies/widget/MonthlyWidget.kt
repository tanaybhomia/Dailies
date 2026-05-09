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
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.text.FontWeight
import androidx.glance.material3.ColorProviders
import com.dailies.MainActivity

class MonthlyWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val colors = ColorProviders(context)
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(colors.surfaceVariant)
                    .clickable(actionStartActivity<MainActivity>())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "This Month",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurfaceVariant
                    ),
                    modifier = GlanceModifier.padding(bottom = 8.dp)
                )
                
                Column(modifier = GlanceModifier.fillMaxWidth()) {
                    for (row in 0 until 5) {
                        Row(modifier = GlanceModifier.fillMaxWidth().padding(vertical = 2.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            for (col in 0 until 7) {
                                val isActive = (row * 7 + col) % 3 == 0
                                Box(
                                    modifier = GlanceModifier
                                        .size(12.dp)
                                        .background(if (isActive) colors.primary else colors.secondaryContainer)
                                ) {}
                                Spacer(modifier = GlanceModifier.width(4.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
