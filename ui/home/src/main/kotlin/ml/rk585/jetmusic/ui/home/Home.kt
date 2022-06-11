package ml.rk585.jetmusic.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import ml.rk585.jetmusic.ui.common.R
import ml.rk585.jetmusic.ui.common.components.JetMusicTopAppBar

@Destination
@Composable
fun Home(
    navigator: HomeNavigator
) {
    Home(
        onClickSettings = navigator::openSettings
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    onClickSettings: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarScrollState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HomeTopAppBar(
                scrollBehavior = scrollBehavior,
                onClickSettings = onClickSettings
            )
        }
    ) {

    }
}

@Composable
internal fun HomeTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onClickSettings: () -> Unit
) {
    JetMusicTopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        modifier = modifier,
        actions = {
            IconButton(onClick = onClickSettings) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = stringResource(id = R.string.settings)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}
