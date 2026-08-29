package com.example.ui.screens.qibla

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.calculations.QiblaCalculator
import com.example.ui.AppViewModel
import com.example.ui.components.SirajTopAppBar
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun QiblaScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val selectedCity by viewModel.selectedCity.collectAsState()
    val azimuth by viewModel.compassAzimuth.collectAsState()

    DisposableEffect(Unit) {
        viewModel.compassSensorManager.start()
        onDispose {
            viewModel.compassSensorManager.stop()
        }
    }

    val qiblaBearing = remember(selectedCity) {
        QiblaCalculator.calculateQiblaBearing(selectedCity.latitude, selectedCity.longitude)
    }
    val distanceToKaaba = remember(selectedCity) {
        QiblaCalculator.calculateDistanceToKaabaKm(selectedCity.latitude, selectedCity.longitude)
    }

    val animatedAzimuth by animateFloatAsState(
        targetValue = -azimuth,
        animationSpec = tween(durationMillis = 150),
        label = "compass_rotation"
    )

    val deltaAngle = ((qiblaBearing - azimuth + 360) % 360)
    val isFacingQibla = deltaAngle < 5 || deltaAngle > 355

    Scaffold(
        topBar = {
            SirajTopAppBar(title = "اتجاه القبلة الشريفة")
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            // Location Header
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${selectedCity.nameAr} (${selectedCity.countryAr})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Text(
                            text = "المسافة إلى الكعبة: ${String.format("%.0f", distanceToKaaba)} كم",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = "${String.format("%.1f", qiblaBearing)}°",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Compass Dial Canvas
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(280.dp)
                    .testTag("compass_dial_box")
            ) {
                // Fixed outer ring glow
                Surface(
                    shape = CircleShape,
                    color = if (isFacingQibla) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxSize()
                ) {}

                // Rotating dial
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(animatedAzimuth)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        val center = Offset(size.width / 2, size.height / 2)
                        val radius = size.minDimension / 2

                        // Outer dial circle
                        drawCircle(
                            color = Color(0xFF134E4A).copy(alpha = 0.2f),
                            radius = radius,
                            style = Stroke(width = 3.dp.toPx())
                        )

                        // 12 Cardinal Tick Marks
                        for (i in 0 until 12) {
                            val angleRad = Math.toRadians((i * 30).toDouble() - 90)
                            val isMajor = i % 3 == 0
                            val tickLength = if (isMajor) 16.dp.toPx() else 8.dp.toPx()
                            val startX = center.x + (radius - tickLength) * cos(angleRad).toFloat()
                            val startY = center.y + (radius - tickLength) * sin(angleRad).toFloat()
                            val endX = center.x + radius * cos(angleRad).toFloat()
                            val endY = center.y + radius * sin(angleRad).toFloat()

                            drawLine(
                                color = if (isMajor) Color(0xFFC99700) else Color.Gray,
                                start = Offset(startX, startY),
                                end = Offset(endX, endY),
                                strokeWidth = if (isMajor) 3.dp.toPx() else 1.5.dp.toPx()
                            )
                        }

                        // Kaaba Target Needle on Dial
                        val qiblaAngleRad = Math.toRadians(qiblaBearing - 90)
                        val kaabaX = center.x + (radius - 28.dp.toPx()) * cos(qiblaAngleRad).toFloat()
                        val kaabaY = center.y + (radius - 28.dp.toPx()) * sin(qiblaAngleRad).toFloat()

                        drawCircle(
                            color = Color(0xFFC99700),
                            radius = 12.dp.toPx(),
                            center = Offset(kaabaX, kaabaY)
                        )
                    }

                    // North label on rotating dial
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 22.dp)
                    ) {
                        Text(
                            text = "N",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    }
                }

                // Fixed Center Target Arrow
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Explore,
                        contentDescription = null,
                        tint = if (isFacingQibla) MaterialTheme.colorScheme.primary else GoldAccent,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = if (isFacingQibla) "أنت باتجاه القبلة ✓" else "${String.format("%.0f", azimuth)}°",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isFacingQibla) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Calibration Note Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "للحصول على أعلى دقة، حرّك الهاتف في الهواء على شكل الرقم (8) لمعايرة الحساس.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
