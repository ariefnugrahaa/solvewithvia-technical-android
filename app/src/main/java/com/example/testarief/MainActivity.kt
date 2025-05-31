package com.example.testarief

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core.domain.model.RequestStatus
import com.example.core.ui.theme.ErrorBackground
import com.example.core.ui.theme.ErrorText
import com.example.core.ui.theme.SuccessBackground
import com.example.core.ui.theme.SuccessText
import com.example.core.ui.theme.TestAriefTheme
import com.example.feature.request.presentation.home.HomeScreen
import com.example.feature.request.presentation.request_create.CreateRequestScreen
import com.example.feature.request.presentation.request_list.RequestListScreen
import com.example.feature.request.presentation.settings.SettingsScreen
import com.example.feature.request.presentation.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
            val fontSize by settingsViewModel.fontSize.collectAsState()
            TestAriefTheme(
                darkTheme = isDarkMode,
                fontSize = fontSize
            ) {
                MainNav(settingsViewModel)
            }
        }
    }
}

@Composable
fun MainNav(settingsViewModel: SettingsViewModel) {
    val navController = rememberNavController()
    val items = listOf(MainScreen.Home, MainScreen.Requests, MainScreen.Settings)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    var snackbarSuccess by remember { mutableStateOf<Boolean?>(null) }

    Scaffold(
        bottomBar = {
            bottomNavigationBar(items, currentRoute, navController)
        },
        snackbarHost = {
            customSnackbarHost(snackbarHostState, snackbarSuccess)
        }
    ) { innerPadding ->
        navigationHost(navController, innerPadding, settingsViewModel, scope, snackbarHostState) { message, success ->
            snackbarMessage = message
            snackbarSuccess = success
        }
    }
}

@Composable
private fun bottomNavigationBar(
    items: List<MainScreen>,
    currentRoute: String?,
    navController: NavController
) {
    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = stringResource(screen.label)) },
                label = {
                    Text(
                        text = stringResource(screen.label),
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
private fun customSnackbarHost(
    snackbarHostState: SnackbarHostState,
    snackbarSuccess: Boolean?
) {
    SnackbarHost(hostState = snackbarHostState) { data ->
        val isSuccess = snackbarSuccess == true
        Snackbar(
            containerColor = if (isSuccess) SuccessBackground else ErrorBackground,
            contentColor = if (isSuccess) SuccessText else ErrorText,
            action = {
                TextButton(onClick = { snackbarHostState.currentSnackbarData?.dismiss() }) {
                    Text(
                        text = stringResource(R.string.snackbar_dismiss),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSuccess) SuccessText else ErrorText
                    )
                }
            }
        ) {
            Text(
                text = data.visuals.message,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun navigationHost(
    navController: NavController,
    innerPadding: PaddingValues,
    settingsViewModel: SettingsViewModel,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onSnackbarUpdate: (String, Boolean) -> Unit
) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = AppRoutes.HOME,
        modifier = Modifier.fillMaxSize().padding(innerPadding)
    ) {
        composable(AppRoutes.HOME) {
            HomeScreen(
                onCreateRequest = { navController.navigate(AppRoutes.CREATE_REQUEST) }
            )
        }
        composable(AppRoutes.REQUESTS) {
            RequestListScreen()
        }
        composable(AppRoutes.SETTINGS) {
            SettingsScreen(viewModel = settingsViewModel)
        }
        composable(AppRoutes.CREATE_REQUEST) {
            CreateRequestScreen(
                onRequestCreated = { status ->
                    navController.popBackStack()
                    val message = when (status) {
                        RequestStatus.APPROVED -> "Request approved"
                        RequestStatus.REJECTED -> "Request rejected"
                        else -> "Request processed"
                    }
                    onSnackbarUpdate(message, status == RequestStatus.APPROVED)
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = message,
                            duration = SnackbarDuration.Short
                        )
                    }
                },
                navController = navController
            )
        }
    }
}
