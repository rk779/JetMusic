package ml.rk585.jetmusic.ui.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
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
fun Settings(
    navigator: SettingsNavigator
) {
    Settings(
        onClickNavigateUp = navigator::onNavigateUp
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Settings(
    onClickNavigateUp: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarScrollState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SettingsTopAppBar(
                scrollBehavior = scrollBehavior,
                onClickNavigateUp = onClickNavigateUp
            )
        }
    ) {

    }
}

@Composable
internal fun SettingsTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onClickNavigateUp: () -> Unit
) {
    JetMusicTopAppBar(
        title = { Text(text = stringResource(id = R.string.settings)) },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onClickNavigateUp) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = stringResource(id = R.string.navigate_up)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}
