package com.example.weatherapp.ui

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.model.City
import com.example.weatherapp.model.Weather
import com.example.weatherapp.ui.nav.Route
import androidx.activity.compose.LocalActivity

@Composable
fun CityItem(
    city: City,
    weather: Weather,
    onClick: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val desc = if (weather == Weather.LOADING) "Carregando clima..." else weather.desc
    Row(
        modifier = modifier.fillMaxWidth().padding(8.dp).clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage( // Substitui o Icon(...)
            model = weather.imgUrl,
            modifier = modifier.size(75.dp),
            error = painterResource(id = R.drawable.loading),
            contentDescription = "Imagem"
        )
        Spacer(modifier = Modifier.size(12.dp))
        Column(modifier = modifier.weight(1f)) {
            Text(modifier = Modifier,
                text = city.name,
                fontSize = 24.sp)
            Text(modifier = Modifier,
                text = desc,
                fontSize = 16.sp)
        }
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
        }
    }
}

@Preview
@Composable
fun ListPage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    // O PDF usa LocalContext.current as? Activity; o lint acusa erro (ContextCastToActivity)
    // e recomenda LocalActivity, que usamos desde a Prática 1.
    val activity = LocalActivity.current
    val cityList = viewModel.cities
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(items = cityList, key = { it.name }) { city ->
            CityItem(city = city, weather = viewModel.weather(city.name), onClose = {
                viewModel.remove(city)
            }, onClick = {
                viewModel.city = city.name
                viewModel.page = Route.Home
            })
        }
    }
}
