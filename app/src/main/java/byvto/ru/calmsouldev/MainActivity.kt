package byvto.ru.calmsouldev

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import byvto.ru.calmsouldev.ui.AboutScreen
import byvto.ru.calmsouldev.ui.CalmSoulEvent
import byvto.ru.calmsouldev.ui.MainScreen
import byvto.ru.calmsouldev.ui.UpdateScreen
import byvto.ru.calmsouldev.ui.theme.CalmSoulDevTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        setContent {
            CalmSoulDevTheme {
                val context = LocalContext.current
                LaunchedEffect(true) {
                    viewModel.channel.collect { event ->
                        when (event) {
                            is CalmSoulEvent.ShowToast -> {
                                Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                }

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main_screen") {
                    composable("main_screen") {
                        MainScreen(
                            viewModel = hiltViewModel(),
                            navController = navController
                        )
                    }
                    composable("about_screen") {
                        AboutScreen(navController = navController)
                    }
                    composable("update_screen") {
                        UpdateScreen(
                            viewModel = hiltViewModel(),
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}
