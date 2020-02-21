package com.krm.tmdb.ui.adapters.adaptertv

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.krm.tmdb.data.model.PojoResultsTv
import com.krm.tmdb.data.preference.DataSharePreference.getGenderTvShow
import com.krm.tmdb.utils.ConstStrings
import com.krm.tmdb.utils.ConstFun.getReformatDate
import com.krm.tmdb.utils.ConstFun.setImageUrl
import kotlinx.android.synthetic.main.item_adapter.view.*

class ViewHolderTvDefault (itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(item: PojoResultsTv) = with(itemView) {
        title_adapter.text = item.name
        date_adapter.text = getReformatDate(item.first_air_date)
        rating_adapter.text = item.vote_average.toString()

        val genders = StringBuilder()
        (0 until item.genre_ids.size).asSequence()
                .forEach { if (it == item.genre_ids.size-1) genders.append(context.getGenderTvShow(item.genre_ids[it].toString()))
                else genders.append(context.getGenderTvShow(item.genre_ids[it].toString())).append(", ") }
        genero_adapter.text = genders.toString()

        img_poster.setImageUrl("${ConstStrings.BASE_URL_IMAGE}${ConstStrings.SIZE_IMAGE_POSTER}${item.poster_path}")
    }
}