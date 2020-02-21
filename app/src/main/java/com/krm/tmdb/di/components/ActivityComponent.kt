package com.krm.tmdb.di.components

import com.krm.tmdb.ui.activities.main.MainActivity
import com.krm.tmdb.ui.activities.splash.SplashActivity
import com.krm.tmdb.di.PerActivity
import com.krm.tmdb.di.modules.ActivityModule
import com.krm.tmdb.ui.activities.image.ViewPagerImageActivity
import com.krm.tmdb.ui.activities.detailspeople.DetailsPeopleActivity
import com.krm.tmdb.ui.fragments.details.FragmentDetailsBottom
import com.krm.tmdb.ui.fragments.details.FragmentDetailsTop
import com.krm.tmdb.ui.fragments.home.FragmentHome
import com.krm.tmdb.ui.fragments.movies.category.FragmentCategoryMovies
import com.krm.tmdb.ui.fragments.people.FragmentPeople
import com.krm.tmdb.ui.fragments.search.FragmentSearch
import com.krm.tmdb.ui.fragments.tvshows.category.FragmentCategoryTv
import dagger.Component

@PerActivity
@Component(dependencies = [AppsComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(splashActivity: SplashActivity)

    fun inject(mainActivity: MainActivity)

    fun inject(detailsPeopleActivity: DetailsPeopleActivity)

    fun inject(viewPagerImageActivity: ViewPagerImageActivity)

    fun inject(fragmentDetailsTop: FragmentDetailsTop)

    fun inject(fragmentDetailsBottom: FragmentDetailsBottom)

    fun inject(fragmentCategoryMovies: FragmentCategoryMovies)

    fun inject(fragmentCategoryTv: FragmentCategoryTv)

    fun inject(fragmentSearch: FragmentSearch)

    fun inject(fragmentPeople: FragmentPeople)

    fun inject(fragmentHome: FragmentHome)

}