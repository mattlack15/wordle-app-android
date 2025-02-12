package ca.mattlack.wordleapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.mattlack.wordleapp.engine.ApiWordleEngine
import ca.mattlack.wordleapp.engine.DefaultWordleEngine
import ca.mattlack.wordleapp.engine.WordleApi
import ca.mattlack.wordleapp.game.WordleGame
import ca.mattlack.wordleapp.screen.DaysScreen
import ca.mattlack.wordleapp.screen.Screen
import ca.mattlack.wordleapp.screen.WordleScreen
import ca.mattlack.wordleapp.ui.theme.WordleAppTheme
import ca.mattlack.wordleapp.viewmodel.DaysViewModel
import ca.mattlack.wordleapp.viewmodel.WordleViewModel
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import kotlin.math.log

class MainActivity : ComponentActivity() {

//    private val retrofitApi: Retrofit = Retrofit.Builder()
//        .baseUrl("http://10.0.2.2:8080")
//        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
//        .build()
//
//    private val api = retrofitApi.create<WordleApi>()
//    private val engine = ApiWordleEngine(api)
    private val engine = DefaultWordleEngine()
    val viewModel: WordleViewModel by viewModels<WordleViewModel>(
        factoryProducer = {
            viewModelFactory {
                initializer {
                    WordleViewModel(WordleGame("hello", engine))
                }
            }
        }
    )

    val daysViewModel: DaysViewModel by viewModels<DaysViewModel>(
        factoryProducer = {
            viewModelFactory {
                initializer {
                    DaysViewModel(engine)
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WordleAppTheme {
                val snackState = remember { SnackbarHostState() }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackState) }) { innerPadding ->

                    val nav = rememberNavController()
                    NavHost(nav, "days") {
                        composable(Screen.Days.route) {
                            val loading by daysViewModel.loading.collectAsState()
                            val days by daysViewModel.days.collectAsState()
                            if (loading) {
                                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                DaysScreen(modifier = Modifier.fillMaxSize().padding(innerPadding), days = days, onSelectDay = {
                                    nav.navigate(Screen.Play.withArgs("word" to it.word))
                                })
                            }
                        }
                        composable("${Screen.Play.route}?word={word}") { entry ->
                            val word = entry.arguments?.getString("word") ?: "hello"
                            viewModel.game.word = word
                            val state by viewModel.state.collectAsState()

                            LaunchedEffect(true) {
                                viewModel.game.reset()
                                viewModel.uiEvents.collect {
                                    when (it) {
                                        is WordleViewModel.UIEvent.Snackbar -> {
                                            Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                                        }
                                        is WordleViewModel.UIEvent.NavigateBack -> {
                                            nav.popBackStack()
                                        }
                                    }
                                }
                            }
                            WordleScreen(state, viewModel::onEvent, Modifier.padding(innerPadding))
                        }
                    }

                }
            }
        }
    }
}