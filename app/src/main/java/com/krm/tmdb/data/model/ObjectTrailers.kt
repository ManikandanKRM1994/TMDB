package com.krm.tmdb.data.model

import java.io.Serializable

class ObjectTrailers(
        var id: Int,
        var results: MutableList<ObjectDetailsTrailers>
) : Serializable

class ObjectDetailsTrailers(
        var key: String,
        var name: String,
        var size: String
) : Serializable
