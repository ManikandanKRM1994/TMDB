package com.krm.tmdb.data.model

import com.google.gson.JsonArray
import java.io.Serializable

data class ObjectsSearch(
        var page: Int,
        var total_pages: Int,
        var results: JsonArray
) : Serializable