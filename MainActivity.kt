package com.example.lazylist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lazylist.ui.Joke
import com.example.lazylist.ui.ViewModel

sealed class Screen(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    data object Jokes : Screen("Jokes", "Jokes", Icons.AutoMirrored.Filled.List)
    data object Favorites : Screen("Favorites", "Favorites", Icons.Filled.Favorite)
    data object Developer : Screen("Developer", "Developer", Icons.Filled.Info)
}

@Suppress("INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING") //was automatocally added by the IDE after hittng ALT+ENTER
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")//was automatocally added by the IDE after hittng ALT+ENTER
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: ViewModel = viewModel()

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "Jokes") },
                        actions = { ResetButton(viewModel) }
                    )
                },
                bottomBar = { BottomNavigationBar(navController) }
            ) { innerPadding ->
                NavigationGraph(navController, viewModel, innerPadding)
            }
        }
    }
}

@Composable
fun JokeItem(joke: Joke, onSwipeLeft: () -> Unit, onSwipeRight: () -> Unit) {
    var currentJoke by remember { mutableStateOf(joke) }
    var swipeDirection by remember { mutableStateOf(0) }

    AnimatedContent( // couldn't remember how to get the animations so I asked Gemini for help
        targetState = currentJoke,
        transitionSpec = {
            if (swipeDirection > 0) {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                ) togetherWith slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            } else {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                ) togetherWith slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        }, label = ""
    ) { joke ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        if (dragAmount < -100) {
                            swipeDirection = -1
                            onSwipeLeft()
                            currentJoke = Joke("", "")
                        } else if (dragAmount > 100) {
                            swipeDirection = 1
                            onSwipeRight()
                            currentJoke = Joke("", "")
                        }
                    }
                }
        ) {
            Text(
                joke.setup,
                modifier = Modifier.padding(16.dp),
            )
            Text(
                joke.punchline,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Composable
fun JokeListScreen(viewModel: ViewModel, innerPadding: PaddingValues) {
    val jokes = viewModel.jokes
    LazyColumn(
        modifier = Modifier.padding(innerPadding)
    ) {
        items(jokes) { joke ->
            JokeItem(
                joke,
                onSwipeLeft = { viewModel.removeJoke(joke) },
                onSwipeRight = { viewModel.favoriteJoke(joke) }
            )
        }
    }
}

@Composable
fun FavoriteJokeScreen(viewModel: ViewModel, innerPadding: PaddingValues) {
    val favorites = viewModel.favorites
    LazyColumn(
        modifier = Modifier.padding(innerPadding)
    ) {
        items(favorites) { joke ->
            JokeItem(
                joke,
                onSwipeLeft = { viewModel.removeFavorite(joke) },
                onSwipeRight = {}
            )
        }
    }
}

@Composable
fun DevScreen(innerPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.dev),
                contentDescription = "An Image of Yours Truly",
                modifier = Modifier
                    .size(650.dp)
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = "Patrick Nelson")

        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val screens = listOf(
        Screen.Jokes,
        Screen.Favorites,
        Screen.Developer,
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    viewModel: ViewModel,
    innerPadding: PaddingValues
) {
    NavHost(navController, startDestination = "Jokes") {
        composable("Jokes") { JokeListScreen(viewModel, innerPadding) }
        composable("Favorites") { FavoriteJokeScreen(viewModel, innerPadding) }
        composable("Developer") { DevScreen(innerPadding) }
    }
}

@Composable
fun ResetButton(viewModel: ViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    IconButton(onClick = { showDialog = true }) {
        Icon(Icons.Default.Refresh, contentDescription = "Reset")
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Reset Jokes") },
            text = { Text("Are you sure you want to reset the jokes?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.resetJokes()
                    showDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}









