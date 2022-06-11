package ml.rk585.jetmusic.ui.artist

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import ml.rk585.jetmusic.core.base.extensions.stripArtistSuffix
import ml.rk585.jetmusic.ui.common.R
import ml.rk585.jetmusic.ui.common.components.Loading
import ml.rk585.jetmusic.ui.common.components.IconButton
import ml.rk585.jetmusic.ui.common.components.JetMusicTopAppBar
import ml.rk585.jetmusic.ui.common.components.MediaListItem
import ml.rk585.jetmusic.ui.common.components.Scaffold
import ml.rk585.jetmusic.ui.common.components.rememberStateWithLifecycle
import ml.rk585.jetmusic.ui.common.extensions.copy
import ml.rk585.jetmusic.ui.common.extensions.drawForegroundGradientScrim
import ml.rk585.jetmusic.ui.common.extensions.iconButtonBackgroundScrim
import org.schabi.newpipe.extractor.channel.ChannelInfo
import org.schabi.newpipe.extractor.stream.StreamInfoItem

@Destination(navArgsDelegate = ArtistNavArgs::class)
@Composable
fun Artist(
    navigator: ArtistNavigator
) {
    Artist(
        navigator = navigator,
        viewModel = hiltViewModel()
    )
}

@Composable
internal fun Artist(
    navigator: ArtistNavigator,
    viewModel: ArtistViewModel
) {
    val channelInfo by rememberStateWithLifecycle(viewModel.channelInfo)
    val channelPager = viewModel.artistPagerFlow.collectAsLazyPagingItems()

    if (channelInfo != null) {
        Artist(
            channelInfo = channelInfo!!,
            channelPagingItems = channelPager,
            onNavigateUp = navigator::onNavigateUp
        )
    } else {
        Loading()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Artist(
    channelInfo: ChannelInfo,
    channelPagingItems: LazyPagingItems<StreamInfoItem>,
    onNavigateUp: () -> Unit
) {
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarScrollState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            var appBarHeight by remember { mutableStateOf(0) }
            val showAppBarBackground by remember {
                derivedStateOf {
                    val visibleItemsInfo = listState.layoutInfo.visibleItemsInfo
                    when {
                        visibleItemsInfo.isEmpty() -> false
                        appBarHeight <= 0 -> false
                        else -> {
                            val firstVisibleItem = visibleItemsInfo[0]
                            when {
                                // If the first visible item is > 0, we want to show the app bar background
                                firstVisibleItem.index > 0 -> true
                                // If the first item is visible, only show the app bar background once the only
                                // remaining part of the item is <= the app bar
                                else -> firstVisibleItem.size + firstVisibleItem.offset <= appBarHeight
                            }
                        }
                    }
                }
            }

            ArtistTopAppBar(
                title = channelInfo.name.stripArtistSuffix(),
                showAppBarBackground = showAppBarBackground,
                scrollBehavior = scrollBehavior,
                onNavigateUp = onNavigateUp,
                modifier = Modifier
                    .onSizeChanged { appBarHeight = it.height }
            )
        }
    ) { contentPadding ->
        ArtistContent(
            channelInfo = channelInfo,
            channelPagingItems = channelPagingItems,
            listState = listState,
            contentPadding = contentPadding,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
internal fun ArtistContent(
    channelInfo: ChannelInfo,
    channelPagingItems: LazyPagingItems<StreamInfoItem>,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = contentPadding.copy(copyTop = false)
    ) {
        item {
            BackdropImage(
                title = channelInfo.name.stripArtistSuffix(),
                imageUrl = channelInfo.avatarUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 10)
                    .clipToBounds()
                    .offset {
                        IntOffset(
                            x = 0,
                            y = if (listState.firstVisibleItemIndex == 0) {
                                listState.firstVisibleItemScrollOffset / 2
                            } else 0
                        )
                    }
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            ArtistInfoDetailsHeader(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
        items(channelPagingItems) { item ->
            if (item != null) {
                MediaListItem(
                    item = item
                )
            }
        }
    }
}

@Composable
internal fun BackdropImage(
    title: String,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        Box {
            AsyncImage(
                model = imageUrl,
                contentDescription = title,
                modifier = Modifier
                    .fillMaxSize()
                    .drawForegroundGradientScrim(Color.Black.copy(alpha = 0.7f)),
                contentScale = ContentScale.Crop
            )

            val textStyle = MaterialTheme.typography.displayMedium
            val shadowSize = with(LocalDensity.current) {
                textStyle.fontSize.toPx() / 16
            }

            Text(
                text = title,
                style = textStyle.copy(
                    color = Color.White,
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(shadowSize, shadowSize),
                        blurRadius = 0.1f
                    )
                ),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
internal fun ArtistInfoDetailsHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Row {
                OutlinedButton(onClick = { /*TODO*/ }) {
                    Text(text = stringResource(id = R.string.follow))
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = null
                    )
                }
            }
        }
        Button(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = stringResource(id = R.string.play))
        }
    }
}

@Composable
internal fun ArtistTopAppBar(
    title: String,
    showAppBarBackground: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigateUp: () -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = when {
            showAppBarBackground -> MaterialTheme.colorScheme.surface
            else -> Color.Transparent
        },
        animationSpec = spring(
            stiffness = Spring.StiffnessLow
        ),
    )

    JetMusicTopAppBar(
        title = {
            Crossfade(showAppBarBackground) { show ->
                if (show) Text(text = title)
            }
        },
        modifier = modifier,
        contentPadding = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
            .asPaddingValues(),
        navigationIcon = {
            IconButton(
                onClick = onNavigateUp,
                modifier = Modifier.iconButtonBackgroundScrim(enabled = !showAppBarBackground)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.navigate_up)
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = containerColor
        ),
        scrollBehavior = scrollBehavior
    )
}
