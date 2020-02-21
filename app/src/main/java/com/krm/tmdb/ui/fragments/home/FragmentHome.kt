package com.krm.tmdb.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.daimajia.androidanimations.library.Techniques
import com.krm.tmdb.data.model.PojoResultsMovie
import com.krm.tmdb.data.model.PojoResultsTv
import com.krm.tmdb.ui.activities.base.BaseFragment
import com.krm.tmdb.ui.adapters.adaptermovie.ViewPagerPopularMovie
import com.krm.tmdb.ui.adapters.adaptermovie.RecyclerAdapterMovies
import com.krm.tmdb.ui.adapters.adaptertv.RecyclerAdapterTvShows
import com.krm.tmdb.R
import com.krm.tmdb.utils.ConstStrings
import com.krm.tmdb.utils.MyCountDownTimer
import com.krm.tmdb.utils.ConstFun.animatedView
import com.krm.tmdb.utils.ConstFun.parallaxPageTransformer
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject
import javax.inject.Named

class FragmentHome : BaseFragment() , InterfaceFragmentHome.View, RecyclerAdapterMovies.OnItemMovieClickListener, RecyclerAdapterTvShows.OnItemTvClickListener, ViewPagerPopularMovie.OnItemClickListener {

    @Inject lateinit var interactor: InteractorFragmentHome<InterfaceFragmentHome.View>

    @Inject lateinit var recyclerAdapterMovieNowPlaying: RecyclerAdapterMovies
    @Inject lateinit var recyclerAdapterOnTv: RecyclerAdapterTvShows

    @field:Named(ConstStrings.HORIZONTAL)
    @Inject lateinit var lManagerMovieNowPlaying: GridLayoutManager
    @field:Named(ConstStrings.HORIZONTAL)
    @Inject lateinit var lManagerOnTv : GridLayoutManager

    private var timer: MyCountDownTimer?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent()!!.inject(this)
        interactor.onAttach(this)
    }

    override fun onDetach() {
        interactor.onDetach()
        super.onDetach()
    }

    override fun onPause() {
        super.onPause()
        onStopTimer()
    }

    override fun onStart() {
        super.onStart()
        if (isAdded) if (timer!=null) timer!!.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_home, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onClickView()
        setAdapterRecycler()
        interactor.getMoviesPopular()
        interactor.getMoviesNowPlaying()
        interactor.getOnTv()
    }

    private fun onClickView(){
        btn_see_all_now_playing.setOnClickListener {
            getBaseActivity().setSelectedNavigationMovie(R.id.nav_now_playing)
        }
        btn_see_all_on_tv.setOnClickListener {
            getBaseActivity().setSelectedNavigationTv(R.id.nav_air_tv)
        }
    }

    private fun setAdapterRecycler(){
        list_now_playing.apply {
            setHasFixedSize(true)
            layoutManager = lManagerMovieNowPlaying
            recycledViewPool.clear()
            adapter = recyclerAdapterMovieNowPlaying
            isNestedScrollingEnabled = false
        }
        recyclerAdapterMovieNowPlaying.setType(2)
        recyclerAdapterMovieNowPlaying.notifyDataSetChanged()
        recyclerAdapterMovieNowPlaying.setOnItemMovieClickListener(this)

        list_on_tv.apply {
            setHasFixedSize(true)
            layoutManager = lManagerOnTv
            recycledViewPool.clear()
            adapter = recyclerAdapterOnTv
            isNestedScrollingEnabled = false
        }
        recyclerAdapterOnTv.setType(2)
        recyclerAdapterOnTv.notifyDataSetChanged()
        recyclerAdapterOnTv.setOnItemTvClickListener(this)
    }

    override fun setDataMoviesPopular(list: MutableList<PojoResultsMovie>) {
        val pagerAdapter = ViewPagerPopularMovie(context!!, list)
        viewpager_popular_movie.apply {
            setScrollDurationFactor(4)
            setPageTransformer(true,parallaxPageTransformer(R.id.cardview_pager,R.id.id_text))
            adapter = pagerAdapter
        }
        pagerAdapter.notifyDataSetChanged()
        pagerAdapter.setOnItemClickListener(this)
        pageSwitcher(list)
    }

    override fun onItemCastClicked(id: Int, title: String, type: String, image: String) {
        getBaseActivity().setMaximizeDraggable(id,title,type,image)
    }

    private fun pageSwitcher(list: MutableList<PojoResultsMovie>){
        timer = MyCountDownTimer(5000,5000){
            try {
                if (list.size-1 == viewpager_popular_movie.currentItem) viewpager_popular_movie.currentItem = 0
                else viewpager_popular_movie.currentItem = viewpager_popular_movie.currentItem+1
                timer!!.start()
            }catch (t:Throwable){
                timer!!.cancel()
            }
        }
        timer!!.start()
    }

    fun onStopTimer(){
        if (timer!=null) timer!!.cancel()
    }

    fun onUserInteraction(){
        if (timer!=null){
            timer!!.cancel()
            timer!!.start()
        }
    }

    override fun setDataMoviesNowPlaying(list: MutableList<PojoResultsMovie>) {
        for (movie in list) recyclerAdapterMovieNowPlaying.addItem(movie)
        animatedView(Techniques.SlideInRight,list_now_playing,1000)
    }

    override fun onItemMovieClicked(id: Int, title: String, type: String, image: String) {
        getBaseActivity().setMaximizeDraggable(id, title, type, image)
    }

    override fun setDataOnTv(list: MutableList<PojoResultsTv>) {
        for (tv in list) recyclerAdapterOnTv.addItem(tv)
        animatedView(Techniques.SlideInRight, list_on_tv, 1000)
    }

    override fun onItemTvClicked(id: Int, title: String, type: String, image: String) {
        getBaseActivity().setMaximizeDraggable(id, title, type, image)
    }

    override fun showLoadingMoviesPopular() {
        progress_popular_movie.show()
        viewpager_popular_movie.hide()
    }

    override fun hideLoadingMoviesPopular() {
        viewpager_popular_movie.show()
        progress_popular_movie.hide()
    }

    override fun showLoadingMoviesNowPlaying() {
        progress_now_playing_movie.show()
        list_now_playing.hide()
    }

    override fun showLoadingOnTv() {
        progress_on_tv.show()
        list_on_tv.hide()
    }

    override fun hideLoadingMoviesNowPlaying() {
        progress_now_playing_movie.hide()
        list_now_playing.show()
    }

    override fun hideLoadingOntTv() {
        progress_on_tv.hide()
        list_on_tv.show()
    }

}