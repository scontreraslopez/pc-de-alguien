package net.iessochoa.sergiocontreras.pcdealguien

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import net.iessochoa.sergiocontreras.pcdealguien.ui.theme.PCdeAlguienTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PCdeAlguienTheme {
                //* Configuration for the Minimal Firebase UI Auth */
                val configuration = authUIConfiguration {
                    providers = listOf(
                        AuthProvider.Email(),
                        AuthProvider.Google()
                    )
                PokemonApp()
            }
        }
    }
}