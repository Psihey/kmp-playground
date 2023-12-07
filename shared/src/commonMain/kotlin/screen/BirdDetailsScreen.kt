package screen

import BirdAppTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import co.touchlab.kermit.Logger
import com.myapplication.MR
import dev.icerock.moko.resources.compose.painterResource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

data class BirdDetailsScreen(val path : String) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Logger.i("Open screen", tag = BirdDetailsScreen::class.simpleName.toString())
        Box(Modifier.fillMaxSize()) {
                Image(painter = painterResource(MR.images.back_arrow),
                    modifier = Modifier.clickable {navigator.pop()}.align(Alignment.TopStart).padding(16.dp).size(24.dp),
                    contentDescription = null)
                KamelImage(
                    asyncPainterResource("https://sebastianaigner.github.io/demo-image-api/${path}"),
                    path,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().align(Alignment.Center).aspectRatio(1.0f)
                )
            }
    }

}