package com.example.sporttracker


// Не забудь додати цей імпорт зверху:
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

        val database = WorkoutDatabase.getDatabase(applicationContext)
        val factory = WorkoutViewModelFactory(database.workoutDao(), application)

        setContent {
            val viewModelWorkout: WorkoutViewModel = viewModel(factory = factory)

            // Тепер при зміні у ViewModel весь UI автоматично перемалюється
            val currentTheme by viewModelWorkout.themeMode.collectAsState()

            // 3. Передаємо значення в тему
            SportTrackerTheme(themeMode = currentTheme) {
                MainScreen(viewModelWorkout)
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