package exe.weazy.cvchecker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import exe.weazy.cvchecker.R
import exe.weazy.cvchecker.entity.Viewer
import exe.weazy.cvchecker.util.getDateByUid
import exe.weazy.cvchecker.util.getViewerRankTitle
import java.text.SimpleDateFormat
import java.util.*

class ViewersAdapter(val viewers: MutableList<Viewer>, private val onItemClickListener: View.OnClickListener) : RecyclerView.Adapter<ViewersAdapter.Holder>() {


    fun setViewers(viewers: List<Viewer>) {
        this.viewers.clear()
        this.viewers.addAll(viewers)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.element_viewer, parent, false)
        view.setOnClickListener(onItemClickListener)

        return Holder(view)
    }

    override fun getItemCount() = viewers.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(viewers[position])
    }


    inner class Holder(view : View) : RecyclerView.ViewHolder(view) {

        private var nameTextView : TextView
        private var surnameTextView : TextView
        private var patronymicTextView : TextView
        private var rankTextView : TextView
        private var dateTextView : TextView


        init {
            super.itemView

            nameTextView = view.findViewById(R.id.text_name)
            surnameTextView = view.findViewById(R.id.text_surname)
            patronymicTextView = view.findViewById(R.id.text_patronymic)
            rankTextView = view.findViewById(R.id.text_rank)
            dateTextView = view.findViewById(R.id.text_date)
        }

        fun bind(viewer : Viewer) {
            nameTextView.text = viewer.name
            surnameTextView.text = viewer.surname
            patronymicTextView.text = viewer.patronymic

            val rank = StringBuilder("Статус: ")
            rank.append(getViewerRankTitle(viewer))

            rankTextView.text = rank.toString()

            val date = getDateByUid(viewer)
            val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

            dateTextView.text = formatter.format(date)
        }
    }
}