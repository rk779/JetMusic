package ml.rk585.jetmusic.core.base.extensions

import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.fresh
import com.dropbox.android.external.store4.get

suspend inline fun <Key : Any, Output : Any> Store<Key, Output>.fetch(
    key: Key,
    forceFresh: Boolean = false
): Output {
    return when {
        // If we're forcing a fresh fetch, do it now
        forceFresh -> fresh(key)
        else -> get(key)
    }
}
