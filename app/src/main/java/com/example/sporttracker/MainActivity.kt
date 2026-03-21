package com.example.sporttracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.sporttracker.ui.components.WorkoutDatabase
import com.example.sporttracker.ui.components.WorkoutViewModel
import com.example.sporttracker.ui.components.WorkoutViewModelFactory


import com.example.sporttracker.ui.screens.HistoryCalendarScreen
import com.example.sporttracker.ui.screens.PushUpCounterScreen
import com.example.sporttracker.ui.screens.StatScreen
import com.example.sporttracker.ui.theme.SportTrackerTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        // 1. Инициализируем базу данных
        val database = WorkoutDatabase.getDatabase(applicationContext)
        // 2. Создаем фабрику
        val factory = WorkoutViewModelFactory(database.workoutDao(), application)

        setContent {
            // 3. Передаем фабрику во вью модель
            val viewModelWorkout: WorkoutViewModel = viewModel(factory = factory)
            SportTrackerTheme(){
                Box(modifier = Modifier.fillMaxSize()){
                    Image(
                        painter = painterResource(id = R.drawable.bg), // Твоя картинка из res/drawable
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop // Важно: обрезает фото, чтобы оно заполнило весь Box без искажений
                    )
                    Surface(modifier = Modifier.fillMaxSize(),
                        color = Color.Transparent){
                        MainScreen(viewModelWorkout)

                    }
                }
            }

        }
    }
}

fun setTarget(target: Int, date: Float){

}
@OptIn(ExperimentalFoundationApi::class) // Добавь это!
@Composable
fun MainScreen(viewModelWorkout: WorkoutViewModel){
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = {3})
    Box(modifier = Modifier.fillMaxSize()){
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ){page ->
            when(page){
                0 -> HistoryCalendarScreen(viewModelWorkout)
                1 -> PushUpCounterScreen(viewModelWorkout)
                2 -> StatScreen()
            }
        }
    }
}




















