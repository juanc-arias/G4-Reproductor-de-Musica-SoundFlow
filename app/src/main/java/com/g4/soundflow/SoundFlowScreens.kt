package com.g4.soundflow

import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

private val appBackground = Color(0xFFF7F8FB)
private val blue = Color(0xFF2563EB)
private val secondary = Color(0xFF64748B)

@Composable
private fun Title(text: String, caption: String) {
    Text(text, fontWeight = FontWeight.Bold, fontSize = 23.sp, lineHeight = 28.sp)
    Spacer(Modifier.height(4.dp))
    Text(caption, fontSize = 12.sp, color = secondary)
    Spacer(Modifier.height(15.dp))
}

@Composable
private fun SectionCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) { content() }
    }
}

@Composable
fun ProfileScreen() {
    Column(
        Modifier.fillMaxSize().background(appBackground).verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {
        Title("Perfil", "Información de una persona")
        SectionCard {
            Image(
                painter = painterResource(R.drawable.demo_avatar),
                contentDescription = "Fotografía ilustrativa de perfil",
                modifier = Modifier.fillMaxWidth().height(130.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.height(8.dp))
            Text("Alex Rivera", fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text("Productor musical — perfil de demostración", fontSize = 12.sp, color = secondary)
        }
        Spacer(Modifier.height(12.dp))
        SectionCard {
            Text("Estudios", fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Tecnología en Producción Musical. Formación complementaria en edición digital de audio y fundamentos de sonido.")
        }
        Spacer(Modifier.height(12.dp))
        SectionCard {
            Text("Experiencia profesional", fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Producción de pistas instrumentales de demostración, edición de audio, organización de bibliotecas musicales y manejo de herramientas digitales de sonido.")
        }
        Spacer(Modifier.height(16.dp))
    }
}

private data class PhotoInfo(val title: String, val description: String, val resId: Int)
private val photoItems = listOf(
    PhotoInfo("El vinilo", "Representación de un disco de vinilo y su relación con la música analógica.", R.drawable.demo_photo_1),
    PhotoInfo("Energía en vivo", "Luces, sonido y ambiente de una presentación musical.", R.drawable.demo_photo_2),
    PhotoInfo("Producción digital", "Mezcla y producción musical mediante herramientas digitales.", R.drawable.demo_photo_3)
)

@Composable
fun PhotosScreen() {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(appBackground).padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Title("Fotos", "Selecciona una imagen para ver su descripción") }
        itemsIndexed(photoItems) { index, photo ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable { selectedIndex = index },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(Modifier.padding(10.dp)) {
                    Image(
                        painter = painterResource(photo.resId),
                        contentDescription = photo.title,
                        modifier = Modifier.fillMaxWidth().height(130.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(photo.title, fontWeight = FontWeight.Bold)
                    if (index == selectedIndex) {
                        Spacer(Modifier.height(5.dp))
                        Text(photo.description, fontSize = 12.sp, color = secondary)
                    } else {
                        Text("Toca para consultar", fontSize = 11.sp, color = blue)
                    }
                }
            }
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoScreen() {
    val context = LocalContext.current
    val player = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(
                MediaItem.fromUri(
                    Uri.parse("android.resource://${context.packageName}/${R.raw.demo_video_compatible}")
                )
            )
            repeatMode = Player.REPEAT_MODE_ONE
            prepare()
        }
    }
    DisposableEffect(player) { onDispose { player.release() } }
    Column(Modifier.fillMaxSize().background(appBackground).verticalScroll(rememberScrollState()).padding(12.dp)) {
        Title("Video", "Contenido audiovisual dentro de SoundFlow")
        SectionCard {
            AndroidView(
                factory = { PlayerView(it).apply { this.player = player; useController = true } },
                modifier = Modifier.fillMaxWidth().height(185.dp)
            )
            Spacer(Modifier.height(10.dp))
            Text("Video musical de demostración", fontWeight = FontWeight.Bold)
            Text("Usa los controles integrados para reproducir, pausar y avanzar.", fontSize = 12.sp, color = secondary)
            Spacer(Modifier.height(8.dp))
            Button(onClick = { player.play() }) { Text("Reproducir video") }
        }
    }
}

@Composable
fun WebScreen() {
    val context = LocalContext.current
    var url by rememberSaveable { mutableStateOf("https://example.com") }
    var error by remember { mutableStateOf("") }
    val browser = remember(context) {
        WebView(context).apply {
            settings.javaScriptEnabled = false
            webViewClient = WebViewClient()
            loadUrl("https://example.com")
        }
    }
    DisposableEffect(browser) {
        onDispose { browser.stopLoading(); browser.destroy() }
    }
    Column(Modifier.fillMaxSize().background(appBackground).padding(12.dp)) {
        Title("Web", "Navegador integrado")
        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("Dirección web") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(6.dp))
        Button(onClick = {
            val input = url.trim()
            val normalized = if (input.startsWith("https://", true) || input.startsWith("http://", true)) input else "https://$input"
            val parsed = Uri.parse(normalized)
            if (parsed.host.isNullOrBlank() || parsed.scheme != "https") {
                error = "Ingresa una URL HTTPS válida."
            } else {
                error = ""
                browser.loadUrl(normalized)
            }
        }) { Text("Cargar página") }
        if (error.isNotEmpty()) Text(error, fontSize = 12.sp, color = Color(0xFFB91C1C))
        Spacer(Modifier.height(8.dp))
        Card(modifier = Modifier.fillMaxWidth().weight(1f), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            AndroidView(factory = { browser }, modifier = Modifier.fillMaxSize())
        }
    }
}

private data class Track(val title: String, val resId: Int)
private val demoTracks = listOf(
    Track("Neon Pulse", R.raw.demo_song_1),
    Track("Ruta Sonora", R.raw.demo_song_2),
    Track("Horizonte Digital", R.raw.demo_song_3)
)

@Composable
fun MusicScreen() {
    val context = LocalContext.current
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var playing by remember { mutableStateOf(false) }
    val player = remember(context) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItems(demoTracks.map { track ->
                MediaItem.fromUri(Uri.parse("android.resource://${context.packageName}/${track.resId}"))
            })
            repeatMode = Player.REPEAT_MODE_ALL
            prepare()
        }
    }
    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) { playing = isPlaying }
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                currentIndex = player.currentMediaItemIndex.coerceAtLeast(0)
            }
        }
        player.addListener(listener)
        onDispose { player.removeListener(listener); player.release() }
    }
    fun changeTrack(delta: Int) {
        val next = (currentIndex + delta + demoTracks.size) % demoTracks.size
        currentIndex = next
        player.seekToDefaultPosition(next)
        player.play()
    }
    Column(Modifier.fillMaxSize().background(appBackground).verticalScroll(rememberScrollState()).padding(12.dp)) {
        Title("Botones", "Reproductor musical")
        SectionCard {
            Image(
                painter = painterResource(R.drawable.demo_photo_1),
                contentDescription = "Portada de la pista",
                modifier = Modifier.fillMaxWidth().height(150.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(12.dp))
            Text(demoTracks[currentIndex].title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("Pista de audio original de demostración", fontSize = 11.sp, color = secondary)
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = { changeTrack(-1) }, contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)) {
                    Text("⏮")
                }
                Button(onClick = {
                    if (playing) player.pause() else player.play()
                }, contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 9.dp)) {
                    Text(if (playing) "Pausa" else "▶")
                }
                Button(onClick = { changeTrack(1) }, contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)) {
                    Text("⏭")
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(if (playing) "Reproduciendo" else "En pausa", color = blue, fontSize = 12.sp)
        }
    }
}
