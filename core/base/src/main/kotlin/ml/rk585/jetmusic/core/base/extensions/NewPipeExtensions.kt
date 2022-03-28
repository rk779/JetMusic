package ml.rk585.jetmusic.core.base.extensions

import androidx.paging.PagingSource
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.ListExtractor.InfoItemsPage
import org.schabi.newpipe.extractor.ListInfo
import org.schabi.newpipe.extractor.Page

fun <T : InfoItem> ListInfo<T>.toPage(): PagingSource.LoadResult.Page<Page, T> {
    return PagingSource.LoadResult.Page(
        data = relatedItems,
        nextKey = nextPage,
        prevKey = null
    )
}

fun <T : InfoItem> InfoItemsPage<T>.toPage(): PagingSource.LoadResult.Page<Page, T> {
    return PagingSource.LoadResult.Page(
        data = items,
        nextKey = nextPage,
        prevKey = null
    )
}
