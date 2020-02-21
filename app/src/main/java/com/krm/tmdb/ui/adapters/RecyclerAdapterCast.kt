package com.krm.tmdb.ui.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.krm.tmdb.R
import com.krm.tmdb.data.model.ObjectsDetailsCast
import com.krm.tmdb.utils.ConstStrings
import com.krm.tmdb.utils.ConstFun.setImagePeopleUrl
import com.pawegio.kandroid.inflateLayout
import kotlinx.android.synthetic.main.item_cast.view.*
import javax.inject.Inject

class RecyclerAdapterCast @Inject constructor(private var context: Context) : RecyclerView.Adapter<RecyclerAdapterCast.ViewHolder>() {

    private var list: MutableList<ObjectsDetailsCast> = mutableListOf()
    private var onItemCastClickListener: OnItemCastClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(context.inflateLayout(R.layout.item_cast, parent, false))

    override fun getItemCount(): Int = list.size

    fun addItem(item: ObjectsDetailsCast) {
        list.add(item)
        notifyItemInserted(itemCount)
    }

    fun clearList() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
        val data = list[position]
        holder.itemView.linear_click.setOnClickListener {
            val profile = if (!data.profile_path.isNullOrEmpty()) data.profile_path else ""
            onItemCastClickListener!!.onItemCastClicked(data.id, data.name, profile,holder.itemView.img_cast)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ObjectsDetailsCast) = with(itemView) {
            name_cast.text = item.name
            character_cast.text = item.character
            img_cast.setImagePeopleUrl("${ConstStrings.BASE_URL_IMAGE}${ConstStrings.SIZE_IMAGE_POSTER}${item.profile_path}"){}
        }
    }

    interface OnItemCastClickListener {
        fun onItemCastClicked(id: Int, name: String,image: String,v:View)
    }

    fun setOnItemCastClickListener(onItemCastClickListener: OnItemCastClickListener) {
        this.onItemCastClickListener = onItemCastClickListener
    }

}