package com.krm.tmdb.ui.adapters.adaptertv

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.krm.tmdb.data.model.PojoResultsTv
import com.krm.tmdb.utils.ConstStrings
import com.krm.tmdb.utils.ConstFun.setImageUrl
import kotlinx.android.synthetic.main.item_adapter_poster.view.*

class ViewHolderTvHorizontal(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: PojoResultsTv) = with(itemView) {
        txt_title.text = item.name
        img_poster.setImageUrl("${ConstStrings.BASE_URL_IMAGE}${ConstStrings.SIZE_IMAGE_POSTER}${item.poster_path}")
    }
}