package ru.vsu.cs.simplecinemaapp.view

import FilmViewModel
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.simplecinemaapp.GlideApp
import ru.vsu.cs.simplecinemaapp.R
import ru.vsu.cs.simplecinemaapp.holders.FilmDataHolder

class FilmOverviewFragment(private val aLoadingDialog: ALoadingDialog) :
    Fragment(R.layout.fragment_film_overview) {
    private val id = FilmDataHolder.filmOverviewId

    private lateinit var filmViewModel: FilmViewModel

    private lateinit var poster: ImageView
    private lateinit var filmTitle: TextView
    private lateinit var filmDescription: TextView
    private lateinit var genreContainer: LinearLayout
    private lateinit var countryContainer: LinearLayout
    private lateinit var filmGenres: TextView
    private lateinit var filmCountry: TextView

    private lateinit var updateWrapper: LinearLayout
    private lateinit var updateBtn: Button
    private lateinit var overviewContainer: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filmViewModel = ViewModelProvider(requireActivity())[FilmViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        poster = view.findViewById(R.id.posterView)
        filmTitle = view.findViewById(R.id.filmTitle)
        filmDescription = view.findViewById(R.id.filmDescription)
        genreContainer = view.findViewById(R.id.genreContainer)
        countryContainer = view.findViewById(R.id.countryContainer)
        filmGenres = view.findViewById(R.id.filmGenres)
        filmCountry = view.findViewById(R.id.filmCountry)

        overviewContainer = view.findViewById(R.id.overviewContainer)
        updateWrapper = view.findViewById(R.id.updateWrapper)
        updateBtn = view.findViewById(R.id.updateBtn)

        updateBtn.setOnClickListener {
            val tmp = id.value
            if (tmp != null && tmp > 0) {
                filmViewModel.getFilmById(tmp)
            } else {
                Toast.makeText(context, "Выберите фильм из списка", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        id.observe(viewLifecycleOwner) {
            filmViewModel.getFilmById(it)
        }

        filmViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                updateWrapper.visibility = View.GONE
                aLoadingDialog.show()
            } else {
                aLoadingDialog.cancel()
            }
        }
        filmViewModel.isError.observe(viewLifecycleOwner) { isError ->
            if (isError) {
                overviewContainer.visibility = View.GONE
                updateWrapper.visibility = View.VISIBLE
                Toast.makeText(
                    context, "Ошибка получения данных\n" + filmViewModel.errorMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        filmViewModel.filmData.observe(viewLifecycleOwner) { response ->
            response?.let {
                overviewContainer.visibility = View.VISIBLE

                filmTitle.text = "${it.nameRu} (${it.year} г.)"
                filmDescription.text = it.description
                filmGenres.text = it.genres?.joinToString { a -> a?.genre.toString() }
                filmCountry.text = it.countries?.joinToString { a -> a?.country.toString() }
                setResultImage(it.posterUrl)
            }
        }
    }

    private fun setResultImage(imageUrl: String?) {
        // Display image when image url is available
        imageUrl?.let { url ->
            GlideApp.with(this)
                .load(url)
                .into(poster)
            poster.visibility = View.VISIBLE
        } ?: run {
            // Hide image when image url is null
            poster.visibility = View.GONE
        }
    }
}
