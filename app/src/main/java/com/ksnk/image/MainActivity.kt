package com.ksnk.image

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.ksnk.image.ui.theme.ImageTrumpTheme
import org.koin.java.KoinJavaComponent.inject
import java.util.UUID
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by inject()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageTrumpTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "firstscreen"
                    ) {
                        composable("firstscreen") {
                            MyComposeList(
                                data = generateData(), modifier = Modifier
                                    .fillMaxWidth(),
                                navController = navController
                            )
                        }
                        composable("secondscreen") {
                            SecondScreen(navController)
                        }
                    }
                }
            }
        }
    }


    fun generateData(): MutableList<DataItem> {
        val data = mutableListOf<DataItem>()
        data.add(
            DataItem(
                "Tesla Model S",
                "https://stimg.cardekho.com/images/carexteriorimages/930x620/Tesla/Model-S/5252/1611840999494/front-left-side-47.jpg"
            )
        )
        data.add(
            DataItem(
                "Testla Model 3",
                "https://images.91wheels.com//assets/c_images/gallery/tesla/model-3/tesla-model-3-0-1626249225.jpg"
            )
        )
        data.add(
            DataItem(
                "Tesla CyberTruck",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/Tesla_Cybertruck_outside_unveil_modified_by_Smnt.jpg/640px-Tesla_Cybertruck_outside_unveil_modified_by_Smnt.jpg"
            )
        )
        data.add(
            DataItem(
                "Tesla CyberTruck",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/Tesla_Cybertruck_outside_unveil_modified_by_Smnt.jpg/640px-Tesla_Cybertruck_outside_unveil_modified_by_Smnt.jpg"
            )
        )

        data.add(
            DataItem(
                "Tesla CyberTruck",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/Tesla_Cybertruck_outside_unveil_modified_by_Smnt.jpg/640px-Tesla_Cybertruck_outside_unveil_modified_by_Smnt.jpg"
            )
        )

        data.add(
            DataItem(
                "Tesla CyberTruck",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/Tesla_Cybertruck_outside_unveil_modified_by_Smnt.jpg/640px-Tesla_Cybertruck_outside_unveil_modified_by_Smnt.jpg"
            )
        )

        data.add(
            DataItem(
                "Tesla CyberTruck",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/Tesla_Cybertruck_outside_unveil_modified_by_Smnt.jpg/640px-Tesla_Cybertruck_outside_unveil_modified_by_Smnt.jpg"
            )
        )
        return data
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SecondScreen(navController: NavController) {
        val context = LocalContext.current

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val msg = navController.previousBackStackEntry?.savedStateHandle?.get<String>("msg")
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
                            contentDescription = "Назад",
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
                    onClick = { /* Действие для первой иконки */ },
                    modifier = Modifier.padding(bottom = 15.dp)
                ) {
                    Icon(
                        Icons.Filled.Share,
                        contentDescription = "Первая иконка",
                        modifier = Modifier.size(48.dp)
                    )
                }
                IconButton(
                    onClick = { viewModel.downloadFile(msg ?: "", context) },
                    modifier = Modifier.padding(bottom = 15.dp)
                ) {
                    Icon(
                        painterResource(id = R.drawable.baseline_downloading_24),
                        contentDescription = "Вторая иконка",
                        modifier = Modifier.size(48.dp)
                    )
                }

                IconButton(
                    onClick = {
                        val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(context)
                        val bitmap = BitmapFactory.decodeFile(
                            viewModel.downloadFile(
                                msg ?: "",
                                context
                            )?.absolutePath
                        )
                        wallpaperManager.setBitmap(bitmap)
                    },
                    modifier = Modifier.padding(bottom = 15.dp)
                ) {
                    Icon(
                        painterResource(id = R.drawable.baseline_app_settings_alt_24),
                        contentDescription = "Вторая иконка",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun MyComposeList(
        modifier: Modifier = Modifier,
        data: List<DataItem>,
        navController: NavController
    ) {
        LazyVerticalGrid(
            columns = object : GridCells {
                override fun Density.calculateCrossAxisCellSizes(
                    availableSize: Int,
                    spacing: Int
                ): List<Int> {
                    val firstColumn = (availableSize - spacing) * 2 / 3
                    val secondColumn = availableSize - spacing - firstColumn
                    return listOf(firstColumn, secondColumn)
                }
            },
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(24.dp)
        ) {
            data.forEachIndexed { index, item ->
                if (index % 3 == 0) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        MyCard(dataItem = item, navController)
                    }
                } else {
                    item(span = { GridItemSpan(1) }) {
                        MyCard(dataItem = item, navController)
                    }
                }
            }
        }
    }


    @Composable
    fun MyCard(
        dataItem: DataItem,
        navController: NavController
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .height(180.dp)
                .width(140.dp)
        ) {
            Image(
                rememberAsyncImagePainter(dataItem.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clickable {
                        Log.d("MESSAGE::: ", dataItem.image.toString())
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "msg",
                            dataItem.image
                        )
                        navController.navigate("secondscreen")
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
                Text(
                    text = dataItem.text.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = dataItem.text.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "secondscreen"
        ) {
            composable("firstscreen") {
                MyComposeList(
                    data = generateData(), modifier = Modifier
                        .fillMaxWidth(),
                    navController = navController
                )
            }
            composable("secondscreen") {
                SecondScreen(navController)
            }
        }
//    MyComposeList(
//        data = generateData(), modifier = Modifier
//            .fillMaxWidth(), navController =navController
//
//    )
    }
}