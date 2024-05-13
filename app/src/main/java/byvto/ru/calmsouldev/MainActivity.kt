package byvto.ru.calmsouldev

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import byvto.ru.calmsouldev.ui.AboutScreen
import byvto.ru.calmsouldev.ui.MainScreen
import byvto.ru.calmsouldev.ui.UpdateScreen
import byvto.ru.calmsouldev.ui.theme.CalmSoulDevTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity() : ComponentActivity() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            CalmSoulDevTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "update_screen") {
//                    composable("splash_screen") {
//                        SplashScreen(navController = navController)
//                    }
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

//@Composable
//fun SplashScreen(navController: NavController) {
//    val scale = remember {
//        Animatable(0f)
//    }
//
//    LaunchedEffect(key1 = true) {
//        scale.animateTo(
//            targetValue = 5f,
//            animationSpec = tween(
//                durationMillis = 500,
//                easing = {
//                    OvershootInterpolator(2f).getInterpolation(it)
//                }
//            )
//        )
//        delay(2000L)
//        navController.navigate("main_screen")
//    }
//
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.moai),
//            contentDescription = "moai",
//            modifier = Modifier.scale(scale.value)
//        )
//    }
//}
