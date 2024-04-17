package com.ksnk.image.ui.main

import android.app.WallpaperManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
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
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.ksnk.image.remote.model.DataModel
import com.ksnk.image.ui.theme.ImageIsraelTheme
import com.ksnk.israelimage.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.File
import java.net.URL
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this) {}

        viewModel.loadDataFromUrl()

        setContent {
            ImageIsraelTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val urlState by viewModel.urlLiveData().observeAsState()
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = ROUTE_SPLASH_SCREEN
                    ) {
                        composable(ROUTE_HOME_SCREEN) {
                            HomeScreen(
                                data = urlState.orEmpty(),
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

    @Composable
    fun AdmobBanner(modifier: Modifier = Modifier) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = AD_ID_BANNER
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PictureScreen(navController: NavController) {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val url = navController.previousBackStackEntry?.savedStateHandle?.get<String>(KEY_URL)
            Image(
                painter = rememberAsyncImagePainter(url),
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
                    onClick = { viewModel.downloadFile(url ?: "", context) },
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
                                    URL(url).openConnection().getInputStream()
                                )
                            }

                            wallpaperManager.setBitmap(
                                task.await(),
                                null,
                                false,
                                WallpaperManager.FLAG_SYSTEM
                            )

                            Toast.makeText(
                                context,
                                context.getText(R.string.set),
                                Toast.LENGTH_SHORT
                            ).show()
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
        Column(
            modifier = Modifier
                .background(Color.LightGray)
        ) {
            LottieAnimation(
                composition = composition,
                progress = {
                    progress
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getString(R.string.app_name),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    color = Black
                )
            }
        }
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
        data: List<DataModel>?,
        navController: NavController
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(24.dp)
            ) {
                data?.forEachIndexed { _, item ->
                    item(span = { GridItemSpan(1) }) {
                        ImageCard(dataModel = item, navController)
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.BottomCenter)
            ) { AdmobBanner(modifier = Modifier.fillMaxWidth()) }
        }
    }

    @Composable
    fun ImageCard(
        dataModel: DataModel,
        navController: NavController
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .height(280.dp)
                .width(200.dp)
        ) {
            Image(
                rememberAsyncImagePainter(dataModel.url),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clickable {
                        if (Random.nextInt(0, 11) % RANDOM == 0) showInterstialAd(
                            navController,
                            dataModel.url ?: ""
                        )
                        else navigateToPicture(navController, dataModel.url)
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
                    data = viewModel.urlLiveData().value,
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

    private fun navigateToPicture(navController: NavController, url: String?) {
        navController.currentBackStackEntry?.savedStateHandle?.set(
            KEY_URL,
            url
        )
        navController.navigate(ROUTE_PICTURE_SCREEN)
    }

    private fun showInterstialAd(navController: NavController, url: String) {
        InterstitialAd.load(
            this,
            AD_ID_INTERESTIAL,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    interstitialAd.show(this@MainActivity)
                    interstitialAd.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdClicked() {
                                super.onAdClicked()
                                navigateToPicture(navController, url)
                            }

                            override fun onAdShowedFullScreenContent() {
                                super.onAdShowedFullScreenContent()
                                navigateToPicture(navController, url)
                            }
                        }
                }
            }
        )
    }

    companion object {
        private const val ROUTE_HOME_SCREEN = "home_screen"
        private const val ROUTE_PICTURE_SCREEN = "picture_screen"
        private const val ROUTE_SPLASH_SCREEN = "splash_screen"

        private const val KEY_URL = "img"

        private const val AD_ID_INTERESTIAL = "ca-app-pub-2981423664535117/4143256887"
        private const val AD_ID_BANNER = "ca-app-pub-2981423664535117/5699881134"

        private const val RANDOM = 2

        object FILE {
            val ENVIRONMENT = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "USA IMAGES/IMAGES"
            )

            const val TITLE = "Picture"
            const val MIME_TYPE = "jpg/*"
            const val FORMAT = ".jpg"
        }
    }
}