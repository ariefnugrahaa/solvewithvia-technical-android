package com.example.feature.request.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.feature.request.R
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.ui.theme.TestAriefTheme
import com.example.core.ui.theme.Primary
import com.example.core.ui.theme.HomeScreenBackground
import com.example.core.ui.theme.Secondary
import com.example.core.ui.theme.White

@Composable
fun HomeScreen(
    onCreateRequest: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HomeScreenBackground)
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.home),
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = Primary,
            modifier = Modifier.align(Alignment.TopStart)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .background(White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.via_logo),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier.size(100.dp)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onCreateRequest,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = Secondary)
            ) {
                Text(
                    text = stringResource(R.string.home_create_request_button),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TestAriefTheme {
        HomeScreen(
            onCreateRequest = {}
        )
    }
}