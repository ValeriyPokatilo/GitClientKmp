package org.example.android.uikit.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.library.utils.paging.PagingState

@Composable
fun <T> PagingContent(
    modifier: Modifier = Modifier,
    state: PagingState<T>,
    onLoadNextRequested: () -> Unit,
    itemContent: @Composable (T) -> Unit
) {
    val lazyListState: LazyListState = rememberLazyListState()

    val shouldLoadMore: Boolean by remember {
        derivedStateOf {
            val layoutInfo: LazyListLayoutInfo = lazyListState.layoutInfo
            val lastVisibleIndex: Int? = layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val isLastItem: Boolean = lastVisibleIndex == layoutInfo.totalItemsCount - 1
            lastVisibleIndex != 0 && !state.isEndOfList && isLastItem
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !state.isNextPageLoading) {
            onLoadNextRequested()
        }
    }

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 16.dp)
    ) {
        items(state.items) { item ->
            itemContent(item)
        }

        if (state.isNextPageLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
