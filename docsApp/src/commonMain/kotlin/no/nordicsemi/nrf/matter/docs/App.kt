package no.nordicsemi.nrf.matter.docs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import no.nordicsemi.nrf.matter.HomeViewModel
import no.nordicsemi.nrf.matter.docs.components.DocPanel
import no.nordicsemi.nrf.matter.docs.components.ObserveWebDemoEvents
import no.nordicsemi.nrf.matter.docs.docs.DocAnchor
import no.nordicsemi.nrf.matter.docs.docs.DocLinks
import no.nordicsemi.nrf.matter.docs.docs.DocPage
import no.nordicsemi.nrf.matter.docs.docs.LinkTarget
import no.nordicsemi.nrf.matter.docs.platform.openUrl
import no.nordicsemi.nrf.matter.docs.screens.DocsBrowserScreen
import no.nordicsemi.nrf.matter.theme.NordicTheme
import no.nordicsemi.nrf.matter.ui.PHONE_BORDER_WIDTH
import no.nordicsemi.nrf.matter.ui.PHONE_MARGIN
import no.nordicsemi.nrf.matter.ui.PHONE_WIDTH
import no.nordicsemi.nrf.matter.webdemo.WebDemoEvents
import org.koin.compose.viewmodel.koinViewModel
import no.nordicsemi.nrf.matter.App as RealApp

private val PANEL_WIDTH = 400.dp

private const val COMMISSIONING_GATE_ACTION_LABEL = "Done, add the device"

private val DocAnchor.isCommissioningGate: Boolean
    get() = page == DocPage.ONBOARDING

@Composable
fun App() {
    var revealedAnchor by remember { mutableStateOf<DocAnchor?>(null) }
    var showDocsBrowser by remember { mutableStateOf<DocAnchor?>(null) }
    var coachMarkDismissed by rememberSaveable { mutableStateOf(false) }

    fun handleLink(target: LinkTarget) {
        when (target) {
            is LinkTarget.External -> openUrl(target.url)
            is LinkTarget.Internal -> {
                revealedAnchor = null
                showDocsBrowser = target.anchor
            }
        }
    }

    NordicTheme {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                            MaterialTheme.colorScheme.background,
                        ),
                        radius = 1400f,
                    ),
                ),
        ) {
            val panelBesideFrame = maxWidth - PHONE_WIDTH - (PHONE_MARGIN * 2) >= PANEL_WIDTH + 48.dp
            val isPhoneViewport = maxWidth < PHONE_WIDTH
            val capturedMaxHeight = maxHeight

            // The phone is always dead-center: Box positions each aligned child independently,
            // so the side panel appearing/disappearing never shifts it (a Row would recenter the
            // whole row and shove the phone sideways).
            PhoneFrame(
                isPhoneViewport = isPhoneViewport,
                modifier = Modifier.align(Alignment.Center),
            ) {
                PhoneFrameContent(
                    revealedAnchor = revealedAnchor,
                    onReveal = { revealedAnchor = it },
                    onDismissReveal = { revealedAnchor = null },
                    panelBesideFrame = panelBesideFrame,
                    maxHeight = capturedMaxHeight,
                    onLink = ::handleLink,
                    onOpenFullPage = { target -> revealedAnchor = null; showDocsBrowser = target },
                )
            }

            SidePanel(
                visible = panelBesideFrame && revealedAnchor != null,
                anchor = revealedAnchor,
                onLink = ::handleLink,
                onDismiss = { revealedAnchor = null },
                onOpenFullPage = { target -> revealedAnchor = null; showDocsBrowser = target },
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 24.dp),
            )

            FloatingActionButton(
                onClick = { showDocsBrowser = DocAnchor(DocLinks.AppIntro.page) },
                modifier = Modifier.align(Alignment.BottomStart).padding(20.dp),
            ) {
                Icon(Icons.Filled.MenuBook, contentDescription = "Browse all docs")
            }

            AnimatedVisibility(
                visible = !coachMarkDismissed,
                modifier = Modifier.align(Alignment.BottomStart).padding(start = 84.dp, bottom = 32.dp),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                CoachMark(onDismiss = { coachMarkDismissed = true })
            }

            val browserAnchor = showDocsBrowser
            if (browserAnchor != null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    DocsBrowserScreen(
                        initialAnchor = browserAnchor,
                        onLink = ::handleLink,
                        onClose = { showDocsBrowser = null },
                    )
                }
            }
        }
    }
}

@Composable
private fun BoxScope.PhoneFrameContent(
    revealedAnchor: DocAnchor?,
    onReveal: (DocAnchor) -> Unit,
    onDismissReveal: () -> Unit,
    panelBesideFrame: Boolean,
    maxHeight: Dp,
    onLink: (LinkTarget) -> Unit,
    onOpenFullPage: (DocAnchor) -> Unit,
) {
    RealApp(homeViewModel = koinViewModel<HomeViewModel>())

    ObserveWebDemoEvents(onReveal)

    if (!panelBesideFrame && revealedAnchor != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.32f))
                .clickable(enabled = !revealedAnchor.isCommissioningGate, onClick = onDismissReveal),
        )
    }

    if (!panelBesideFrame) {
        AnimatedVisibility(
            visible = revealedAnchor != null,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
        ) {
            val anchor = revealedAnchor
            if (anchor != null) {
                DocPanel(
                    anchor = anchor,
                    onLink = onLink,
                    onDismiss = onDismissReveal,
                    onOpenFullPage = onOpenFullPage,
                    dismissible = !anchor.isCommissioningGate,
                    showFullPage = anchor.isCommissioningGate,
                    primaryActionLabel = if (anchor.isCommissioningGate) COMMISSIONING_GATE_ACTION_LABEL else null,
                    onPrimaryAction = if (anchor.isCommissioningGate) {
                        { onDismissReveal(); WebDemoEvents.acknowledgeCommissioning() }
                    } else null,
                    modifier = Modifier.fillMaxWidth().heightIn(max = maxHeight * 0.75f),
                )
            }
        }
    }
}

@Composable
private fun SidePanel(
    visible: Boolean,
    anchor: DocAnchor?,
    onLink: (LinkTarget) -> Unit,
    onDismiss: () -> Unit,
    onOpenFullPage: (DocAnchor) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(visible = visible, modifier = modifier, enter = fadeIn(), exit = fadeOut()) {
        if (anchor != null) {
            Surface(
                modifier = Modifier.widthIn(max = PANEL_WIDTH).fillMaxHeight().padding(vertical = PHONE_MARGIN),
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 8.dp,
            ) {
                DocPanel(
                    anchor = anchor,
                    onLink = onLink,
                    onDismiss = onDismiss,
                    onOpenFullPage = onOpenFullPage,
                    dismissible = !anchor.isCommissioningGate,
                    showFullPage = anchor.isCommissioningGate,
                    primaryActionLabel = if (anchor.isCommissioningGate) COMMISSIONING_GATE_ACTION_LABEL else null,
                    onPrimaryAction = if (anchor.isCommissioningGate) {
                        { onDismiss(); WebDemoEvents.acknowledgeCommissioning() }
                    } else null,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun PhoneFrame(
    isPhoneViewport: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    if (isPhoneViewport) {
        Box(modifier = modifier.fillMaxSize(), content = content)
        return
    }
    Surface(
        modifier = modifier
            .widthIn(max = PHONE_WIDTH)
            .fillMaxHeight()
            .padding(vertical = PHONE_MARGIN)
            .clip(RoundedCornerShape(36.dp)),
        shape = RoundedCornerShape(36.dp),
        shadowElevation = 24.dp,
        border = BorderStroke(PHONE_BORDER_WIDTH, Color(0xFF10151A)),
    ) {
        Box(modifier = Modifier.fillMaxSize(), content = content)
    }
}

@Composable
private fun CoachMark(onDismiss: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "coach-mark")
    val scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(animation = tween(900, easing = LinearOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "coach-mark-scale",
    )

    Surface(
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.scale(scale).clip(CircleShape).clickable(onClick = onDismiss),
    ) {
        Text(
            "Tap the book for docs",
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        )
    }
}
