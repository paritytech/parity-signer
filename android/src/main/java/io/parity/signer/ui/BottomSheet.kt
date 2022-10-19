package io.parity.signer.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import io.parity.signer.ui.theme.backgroundTertiary
import kotlinx.coroutines.launch

/**
 * For use in the same screen with content
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetWrapperContent(
	bottomSheetState: ModalBottomSheetState,
	bottomSheetContent: @Composable () -> Unit,
	mainContent: @Composable () -> Unit
) {
	ModalBottomSheetLayout(
		sheetBackgroundColor = MaterialTheme.colors.backgroundTertiary,
		sheetState = bottomSheetState,
		sheetContent = {
			BottomSheetContentWrapperInternal {
				bottomSheetContent()
			}
		},
		content = mainContent,
	)
}
/**
 * Used for screens controlled by central rust-based navigation system
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetWrapperRoot(
	onClosedAction: () -> Unit = {},
	bottomSheetContent: @Composable (state: BottomSheetPositionHandle) -> Unit,
) {
	val coroutineScope = rememberCoroutineScope()
	val modalBottomSheetState =
		rememberModalBottomSheetState(
			ModalBottomSheetValue.Hidden,
			confirmStateChange = {
				it != ModalBottomSheetValue.HalfExpanded
			}
		)

	var wasSheetClosed by remember { mutableStateOf(false) }
	var wasSheetShown by remember { mutableStateOf(false) }

	val handle = remember {
		object : BottomSheetPositionHandle {
			override fun hide() {
				wasSheetClosed = true
				coroutineScope.launch {
					if (!modalBottomSheetState.isVisible) {
						modalBottomSheetState.hide()
					}
				}
			}

			override fun show() {
				coroutineScope.launch {
					modalBottomSheetState.show()
				}
			}
		}
	}

	ModalBottomSheetLayout(
		sheetBackgroundColor = MaterialTheme.colors.backgroundTertiary,
		sheetState = modalBottomSheetState,
		sheetContent = {
			BottomSheetContentWrapperInternal {
				bottomSheetContent(handle)
			}
		},
		content = {},
	)

	BackHandler {
		coroutineScope.launch { modalBottomSheetState.hide() }
	}

	//show once view is create to have initial open animation
	LaunchedEffect(key1 = modalBottomSheetState) {
		modalBottomSheetState.show()
		wasSheetShown = true
	}

	// Take action based on hidden state
	LaunchedEffect(modalBottomSheetState.currentValue) {
		when (modalBottomSheetState.currentValue) {
			ModalBottomSheetValue.Hidden -> {
				if (!wasSheetClosed && wasSheetShown) {
					onClosedAction()
				}
			}
			else -> {}
		}
	}
}

interface BottomSheetPositionHandle {
	fun hide()
	fun show()
}

@Composable
private fun BottomSheetContentWrapperInternal(
	content: @Composable () -> Unit,
) {
	val screenHeight = LocalConfiguration.current.screenHeightDp.dp
	Box(
		modifier = Modifier
			.wrapContentHeight()
			.heightIn(0.dp, screenHeight - 40.dp)
			.fillMaxWidth()
			.clip(RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
	) {
		content()
	}
}



