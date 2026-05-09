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
import android.content.ComponentName
import androidx.compose.ui.graphics.Color
import androidx.glance.unit.ColorProvider
import com.dailies.MainActivity

class YearlyWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val surfaceVariant = ColorProvider(Color(0xFFE7E0EC))
            val onSurfaceVariant = ColorProvider(Color(0xFF49454F))
            val primary = ColorProvider(Color(0xFF6750A4))
            val secondaryContainer = ColorProvider(Color(0xFFE8DEF8))
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(surfaceVariant)
                    .clickable(actionStartActivity(ComponentName(context, MainActivity::class.java)))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Year in Review",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = onSurfaceVariant
                    ),
                    modifier = GlanceModifier.padding(bottom = 8.dp)
                )
                
                Row(modifier = GlanceModifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    for (col in 0 until 35) { // Fits better in 4x2
                        Column(modifier = GlanceModifier.padding(horizontal = 1.dp)) {
                            for (row in 0 until 7) {
                                val isActive = (row * col) % 5 == 0
                                Box(
                                    modifier = GlanceModifier
                                        .size(6.dp)
                                        .background(if (isActive) primary else secondaryContainer)
                                ) {}
                                Spacer(modifier = GlanceModifier.height(2.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
