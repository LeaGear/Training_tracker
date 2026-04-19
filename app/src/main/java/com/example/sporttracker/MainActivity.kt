package com.example.sporttracker


import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sporttracker.data.db.WorkoutDatabase
import com.example.sporttracker.ui.screens.HistoryCalendarScreen
import com.example.sporttracker.ui.screens.PushUpCounterScreen
import com.example.sporttracker.ui.theme.SportTrackerTheme
import com.example.sporttracker.ui.viewmodel.WorkoutViewModel
import com.example.sporttracker.ui.viewmodel.WorkoutViewModelFactory


class MainActivity : AppCompatActivity() {
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
                        painter = painterResource(id = R.drawable.bg),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(viewModelWorkout: WorkoutViewModel){
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = {2})
    Box(modifier = Modifier.fillMaxSize()){
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ){page ->
            when(page){
                0 -> HistoryCalendarScreen(viewModelWorkout)
                1 -> PushUpCounterScreen(viewModelWorkout)
            }
        }
    }
}