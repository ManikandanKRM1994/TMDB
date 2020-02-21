package com.krm.tmdb.ui.adapters.adaptermovie

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.krm.tmdb.data.model.PojoResultsMovie
import com.krm.tmdb.utils.ConstStrings
import com.krm.tmdb.utils.ConstFun.setImageUrl
import kotlinx.android.synthetic.main.item_adapter_poster.view.*

class ViewHolderMovieHorizontal(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: PojoResultsMovie) = with(itemView) {
        txt_title.text = item.title
        img_poster.setImageUrl("${ConstStrings.BASE_URL_IMAGE}${ConstStrings.SIZE_IMAGE_POSTER}${item.poster_path}")
    }

}