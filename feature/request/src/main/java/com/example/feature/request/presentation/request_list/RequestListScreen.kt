package com.example.feature.request.presentation.request_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.core.components.StatusChip
import com.example.core.domain.model.Request
import com.example.core.ui.theme.TestAriefTheme
import com.example.feature.request.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.filled.Check

@Composable
fun RequestListScreen(
    onRequestClick: (Request) -> Unit = {},
    viewModel: RequestListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.request_list_title),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RequestFilterStatus.entries.forEach { status ->
                    val filterText = when (status) {
                        RequestFilterStatus.ALL -> stringResource(R.string.request_list_filter_all)
                        RequestFilterStatus.APPROVED -> stringResource(R.string.request_list_filter_approved)
                        RequestFilterStatus.REJECTED -> stringResource(R.string.request_list_filter_rejected)
                    }
                    Button(
                        onClick = { viewModel.setFilterStatus(status) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.filterStatus == status) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = if (state.filterStatus == status) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = filterText, style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { expanded = true }) {
                Text(
                    text = stringResource(R.string.request_list_sort_label, state.sortOrder.name),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(R.string.request_list_sort_label),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                DropdownMenu(
                    expanded = expanded, onDismissRequest = { expanded = false }) {
                    RequestSortOrder.entries.forEach { sortOrder ->
                        val sortText = when (sortOrder) {
                            RequestSortOrder.NEWEST_FIRST -> stringResource(R.string.request_list_sort_newest)
                            RequestSortOrder.OLDEST_FIRST -> stringResource(R.string.request_list_sort_oldest)
                        }
                        DropdownMenuItem(
                            text = { Text(sortText) },
                            onClick = {
                                viewModel.setSortOrder(sortOrder)
                                expanded = false
                            }, leadingIcon = if (state.sortOrder == sortOrder) {
                                @Composable { Icon(Icons.Default.Check, contentDescription = null) }
                            } else {
                                null
                            })
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (state.requests.isEmpty()) {
            Text(
                text = stringResource(R.string.request_list_no_requests),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.requests) { request ->
                    RequestItem(
                        request = request, onClick = { onRequestClick(request) })
                }
            }
        }

        state.error?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun RequestItem(
    request: Request,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = request.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = request.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))

                val dateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.getDefault())
                val formattedDate = dateFormat.format(Date(request.createdAt))
                Text(
                    text = stringResource(R.string.request_list_created_label) + " " + formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            StatusChip(
                status = request.status, modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RequestListScreenPreview() {
    TestAriefTheme {
        RequestListScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun RequestItemPreview() {
    TestAriefTheme {
        RequestItem(
            request = Request(
                id = "1",
                title = "Sample Request Title that is very long and should be truncated",
                description = "Sample description for the request item that might be too long and need truncation to fit in two lines.",
                status = com.example.core.domain.model.RequestStatus.PENDING,
                createdAt = System.currentTimeMillis()
            ), onClick = {})
    }
} 