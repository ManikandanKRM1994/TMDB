package com.krm.tmdb.ui.adapters.adapterimages

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.krm.tmdb.data.model.ObjectsDetailsImages
import com.krm.tmdb.utils.ConstStrings
import com.krm.tmdb.utils.ConstFun.setImageUrl
import kotlinx.android.synthetic.main.item_images.view.*

class ViewHolderImages(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: ObjectsDetailsImages) = with(itemView) {
        img_images.setImageUrl("${ConstStrings.BASE_URL_IMAGE}${ConstStrings.SIZE_IMAGE_BACKDROP}" + item.file_path)
        voto_images.text = item.vote_count.toString()
    }
}