package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IslamicGoldAccent
import com.example.ui.theme.IslamicGoldPrimary
import kotlin.math.cos
import kotlin.math.sin

/**
 * 8-Pointed Islamic Star (Rub El Hizb / خاتم سليمان)
 */
@Composable
fun IslamicEightPointStar(
    modifier: Modifier = Modifier,
    color: Color = IslamicGoldAccent,
    strokeWidth: Float = 2f,
    filled: Boolean = false
) {
    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2f
        val center = Offset(size.width / 2f, size.height / 2f)

        fun getSquarePath(angleOffsetRad: Double): Path {
            val path = Path()
            val points = (0..3).map { i ->
                val angle = angleOffsetRad + (i * Math.PI / 2.0)
                Offset(
                    x = (center.x + radius * cos(angle)).toFloat(),
                    y = (center.y + radius * sin(angle)).toFloat()
                )
            }
            path.moveTo(points[0].x, points[0].y)
            points.drop(1).forEach { path.lineTo(it.x, it.y) }
            path.close()
            return path
        }

        val path1 = getSquarePath(0.0)
        val path2 = getSquarePath(Math.PI / 4.0)

        if (filled) {
            drawPath(path1, color = color)
            drawPath(path2, color = color)
        } else {
            drawPath(path1, color = color, style = Stroke(width = strokeWidth))
            drawPath(path2, color = color, style = Stroke(width = strokeWidth))
            drawCircle(color = color, radius = radius * 0.25f, style = Stroke(width = strokeWidth))
        }
    }
}

/**
 * Classical Quranic Ayah End Badge (علامة نهاية الآية)
 */
@Composable
fun AyahEndNumberBadge(
    ayahNumber: Int,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 34.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(sizeDp)
    ) {
        IslamicEightPointStar(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.85f),
            strokeWidth = 3f,
            filled = false
        )
        Text(
            text = "$ayahNumber",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Islamic Decorative Horizontal Divider with Central Medallion
 */
@Composable
fun IslamicOrnamentDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, color)
                    )
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        IslamicEightPointStar(
            modifier = Modifier.size(16.dp),
            color = color,
            strokeWidth = 2f
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(color, Color.Transparent)
                    )
                )
        )
    }
}

/**
 * Traditional Quran Surah Title Banner / Frame (إطار السورة المذهب)
 */
@Composable
fun SurahOrnamentHeader(
    surahNameArabic: String,
    revelationType: String,
    ayahsCount: Int,
    modifier: Modifier = Modifier
) {
    val goldColor = MaterialTheme.colorScheme.secondary

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(goldColor, goldColor.copy(alpha = 0.3f), goldColor)
                ),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Subtle geometric background canvas
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(20.dp))
            ) {
                val step = 40.dp.toPx()
                for (x in 0..(size.width / step).toInt()) {
                    for (y in 0..(size.height / step).toInt()) {
                        drawCircle(
                            color = goldColor.copy(alpha = 0.05f),
                            radius = 2.dp.toPx(),
                            center = Offset(x * step, y * step)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IslamicEightPointStar(
                        modifier = Modifier.size(18.dp),
                        color = goldColor
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "سُورَةُ $surahNameArabic",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    IslamicEightPointStar(
                        modifier = Modifier.size(18.dp),
                        color = goldColor
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                ) {
                    Text(
                        text = "$revelationType • $ayahsCount آيات",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
