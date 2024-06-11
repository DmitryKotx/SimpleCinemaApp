package ru.vsu.cs.simplecinemaapp.view

import MainViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.vsu.cs.simplecinemaapp.R
import ru.vsu.cs.simplecinemaapp.adapter.HoldListener
import ru.vsu.cs.simplecinemaapp.adapter.ListAdapter
import ru.vsu.cs.simplecinemaapp.adapter.ClickListener
import ru.vsu.cs.simplecinemaapp.holders.FilmDataHolder
import ru.vsu.cs.simplecinemaapp.model.Film


class PopularFragment(private val aLoadingDialog: ALoadingDialog) : Fragment(R.layout.fragment_popular) {

    private var page = 1
    private var pageCount: Int? = 0
    private val filmList = ArrayList<Film>()
    private val filmId = FilmDataHolder.filmOverviewId
    private val favList = FilmDataHolder.favouriteFilmList

    private lateinit var viewModel: MainViewModel

    private lateinit var adapter: ListAdapter
    private lateinit var updateWrapper: LinearLayout
    private lateinit var updateBtn: Button
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        recyclerView = view.findViewById(R.id.filmListView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        adapter = ListAdapter(filmList, requireContext(), clickListener, holdListener)

        updateBtn = view.findViewById(R.id.updateBtn)
        updateWrapper = view.findViewById(R.id.updateWrapper)
        updateWrapper.visibility = View.GONE

        updateBtn.setOnClickListener {
            viewModel.getTopFilms(page)
        }

        favList.observe(viewLifecycleOwner){
            adapter.notifyDataSetChanged()
        }

        setupRecyclerView()
        viewModelSubscribe()
    }

    private fun setupRecyclerView() {
        recyclerView.adapter = adapter
        // Настройка RecyclerView, чтобы при конце прокрутки доставились новые фильмы
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && page <= pageCount!!) {
                    page++
                    viewModel.getTopFilms(page)
                }
            }
        })
    }

    private fun viewModelSubscribe() {
        viewModel.getTopFilms()

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                updateWrapper.visibility = View.GONE
                aLoadingDialog.show()
            } else {
                aLoadingDialog.cancel()
            }
        }

        viewModel.isError.observe(viewLifecycleOwner) { isError ->
            if (isError) {
                if (filmList.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    updateWrapper.visibility = View.VISIBLE
                }
                Toast.makeText(
                    context,
                    "Ошибка получения данных\n" + viewModel.errorMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        viewModel.filmData.observe(viewLifecycleOwner) { response ->
            pageCount = response.pagesCount
            response.films?.let {
                recyclerView.visibility = View.VISIBLE
                updateWrapper.visibility = View.GONE
                val beforeSize = filmList.size
                filmList.addAll(it)
                adapter.notifyItemRangeInserted(beforeSize, filmList.size - beforeSize)
            }
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
            val toast = Toast.makeText(context, null, Toast.LENGTH_SHORT)
            if (!currentList.contains(film)) {
                currentList.add(film)
                favList.postValue(currentList)
                toast.setText("Фильм ${film.nameRu} был добавлен в избранное")
                toast.show()
                adapter.notifyItemChanged(position)
            } else {
                toast.setText("Фильм ${film.nameRu} уже в избранном")
                toast.show()
            }
            println("HOLD on film ${film.nameRu}")
        }
    }
}
