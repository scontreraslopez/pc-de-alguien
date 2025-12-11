package net.iessochoa.sergiocontreras.pcdealguien.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.iessochoa.sergiocontreras.pcdealguien.network.PokemonDto
import net.iessochoa.sergiocontreras.pcdealguien.ui.components.DynamicSelectTextField
import net.iessochoa.sergiocontreras.pcdealguien.ui.theme.Typography

@Composable
fun PokemonScreen(
    uiState: PokemonUiState,
    onFetchClick: (Int) -> Unit,
    onGenerationSelection: (String) -> Unit,
    modifier: Modifier = Modifier

) {
    // Observamos el estado del ViewModel
    //val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // Fijate que esto no puedo hacerlo por que no siempre habrá lista val pokemonList = uiState.requestStatus.pokemons
    val generationsCount = uiState.generations
    val selectedGen = uiState.selectedGeneration

    // Variables para el Dropdown (UI ya resuelta)
    val generations = (1..generationsCount).toList() // Generations

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pokedex Retrofit", style = Typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // --- SELECTOR DE GENERACIÓN (Ya implementado) ---
        DynamicSelectTextField(
            selectedValue = selectedGen.toString(),
            options = generations.map { it.toString() },
            label = "Generación",
            onValueChangedEvent = onGenerationSelection

        )


        Spacer(modifier = Modifier.height(10.dp))

        // --- BOTÓN DE BÚSQUEDA ---
        Button(
            onClick = {
                // Llama al ViewModel
                onFetchClick(selectedGen)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Recuperar Pokémon")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- LISTA DE RESULTADOS --- -> Función del estado
        when (val status = uiState.requestStatus) {
            is RequestStatus.Loading -> CircularProgressIndicator()
            is RequestStatus.Error -> Text("Error")
            is RequestStatus.Success -> {
                // Aquí pintamos la lista
                LazyVerticalGrid(
                    modifier = Modifier.weight(1f),
                    columns = GridCells.Fixed(2), // 2 columnas
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(status.pokemons) { pokemon ->
                        PokemonItem(pokemon)
                    }
                }
            }

            is RequestStatus.Idle -> Text("Selecciona una generación y pulsa buscar")
        }


    }
}


@Composable
fun PokemonItem(pokemon: PokemonDto) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp).fillMaxWidth()
        ) {
            // TODO 5: Construir la URL de la imagen
            // 🟢 DONE:
            // 1. Obtener el ID desde la URL del pokemon (pokemon.url)
            // 2. Usar: https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/{id}.png

            // Como la URL del pokemon tiene esta pinta: "https://pokeapi.co/api/v2/pokemon-species/4/ lo que me insteresa es extraer el 4
            // Si leemos el méthod split veremos que si usamos / como delimitador devolverá el siguiente array: [https:, , pokeapi.co, api, v2, pokemon-species, 4, ], pues nos interesa coger el penúltimo y ya está

            val splitUrl = pokemon.url.split("/")
            val pokemonId = splitUrl[splitUrl.lastIndex - 1] //Hay mil maneras similares de lograr lo mismo
            val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$pokemonId.png"

            // Esta es la magia de Coil
            AsyncImage(
                model = imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier.size(100.dp)
            )

            Text(
                text = pokemon.name.uppercase(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
