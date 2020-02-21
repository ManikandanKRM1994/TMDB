package com.krm.tmdb.data.model

import java.io.Serializable

data class ObjectImages(var id: Int, var backdrops: MutableList<ObjectsDetailsImages>) : Serializable

data class ObjectsDetailsImages(var file_path: String, var vote_count: Int) : Serializable

data class ObjectImagesPerson(var id:Int, var profiles:MutableList<ObjectsDetailsImages>) : Serializable

data class ObjectImagesPersonTagged(
        var page: Int,
        var total_pages: Int,
        var results: MutableList<ObjectImagesDetailsPersonTagged>
):Serializable

data class ObjectImagesDetailsPersonTagged(var aspect_ratio:Float,var file_path: String, var vote_count: Int):Serializable