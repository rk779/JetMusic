package ml.rk585.jetmusic.ui.artist

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import ml.rk585.jetmusic.core.base.extensions.stripArtistSuffix
import ml.rk585.jetmusic.ui.common.R
import ml.rk585.jetmusic.ui.common.components.IconButton
import ml.rk585.jetmusic.ui.common.components.Loading
import ml.rk585.jetmusic.ui.common.components.MediaListItem
import ml.rk585.jetmusic.ui.common.components.ParallaxTopAppBar
import ml.rk585.jetmusic.ui.common.components.rememberStateWithLifecycle
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
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        decayAnimationSpec = rememberSplineBasedDecay(),
        state = rememberTopAppBarScrollState()
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ParallaxTopAppBar(
                title = { Text(text = channelInfo.name.stripArtistSuffix()) },
                parallaxImagePainter = rememberAsyncImagePainter(channelInfo.avatarUrl),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = WindowInsets.statusBars.asPaddingValues(),
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.navigate_up)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { contentPadding ->
        ArtistContent(
            channelPagingItems = channelPagingItems,
            listState = listState,
            contentPadding = contentPadding,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
internal fun ArtistContent(
    channelPagingItems: LazyPagingItems<StreamInfoItem>,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = contentPadding
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
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
