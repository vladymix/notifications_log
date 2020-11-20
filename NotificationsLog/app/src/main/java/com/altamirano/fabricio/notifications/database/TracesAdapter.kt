package com.altamirano.fabricio.notifications.database


import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.altamirano.fabricio.libraryast.tools.inflate
import com.altamirano.fabricio.notifications.AppLogic.getAsDate
import com.altamirano.fabricio.notifications.AppLogic.getFormatted
import com.altamirano.fabricio.notifications.NotificationsLogActivity
import com.altamirano.fabricio.notifications.R
import com.altamirano.fabricio.notifications.models.TrackNotification

class TracesAdapter(val list: List<TrackNotification>, val origin: String?) :
    RecyclerView.Adapter<TracesAdapter.TracesHolder>() {

    inner class TracesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPackage: ImageView = itemView.findViewById(R.id.imgPackage)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvSubtitle: TextView = itemView.findViewById(R.id.tvSubtitle)
        val tvBundle: TextView = itemView.findViewById(R.id.tvBundle)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val btnExpended: View = itemView.findViewById(R.id.relativeContent)

        fun bindValue(trackRequest: TrackNotification) {

            if (!origin.isNullOrEmpty()) {
                this.tvTitle.text = trackRequest.title
                this.tvSubtitle.text = trackRequest.text
                this.tvBundle.text = trackRequest.raw.getFormatted()
                this.tvDate.text = trackRequest.time.getAsDate()

                if (trackRequest.expended) {
                    tvBundle.visibility = View.VISIBLE
                } else {
                    tvBundle.visibility = View.GONE
                }

                btnExpended.setOnClickListener {
                    list[adapterPosition].let {
                        it.expended = !it.expended
                    }
                    notifyItemChanged(adapterPosition)
                }

            } else {
                this.tvTitle.text = trackRequest.origin
                this.tvSubtitle.text = trackRequest.text
                this.tvBundle.text = trackRequest.raw.getFormatted()
                this.tvDate.text = trackRequest.time.getAsDate()
                tvBundle.visibility = View.GONE

                btnExpended.setOnClickListener {
                    list[adapterPosition].let {
                        btnExpended.context.startActivity(
                            Intent(
                                btnExpended.context,
                                NotificationsLogActivity::class.java
                            ).apply {
                                putExtra("key_origin", it.origin)
                            })
                    }
                    notifyItemChanged(adapterPosition)
                }
            }

            try {
                imgPackage.setImageDrawable(
                    imgPackage.context.packageManager.getApplicationIcon(
                        trackRequest.origin
                    )
                )
            } catch (ex: Exception) {
                imgPackage.setImageResource(R.drawable.ic_icon_notification)
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracesHolder = TracesHolder(
        parent.inflate(
            R.layout.item_notification
        )
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: TracesHolder, position: Int) {

        holder.bindValue(list[position])
    }
}