/*
 * SPDX-FileCopyrightText: 2025 OPass
 * SPDX-License-Identifier: GPL-3.0-only
 */

package app.opass.ccip.android.ui.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import app.opass.ccip.android.R

/**
 * A reusable search app bar
 * @param onSearch Callback when user submits a new query to search
 * @param isEnabled If the search bar is enabled or disabled, defaults to true
 * @param content Content to show in search view when a new query has been submitted
 * @see TopAppBarComposable
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SearchAppBarComposable(
    onSearch: (query: String) -> Unit,
    isEnabled: Boolean = true,
    @StringRes searchHint: Int? = null,
    onNavigateUp: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit = {}
) {
    var query by rememberSaveable { mutableStateOf("") }
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    fun search(search: String) {
        query = search
        onSearch(query)
    }

    fun onExpandedChange(expand: Boolean) {
        isExpanded = expand
        query = String()
        search(query)
    }

    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        SearchBar(
            content = content,
            expanded = isExpanded,
            onExpandedChange = { onExpandedChange(it) },
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = { search(it) },
                    onSearch = { search(it) },
                    expanded = isExpanded,
                    onExpandedChange = { onExpandedChange(it) },
                    enabled = isEnabled,
                    leadingIcon = {
                        if (isExpanded) {
                            IconButton(onClick = { onExpandedChange(false) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        } else {
                            if (onNavigateUp != null) {
                                IconButton(onClick = onNavigateUp) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            } else {
                                Icon(Icons.Default.Search, contentDescription = null)
                            }
                        }
                    },
                    trailingIcon = {
                        if (isExpanded && query.isNotBlank()) {
                            IconButton(onClick = { search(String()) }) {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    placeholder = { searchHint?.let { Text(text = stringResource(id = it)) } }
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchAppBarComposablePreview() {
    SearchAppBarComposable(searchHint = R.string.search_event, onSearch = {})
}
