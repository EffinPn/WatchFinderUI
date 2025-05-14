
package com.example.watchfinder


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.example.watchfinder.data.UserManager
import com.example.watchfinder.ui.theme.WatchFinderTheme
import com.example.watchfinder.data.prefs.TokenManager
import com.example.watchfinder.repository.AuthRepository
import com.example.watchfinder.screens.DiscoverMovies
import com.example.watchfinder.screens.DiscoverSeries
import com.example.watchfinder.screens.ForgotPassword
import com.example.watchfinder.screens.Login
import com.example.watchfinder.screens.MovieOrSeries
import com.example.watchfinder.screens.MyContent
import com.example.watchfinder.screens.Profile
import dagger.hilt.android.AndroidEntryPoint
import com.example.watchfinder.screens.Register
import com.example.watchfinder.data.dto.MovieCard
import com.example.watchfinder.data.dto.SeriesCard
import com.example.watchfinder.screens.*
import javax.inject.Inject
import com.example.watchfinder.data.dataStore
import com.example.watchfinder.screens.ResetPassword
import kotlinx.coroutines.flow.map


object AppPreferenceKeys {
    val DARK_MODE_ENABLED = booleanPreferencesKey("dark_mode")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager
    @Inject
    lateinit var userManager: UserManager
    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Iniciando...")

        handleIntent(intent)

        setContent {
            
            MainAppWithTheme()
        }
    }

    
    @Composable
    private fun MainAppWithTheme() {
        val context = LocalContext.current
        val systemInDarkTheme = isSystemInDarkTheme() 

        
        val isDarkModeUserPreference by context.dataStore.data
            .map { preferences ->
                preferences[AppPreferenceKeys.DARK_MODE_ENABLED] ?: systemInDarkTheme
            }
            .collectAsState(initial = systemInDarkTheme) 

        
        WatchFinderTheme(darkTheme = isDarkModeUserPreference) {
            AppNavigation(
                tokenManager = tokenManager,
                userManager = userManager,
                authRepository = authRepository
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val uri = intent?.data
        if (uri != null) {
            val token = uri.getQueryParameter("token")
            if (token != null) {
                Log.d("DeepLink", "Token para reset recibido: $token")
                userManager.setResetToken(token)
            }
        }
    }
}



enum class AuthState {
    LOADING, 
    AUTHENTICATED,
    UNAUTHENTICATED,
    PASSWORD_RESET,
    FORGOT_PASSWORD
}

enum class ShowScreen { LOGIN, REGISTER }


enum class MainAppScreen {
    DISCOVER, SEARCH, MY_CONTENT, PROFILE, SEARCH_RESULTS, DETAIL 
}

enum class DiscoverState {
    SELECTION,
    MOVIES,
    SERIES
}

@Composable
fun MainScreen(
    
    currentScreen: MainAppScreen,
    onScreenChange: (MainAppScreen) -> Unit,
    onLogout: () -> Unit,
    onAccountDeleted: () -> Unit,
    onNavigateToDetail: (itemType: String, itemId: String) -> Unit,
    
    onNavigateToResults: (movies: List<MovieCard>, series: List<SeriesCard>) -> Unit
) {
    var discoverScreenState by remember { mutableStateOf(DiscoverState.SELECTION) }

    Scaffold(
        topBar = { TopHeader() },
        bottomBar = {
            BottomBar(
                current = currentScreen, 
                click = { newScreen ->
                    onScreenChange(newScreen) 
                    if (newScreen == MainAppScreen.DISCOVER) {
                        
                        discoverScreenState = DiscoverState.SELECTION
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            when (currentScreen) {
                
                MainAppScreen.SEARCH -> Search(
                    onNavigateToDetail = onNavigateToDetail,
                    onNavigateToResults = onNavigateToResults 
                )

                MainAppScreen.MY_CONTENT -> MyContent(onNavigateToDetail = onNavigateToDetail)
                MainAppScreen.DISCOVER -> {
                    when (discoverScreenState) {
                        DiscoverState.SELECTION -> MovieOrSeries(
                            onMoviesClicked = { discoverScreenState = DiscoverState.MOVIES },
                            onSeriesClicked = { discoverScreenState = DiscoverState.SERIES }
                        )

                        DiscoverState.MOVIES -> DiscoverMovies(/* onNavigateToDetail si es necesario */) 
                        DiscoverState.SERIES -> DiscoverSeries(/* onNavigateToDetail si es necesario */)   
                        
                        
                    }
                }

                MainAppScreen.PROFILE -> Profile(
                    onLogoutClick = onLogout,
                    onAccountDeleted = onAccountDeleted
                )
                
                MainAppScreen.SEARCH_RESULTS -> {}
                MainAppScreen.DETAIL -> {}
            }
        }
    }
}

@Composable
fun BottomBar(
    current: MainAppScreen, 
    click: (MainAppScreen) -> Unit 
) {
    
    val items = listOf(
        MainAppScreen.SEARCH to Icons.Filled.Search,
        MainAppScreen.MY_CONTENT to Icons.Filled.Favorite,
        MainAppScreen.DISCOVER to Icons.Filled.Star,
        MainAppScreen.PROFILE to Icons.Filled.Person
    )

    NavigationBar {
        items.forEach { (screen, icon) ->
            
            val label = when (screen) {
                MainAppScreen.SEARCH -> "Buscar"
                MainAppScreen.MY_CONTENT -> "Mi Cont." 
                MainAppScreen.DISCOVER -> "Descubrir"
                MainAppScreen.PROFILE -> "Perfil"
                else -> "" 
            }
            NavigationBarItem(
                selected = (screen == current),
                onClick = { click(screen) }, 
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}


@Composable
fun TopHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
        , 
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Image(
            painter = painterResource(id = R.drawable.logocarta),
            contentDescription = "App Logo",
            modifier = Modifier.height(100.dp) 
        )
    }
}

@Composable
fun AppNavigation(
    tokenManager: TokenManager,
    userManager: UserManager,
    authRepository: AuthRepository
) {
    val context = LocalContext.current

    var loginOrRegister by remember { mutableStateOf(ShowScreen.LOGIN) }
    var authState by remember { mutableStateOf(AuthState.LOADING) }

    var previousMainScreen by remember { mutableStateOf(MainAppScreen.DISCOVER) }
    var currentMainScreen by remember { mutableStateOf(MainAppScreen.DISCOVER) }

    
    var searchResultsMoviesState by remember { mutableStateOf<List<MovieCard>>(emptyList()) }
    var searchResultsSeriesState by remember { mutableStateOf<List<SeriesCard>>(emptyList()) }

    
    var detailScreenInfo by remember { mutableStateOf<Pair<String, String>?>(null) }


    
    LaunchedEffect(Unit) {
        
        if (userManager.resetToken != null) {
            authState = AuthState.PASSWORD_RESET
            return@LaunchedEffect
        }
        val token = tokenManager.getToken()
        if (token == null) {
            authState = AuthState.UNAUTHENTICATED
            loginOrRegister = ShowScreen.LOGIN
        } else {
            val validationResult = authRepository.validate()
            if (validationResult.isSuccess) {
                authState = AuthState.AUTHENTICATED
                
                currentMainScreen = MainAppScreen.DISCOVER
            } else {
                authState = AuthState.UNAUTHENTICATED
                loginOrRegister = ShowScreen.LOGIN
            }
        }
    }

    

    when (authState) {
        AuthState.LOADING -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        AuthState.AUTHENTICATED -> {
            
            
            when (currentMainScreen) {
                
                MainAppScreen.DETAIL -> {
                    val (itemType, itemId) = detailScreenInfo!!
                    DetailScreen(
                        itemType = itemType,
                        itemId = itemId,
                        onNavigateBack = {
                            detailScreenInfo = null 
                            currentMainScreen =
                                previousMainScreen 
                        }
                    )
                }
                
                MainAppScreen.SEARCH_RESULTS -> {
                    SearchResultsScreen(
                        resultsMovies = searchResultsMoviesState,
                        resultsSeries = searchResultsSeriesState,
                        onNavigateBack = {
                            currentMainScreen = MainAppScreen.SEARCH
                        }, 
                        onNavigateToDetail = { type, id ->
                            previousMainScreen = currentMainScreen
                            detailScreenInfo = Pair(type, id) 
                            currentMainScreen = MainAppScreen.DETAIL 
                        }
                    )
                }
                
                else -> {
                    MainScreen(
                        currentScreen = currentMainScreen, 
                        onScreenChange = { newScreen ->
                            currentMainScreen =
                                newScreen 
                        },
                        onLogout = {
                            tokenManager.clearToken()
                            authState = AuthState.UNAUTHENTICATED
                            loginOrRegister = ShowScreen.LOGIN
                        },
                        
                        onNavigateToDetail = { type, id ->
                            previousMainScreen = currentMainScreen
                            detailScreenInfo = Pair(type, id)
                            currentMainScreen = MainAppScreen.DETAIL
                        },
                        
                        onNavigateToResults = { movies, series ->
                            searchResultsMoviesState = movies 
                            searchResultsSeriesState = series
                            currentMainScreen =
                                MainAppScreen.SEARCH_RESULTS 
                        },
                        onAccountDeleted = {
                            Log.d("AppNavigation", "Account deleted callback invoked")
                            authState = AuthState.UNAUTHENTICATED
                            Log.d("AppNavigation", "authState set to: $authState")
                            loginOrRegister = ShowScreen.LOGIN
                            Log.d("AppNavigation", "loginOrRegister set to: $loginOrRegister")
                        }
                    )
                }
            }
        }
        AuthState.UNAUTHENTICATED -> {
            
            when (loginOrRegister) {
                ShowScreen.LOGIN -> Login(
                    onLoginSuccess = {
                        authState = AuthState.AUTHENTICATED
                        currentMainScreen = MainAppScreen.DISCOVER 
                    },
                    onForgotPasswordClick = {
                        println("--> Redirecting to pass reset screen.")
                        
                        authState = AuthState.FORGOT_PASSWORD
                    },
                    onNavigateToRegister = { loginOrRegister = ShowScreen.REGISTER }
                )
                ShowScreen.REGISTER -> Register(
                    onRegisterSuccess = { loginOrRegister = ShowScreen.LOGIN },
                    onNavigateToLogin = { loginOrRegister = ShowScreen.LOGIN }
                )
            }
        }
        AuthState.PASSWORD_RESET -> {
            ResetPassword(
                token = userManager.resetToken ?:"",
                onSuccess = {
                    userManager.setResetToken(null)
                    authState = AuthState.UNAUTHENTICATED
                    loginOrRegister = ShowScreen.LOGIN
                },
                onCancel = {
                    userManager.setResetToken(null)
                    authState = AuthState.UNAUTHENTICATED
                    loginOrRegister = ShowScreen.LOGIN
                }
            )
        }
        AuthState.FORGOT_PASSWORD -> {
            ForgotPassword(
                onNavigateBack = {
                    authState = AuthState.UNAUTHENTICATED
                }
            )
        }
    }
}



/*
    
    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        WatchFinderTheme {
            MainScreen(
                currentScreen = MainAppScreen.DISCOVER, 
                onScreenChange = {},
                onLogout = {},
                onNavigateToDetail = { _, _ -> },
                onNavigateToResults = { _, _ -> } 
            )
        }
    }
*/
