package ml.rk585.jetmusic.ui.screens.playlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.navigationBarsPadding
import kotlinx.coroutines.launch
import ml.rk585.jetmusic.ui.components.JetImage
import ml.rk585.jetmusic.ui.components.JetMusicTopAppBar
import ml.rk585.jetmusic.ui.components.rememberFlowWithLifecycle

@Composable
fun PlaylistScreen(
    onNavigateUp: () -> Unit
) {
    PlaylistScreen(
        viewModel = hiltViewModel(),
        onNavigateUp = onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
private fun PlaylistScreen(
    viewModel: PlaylistViewModel,
    onNavigateUp: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val items by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = emptyList())

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            JetMusicTopAppBar(
                title = { Text(text = "Playlist") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            Spacer(modifier = Modifier.navigationBarsPadding())
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding
        ) {
            items(items) { item ->
                ListItem(
                    text = {
                        Text(
                            text = item.name,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    secondaryText = {
                        Text(
                            text = item.uploaderName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = LocalContentColor.current.copy(ContentAlpha.medium)
                        )
                    },
                    icon = {
                        JetImage(
                            data = item.thumbnailUrl,
                            contentDescription = item.name,
                            contentScale = ContentScale.Crop,
                            size = 48.dp,
                            shape = CircleShape
                        )
                    },
                    modifier = Modifier.clickable {
                        scope.launch {
                            snackbarHostState.showSnackbar("Function not implemented")
                        }
                    }
                )
            }
        }
    }
}
