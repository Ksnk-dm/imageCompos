package com.ksnk.image

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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

class MainActivity : ComponentActivity() {
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
//                    MyComposeList(
//                        data = generateData(), modifier = Modifier
//                            .fillMaxWidth()
//
//                    )
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

@Composable
fun SecondScreen(navController: NavController) {

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            navController.popBackStack()
        }) {
            Text(text = "Submit")
        }
    }}

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
                .fillMaxHeight().clickable {
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