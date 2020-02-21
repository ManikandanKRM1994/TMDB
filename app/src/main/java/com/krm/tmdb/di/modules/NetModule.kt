package com.krm.tmdb.di.modules

import com.krm.tmdb.app.TmdbApp
import com.krm.tmdb.data.api.NewsApi
import com.krm.tmdb.data.api.RestApi
import com.krm.tmdb.data.api.RestFull
import com.krm.tmdb.utils.ConstStrings.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetModule(val app: TmdbApp) {

    @Provides
    fun provideNewsApi(api: RestApi): NewsApi = RestFull(api)

    @Provides
    fun provideRestApi(retrofit: Retrofit): RestApi = retrofit.create(RestApi::class.java)

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun provideRxJavaCallAdapterFactory(): CallAdapter.Factory
            = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

    @Provides
    fun provideOkHttpClient() : OkHttpClient = OkHttpClient.Builder().cache(Cache(app.cacheDir,(5 * 1024 * 1024))).build()

    @Provides
    fun provideRetrofit(client:OkHttpClient,gsonConverterFactory: GsonConverterFactory,callAdapterFactory: CallAdapter.Factory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(callAdapterFactory)
                .build()
    }

}