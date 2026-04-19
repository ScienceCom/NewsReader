package com.example.newswreader.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newswreader.model.Note
import com.example.newswreader.network.NewsRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen() {
    val repository = remember { NewsRepository() }
    val scope = rememberCoroutineScope()

    var posts by remember { mutableStateOf<List<Note>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedPost by remember { mutableStateOf<Note?>(null) }

    val loadData = {
        scope.launch {
            isLoading = true
            errorMessage = null
            try {
                posts = repository.fetchNews()
            } catch (e: Exception) {
                errorMessage = e.message ?: "Koneksi Gagal"
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) { loadData() }

    if (selectedPost != null) {
        // --- DETAIL SCREEN ---
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detail Berita") },
                    navigationIcon = {
                        // Pakai TextButton pengganti Icon Back
                        TextButton(onClick = { selectedPost = null }) {
                            Text("<< Back", fontSize = 16.sp)
                        }
                    }
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                Text(selectedPost!!.title, style = MaterialTheme.typography.headlineSmall)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                Text(selectedPost!!.body, style = MaterialTheme.typography.bodyLarge)
            }
        }
    } else {
        // --- LIST SCREEN ---
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("News Reader") },
                    actions = {
                        // Pakai TextButton pengganti Icon Refresh
                        TextButton(onClick = { loadData() }) {
                            Text("REFRESH ↻", fontSize = 14.sp)
                        }
                    }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (errorMessage != null) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: $errorMessage", color = Color.Red)
                        Button(onClick = { loadData() }, modifier = Modifier.padding(top = 8.dp)) {
                            Text("COBA LAGI")
                        }
                    }
                } else {
                    LazyColumn {
                        items(posts) { post ->
                            Card(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .clickable { selectedPost = post },
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(post.title, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                                    Text(post.body, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}