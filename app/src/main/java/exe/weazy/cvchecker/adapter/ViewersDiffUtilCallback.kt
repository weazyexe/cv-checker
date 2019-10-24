package exe.weazy.cvchecker.adapter

import androidx.recyclerview.widget.DiffUtil
import exe.weazy.cvchecker.entity.Viewer

class ViewersDiffUtilCallback(private val old : List<Viewer>, private val new : List<Viewer>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = old[oldItemPosition]
        val newItem = new[newItemPosition]

        return oldItem.uid == newItem.uid
    }

    override fun getOldListSize() = old.size

    override fun getNewListSize() = new.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = old[oldItemPosition]
        val newItem = new[newItemPosition]

        return oldItem == newItem
    }
}