package com.krm.tmdb.data.model

import java.io.Serializable

data class ObjectsCast(var cast: MutableList<ObjectsDetailsCast>) : Serializable

data class ObjectsDetailsCast(
        var character: String,
        var id: Int,
        var name: String,
        var profile_path: String
) : Serializable