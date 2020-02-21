package com.krm.tmdb.ui.fragments.movies

import androidx.fragment.app.FragmentManager
import com.krm.tmdb.R
import com.krm.tmdb.ui.fragments.movies.category.FragmentCategoryMovies
import com.krm.tmdb.utils.ConstStrings

class InteractorFragmentMovies (private var childManager: FragmentManager) :
    InterfaceFragmentMovies {

    override fun fragmentCategoryPopular() {
        setFragmentCategory(ConstStrings.POPULAR_MOVIE)
    }

    override fun fragmentCategoryRating() {
        setFragmentCategory(ConstStrings.TOP_RATED_MOVIE)
    }

    override fun fragmentCategoryUpcoming() {
        setFragmentCategory(ConstStrings.UPCOMING_MOVIE)
    }

    override fun fragmentCategoryNowPlaying() {
        setFragmentCategory(ConstStrings.NOW_PLAYING)
    }

    private fun setFragmentCategory(category: String) {
        val fragmentNow = childManager.findFragmentByTag("movies")
        val trans = childManager.beginTransaction()
        if (fragmentNow != null) trans.remove(fragmentNow)
        trans.disallowAddToBackStack()
        trans.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        trans.replace(R.id.frame_principal_movies, FragmentCategoryMovies().newInstance(category), "movies")
        trans.commit()
    }

}