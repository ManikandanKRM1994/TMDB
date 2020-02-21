package com.krm.tmdb.ui.adapters.adaptermovie

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.krm.tmdb.data.model.PojoResultsMovie
import com.krm.tmdb.utils.ConstStrings
import com.krm.tmdb.utils.ConstFun.setImageUrl
import kotlinx.android.synthetic.main.item_adapter_multi.view.*

class ViewHolderMovieCount3(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: PojoResultsMovie) = with(itemView) {
        img_multi.setImageUrl("${ConstStrings.BASE_URL_IMAGE}${ConstStrings.SIZE_IMAGE_POSTER}${item.poster_path}")
    }
}