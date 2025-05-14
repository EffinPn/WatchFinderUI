import com.example.watchfinder.data.dto.MovieCard
import com.example.watchfinder.data.dto.SeriesCard

data class MyContentUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentView: MyContentView = MyContentView.BUTTONS,
    val favoriteMovies: List<MovieCard?> = emptyList(),
    val favoriteSeries: List<SeriesCard?> = emptyList()
)