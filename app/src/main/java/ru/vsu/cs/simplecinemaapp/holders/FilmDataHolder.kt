package ru.vsu.cs.simplecinemaapp.holders

import androidx.lifecycle.MutableLiveData
import ru.vsu.cs.simplecinemaapp.model.Film
import java.util.ArrayList

object FilmDataHolder {
    // используется для просмотра фильма, при его изменении меняется содержимое FilmOverviewFragment
    val filmOverviewId = MutableLiveData<Int>()

    // актуальный список с избранными фильмами
    val favouriteFilmList = MutableLiveData<List<Film>>().apply { value = ArrayList() }
}
