package ml.rk585.jetmusic.ui.library

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
fun Library() {
    Library(
        onClickAdd = { /*TODO*/ }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Library(
    onClickAdd: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarScrollState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LibraryTopAppBar(
                scrollBehavior = scrollBehavior,
                onClickAdd = onClickAdd
            )
        }
    ) {

    }
}

@Composable
private fun LibraryTopAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onClickAdd: () -> Unit = { }
) {
    JetMusicTopAppBar(
        title = { Text(text = stringResource(id = R.string.your_library)) },
        modifier = modifier,
        actions = {
            IconButton(onClick = onClickAdd) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}
