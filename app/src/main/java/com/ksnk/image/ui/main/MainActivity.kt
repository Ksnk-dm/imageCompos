package com.ksnk.image.ui.main

import android.app.WallpaperManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ksnk.image.DataItem
import com.ksnk.image.R
import com.ksnk.image.ui.theme.ImageTrumpTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.net.URL

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        viewModel.expandShortenedUrl()

        setContent {
            ImageTrumpTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val expandedUrlState by viewModel.getExpandedUrlLiveData().observeAsState()
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = ROUTE_SPLASH_SCREEN
                    ) {
                        composable(ROUTE_HOME_SCREEN) {
                            HomeScreen(
                                data = expandedUrlState.orEmpty(),
                                navController = navController
                            )
                        }
                        composable(ROUTE_PICTURE_SCREEN) {
                            PictureScreen(navController)
                        }

                        composable(ROUTE_SPLASH_SCREEN) {
                            SplashScreen(navController)
                        }
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PictureScreen(navController: NavController) {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val msg = navController.previousBackStackEntry?.savedStateHandle?.get<String>(KEY_URL)
            Image(
                painter = rememberAsyncImagePainter(msg),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            TopAppBar(
                title = { Text("") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Transparent),
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "",
                            modifier = Modifier.size(48.dp)
                        )
                    }
                },
            )
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.BottomEnd)
            ) {

                IconButton(
                    onClick = { viewModel.downloadFile(msg ?: "", context) },
                    modifier = Modifier.padding(bottom = 15.dp)
                ) {
                    Icon(
                        painterResource(id = R.drawable.baseline_downloading_24),
                        contentDescription = "",
                        modifier = Modifier.size(48.dp)
                    )
                }

                IconButton(
                    onClick = {
                        val wallpaperManager: WallpaperManager =
                            WallpaperManager.getInstance(context)

                        coroutineScope.launch {
                            val task = async(Dispatchers.IO) {
                                BitmapFactory.decodeStream(
                                    URL(msg).openConnection().getInputStream()
                                )
                            }

                            wallpaperManager.setBitmap(
                                task.await(),
                                null,
                                false,
                                WallpaperManager.FLAG_SYSTEM
                            )

                            Toast.makeText(context, "Wallpaper set", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.padding(bottom = 15.dp)
                ) {
                    Icon(
                        painterResource(id = R.drawable.baseline_app_settings_alt_24),
                        contentDescription = "",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun SplashScreen(navController: NavController) {
        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.splash))

        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            composition = composition,
            progress = {
                progress
            }
        )
        LaunchedEffect(Unit) {
            delay(2000)
            navController.navigate(ROUTE_HOME_SCREEN) {
                popUpTo(ROUTE_SPLASH_SCREEN) {
                    inclusive = true
                }
            }
        }
    }

    @Composable
    fun HomeScreen(
        data: List<DataItem>?,
        navController: NavController
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(24.dp)
        ) {
            data?.forEachIndexed { _, item ->
                item(span = { GridItemSpan(1) }) {
                    ImageCard(dataItem = item, navController)
                }
//                if (index % 10 == 0) {
//                    item(span = { GridItemSpan(maxLineSpan) }) {
//                        ImageCard(dataItem = item, navController)
//                    }
//                } else {
//                    item(span = { GridItemSpan(1) }) {
//                        ImageCard(dataItem = item, navController)
//                    }
//                }
            }
        }
    }


    @Composable
    fun ImageCard(
        dataItem: DataItem,
        navController: NavController
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .height(280.dp)
                .width(200.dp)
        ) {
            Image(
                rememberAsyncImagePainter(dataItem.url),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clickable {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            KEY_URL,
                            dataItem.url
                        )
                        navController.navigate(ROUTE_PICTURE_SCREEN)
                    },

                contentScale = ContentScale.Crop,
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(
                        top = 48.dp,
                        bottom = 16.dp
                    )
            ) {
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = ROUTE_SPLASH_SCREEN
        ) {
            composable(ROUTE_HOME_SCREEN) {
                HomeScreen(
                    data = viewModel.getExpandedUrlLiveData().value,
                    navController = navController
                )
            }
            composable(ROUTE_PICTURE_SCREEN) {
                PictureScreen(navController)
            }
            composable(ROUTE_SPLASH_SCREEN) {
                SplashScreen(navController = navController)
            }
        }
    }

    companion object {
        const val ROUTE_HOME_SCREEN = "home_screen"
        const val ROUTE_PICTURE_SCREEN = "picture_screen"
        const val ROUTE_SPLASH_SCREEN = "splash_screen"

        const val KEY_URL = "img"
    }
}