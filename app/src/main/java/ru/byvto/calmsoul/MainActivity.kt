package ru.byvto.calmsoul

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
import ru.byvto.calmsoul.ui.AboutScreen
import ru.byvto.calmsoul.ui.HomeScreen
import ru.byvto.calmsoul.ui.theme.CalmSoulDevTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
//            .apply {
//            setKeepOnScreenCondition {
//                viewModel.isLoading.value
//            }
//        }
        setContent {
            CalmSoulDevTheme {

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = HomeScreen) {
                    composable<HomeScreen> {
                        HomeScreen(
                            viewModel = hiltViewModel(),
                            navController = navController
                        )
                    }
                    composable<AboutScreen> {
                        AboutScreen(navController = navController)
                    }
                }
            }
        }
    }
}