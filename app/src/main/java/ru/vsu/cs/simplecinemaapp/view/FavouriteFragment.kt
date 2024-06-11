package ru.vsu.cs.simplecinemaapp.view

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.simplecinemaapp.R
import ru.vsu.cs.simplecinemaapp.adapter.ClickListener
import ru.vsu.cs.simplecinemaapp.adapter.HoldListener
import ru.vsu.cs.simplecinemaapp.adapter.ListAdapter
import ru.vsu.cs.simplecinemaapp.holders.FilmDataHolder
import ru.vsu.cs.simplecinemaapp.model.Film

class FavouriteFragment : Fragment(R.layout.fragment_favourite) {

    private val filmId = FilmDataHolder.filmOverviewId
    private val favList = FilmDataHolder.favouriteFilmList

    private lateinit var adapter: ListAdapter
    private lateinit var emptyWrapper: LinearLayout
    private lateinit var favRecyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favRecyclerView = view.findViewById(R.id.favFilmListView)
        favRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        favRecyclerView.setHasFixedSize(true)

        adapter = ListAdapter(
            favList.value!!,
            requireActivity().applicationContext,
            clickListener,
            holdListener
        )

        emptyWrapper = view.findViewById(R.id.emptyWrapper)
        emptyWrapper.visibility = View.GONE
        favRecyclerView.adapter = adapter

        favList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                favRecyclerView.visibility = View.GONE
                emptyWrapper.visibility = View.VISIBLE
            } else {
                emptyWrapper.visibility = View.GONE
                favRecyclerView.visibility = View.VISIBLE
            }
            adapter.setLogs(it)
        }

    }

    private val clickListener = object : ClickListener {
        override fun onItemClick(film: Film) {
            filmId.postValue(film.filmId!!)
            val mainActivity = requireActivity() as MainActivity
            mainActivity.changePageToFilmOverview()
            println("Clicked on film ${film.nameRu}")
        }
    }

    private val holdListener = object : HoldListener {
        override fun onItemHold(film: Film, position: Int) {
            val currentList = favList.value?.toMutableList() ?: mutableListOf()
            if (currentList.contains(film)) {
                currentList.remove(film)
                favList.postValue(currentList)
                val toast = Toast.makeText(
                    context, "Фильм ${film.nameRu} был удален из избранного", Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }
    }

}