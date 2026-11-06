package id.andreasmlbngaol.studio.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import studio.shared.generated.resources.Res
import studio.shared.generated.resources.avatar

private val AvatarRing = Color(0xFF111318)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HeroCollage(modifier: Modifier = Modifier) {
    val burst = MaterialShapes.Cookie12Sided.toShape()
    BoxWithConstraints(modifier) {
        val s = maxWidth.value / 560f
        Box(
            Modifier
                .size(560.dp * s, 580.dp * s)
                .clipToBounds(),
        ) {
            Box(
                Modifier
                    .offset(41.dp * s, 162.dp * s)
                    .size(250.dp * s, 520.dp * s)
                    .graphicsLayer {
                        transformOrigin = TransformOrigin(0f, 0f)
                        rotationZ = -38f
                    }
                    .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(50)),
            )
            Box(
                Modifier
                    .offset(0.dp, 10.dp * s)
                    .size(210.dp * s)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.Code,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(104.dp * s),
                )
            }
            Box(
                Modifier
                    .offset(390.dp * s, 0.dp)
                    .size(170.dp * s)
                    .background(MaterialTheme.colorScheme.primary, burst),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.SmartToy,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(72.dp * s),
                )
            }
            Box(
                Modifier
                    .offset(350.dp * s, 380.dp * s)
                    .size(200.dp * s)
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.Cloud,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(96.dp * s),
                )
            }
            Box(
                Modifier
                    .offset(30.dp * s, 400.dp * s)
                    .size(160.dp * s)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.Computer,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(84.dp * s),
                )
            }
            Image(
                painter = painterResource(Res.drawable.avatar),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .offset(200.dp * s, 200.dp * s)
                    .size(240.dp * s)
                    .clip(CircleShape)
                    .border(8.dp * s, AvatarRing, CircleShape),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CompactHeroCollage(modifier: Modifier = Modifier) {
    val burst = MaterialShapes.Cookie12Sided.toShape()
    BoxWithConstraints(modifier) {
        val s = maxWidth.value / 350f
        Box(
            Modifier
                .size(350.dp * s, 220.dp * s)
                .clipToBounds(),
        ) {
            Box(
                Modifier
                    .offset(58.dp * s, 59.dp * s)
                    .size(120.dp * s, 260.dp * s)
                    .graphicsLayer {
                        transformOrigin = TransformOrigin(0f, 0f)
                        rotationZ = -38f
                    }
                    .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(50)),
            )
            Box(
                Modifier
                    .size(120.dp * s)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.Code,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(56.dp * s),
                )
            }
            Box(
                Modifier
                    .offset(220.dp * s, 130.dp * s)
                    .size(120.dp * s)
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.Cloud,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(56.dp * s),
                )
            }
            Box(
                Modifier
                    .offset(30.dp * s, 140.dp * s)
                    .size(100.dp * s)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.Computer,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(52.dp * s),
                )
            }
            Box(
                Modifier
                    .offset(250.dp * s, 0.dp)
                    .size(100.dp * s)
                    .background(MaterialTheme.colorScheme.primary, burst),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Filled.SmartToy,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(44.dp * s),
                )
            }
            Image(
                painter = painterResource(Res.drawable.avatar),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .offset(126.dp * s, 86.dp * s)
                    .size(96.dp * s)
                    .clip(CircleShape)
                    .border(5.dp * s, AvatarRing, CircleShape),
            )
        }
    }
}
