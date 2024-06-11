package ru.vsu.cs.simplecinemaapp.view

import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.orhanobut.hawk.Hawk
import ru.vsu.cs.simplecinemaapp.R
import ru.vsu.cs.simplecinemaapp.adapter.AdapterViewPager
import ru.vsu.cs.simplecinemaapp.holders.FilmDataHolder

class MainActivity : AppCompatActivity() {
    private lateinit var pagerMain: ViewPager2
    private lateinit var backArrow: ImageView
    private val fragments = ArrayList<Fragment>()
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var aLoadingDialog: ALoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        aLoadingDialog = ALoadingDialog(this)
        pagerMain = findViewById(R.id.pagerMain)
        bottomNav = findViewById(R.id.bottomNav)
        backArrow = findViewById(R.id.arrowBack)

        // инициализируем бд Hawk
        Hawk.init(this).build()

        // проверяем содержится ли в бд данные о favouriteList
        if (Hawk.contains("films")) {
            FilmDataHolder.favouriteFilmList.postValue(Hawk.get("films"))
        }

        // подписываемся на изменения в favList и обновляем локальную память
        FilmDataHolder.favouriteFilmList.observe(this) {
            Hawk.put("films", it)
        }

        backArrow.setOnClickListener {
            pagerMain.currentItem = 0
        }
        createSetupFragments()
        setupPagerMain()
    }

    private fun createSetupFragments() {
        fragments.add(PopularFragment(aLoadingDialog))
        fragments.add(FavouriteFragment())
        fragments.add(SearchFragment(aLoadingDialog))
        fragments.add(FilmOverviewFragment(aLoadingDialog))
    }

    private fun setupPagerMain() {
        val adapterViewPager = AdapterViewPager(this, fragments)
        pagerMain.adapter = adapterViewPager

        pagerMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        backArrow.visibility = View.GONE
                        bottomNav.visibility = View.VISIBLE
                        bottomNav.selectedItemId = R.id.itPopular
                    }

                    1 -> {
                        backArrow.visibility = View.GONE
                        bottomNav.visibility = View.VISIBLE
                        bottomNav.selectedItemId = R.id.itFavourite
                    }

                    2 -> {
                        backArrow.visibility = View.GONE
                        bottomNav.visibility = View.VISIBLE
                        bottomNav.selectedItemId = R.id.itSearch
                    }

                    3 -> {
                        backArrow.visibility = View.VISIBLE
                        bottomNav.visibility = View.GONE
                    }
                }
                super.onPageSelected(position)
            }
        })

        bottomNav.setOnItemSelectedListener { item ->
            val itemId = item.itemId
            when (itemId) {
                R.id.itPopular -> pagerMain.currentItem = 0
                R.id.itFavourite -> pagerMain.currentItem = 1
                R.id.itSearch -> pagerMain.currentItem = 2
            }
            true
        }
    }

    fun changePageToFilmOverview() {
        pagerMain.currentItem = 3
    }
}