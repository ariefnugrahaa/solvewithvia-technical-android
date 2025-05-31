package com.example.core.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.domain.model.RequestStatus
import com.example.core.ui.theme.ApprovedStatusBackground
import com.example.core.ui.theme.ApprovedStatusText
import com.example.core.ui.theme.PendingBackground
import com.example.core.ui.theme.PendingText
import com.example.core.ui.theme.RejectedBackground
import com.example.core.ui.theme.RejectedText
import com.example.core.ui.theme.TestAriefTheme

@Composable
fun StatusChip(
    status: RequestStatus,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (status) {
        RequestStatus.PENDING -> PendingBackground to PendingText
        RequestStatus.APPROVED -> ApprovedStatusBackground to ApprovedStatusText
        RequestStatus.REJECTED -> RejectedBackground to RejectedText
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Text(
            text = status.name,
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatusChipPreview() {
    TestAriefTheme {
        StatusChip(status = RequestStatus.PENDING)
    }
}

@Preview(showBackground = true)
@Composable
fun StatusChipApprovedPreview() {
    TestAriefTheme {
        StatusChip(status = RequestStatus.APPROVED)
    }
}

@Preview(showBackground = true)
@Composable
fun StatusChipRejectedPreview() {
    TestAriefTheme {
        StatusChip(status = RequestStatus.REJECTED)
    }
}

