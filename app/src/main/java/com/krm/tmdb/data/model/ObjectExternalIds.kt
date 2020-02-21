package com.krm.tmdb.data.model

import java.io.Serializable

data class ObjectExternalIds(
        var imdb_id:String,
        var facebook_id:String,
        var instagram_id:String,
        var twitter_id:String
) : Serializable