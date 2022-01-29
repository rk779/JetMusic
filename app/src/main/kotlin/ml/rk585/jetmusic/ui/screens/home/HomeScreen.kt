package ml.rk585.jetmusic.ui.screens.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ml.rk585.jetmusic.R
import ml.rk585.jetmusic.ui.components.AppBarStyle
import ml.rk585.jetmusic.ui.components.JetMusicBottomNavigationBar
import ml.rk585.jetmusic.ui.components.JetMusicTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            JetMusicTopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                appBarStyle = AppBarStyle.Small,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { insetPaddingValues ->
        DummyList(
            modifier = Modifier.fillMaxSize(),
            contentPadding = insetPaddingValues
        )
    }
}

@Composable
private fun BottomNavigationBar(
    modifier: Modifier = Modifier
) {
    val items = listOf(
        "Home" to Icons.Default.Home,
        "Search" to Icons.Default.Search,
        "Library" to Icons.Default.LibraryMusic
    )
    var currentItem by remember { mutableStateOf(items[1]) }

    JetMusicBottomNavigationBar(
        modifier = modifier
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = item == currentItem,
                onClick = { currentItem = item },
                icon = {
                    Icon(
                        imageVector = item.second,
                        contentDescription = item.first
                    )
                },
                label = { Text(text = item.first) }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DummyList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(100) { item ->
            ListItem {
                Text(text = "Hello world $item")
            }
        }
    }
}
