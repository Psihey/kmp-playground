package screen

import BirdAppTheme
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import co.touchlab.kermit.Logger
import com.myapplication.MR
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.BirdImage

class BirdsListScreen : Screen {
    @Composable
    override fun Content() {
        Logger.i("Open screen", tag = BirdsListScreen::class.simpleName.toString())
        val birdsViewModel = getScreenModel <BirdsViewModel>()
        BirdAppTheme {
            BirdsListScreen(birdsViewModel)
        }
    }
}

@Composable
fun BirdsListScreen(viewModel: BirdsViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val navigator = LocalNavigator.currentOrThrow

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AnimatedVisibility(uiState.selectedImages.isEmpty()){
            Column(
                Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(painter = painterResource(MR.images.bird_logo),contentDescription = null)
                Text(modifier = Modifier.padding(6.dp),text = stringResource(MR.strings.app_name), fontSize = 20.sp, color = Color.Black)
            }
        }
        Row(
            Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            for (category in uiState.categories) {
                Button(
                    onClick = {
                        viewModel.selectCategory(category)
                    },
                    modifier = Modifier.aspectRatio(1.0f).fillMaxSize().weight(1.0f),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        focusedElevation = 0.dp
                    )
                )
                {
                    Text(category)
                }
            }
        }
        AnimatedVisibility(uiState.selectedImages.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxSize().padding(horizontal = 5.dp),
                content = {
                    items(uiState.selectedImages) {
                        BirdImageCell(it,navigator)
                    }
                }
            )
        }
    }
}

@Composable
fun BirdImageCell(image: BirdImage, navigator: Navigator) {
    KamelImage(
        asyncPainterResource("https://sebastianaigner.github.io/demo-image-api/${image.path}"),
        "${image.category} by ${image.author}",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth().aspectRatio(1.0f).clickable {
            navigator.plusAssign(BirdDetailsScreen(image.path))
        }
    )
}