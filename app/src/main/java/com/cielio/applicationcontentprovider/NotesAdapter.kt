package com.cielio.applicationcontentprovider
import android.database.Cursor
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cielio.applicationcontentprovider.database.NotesDatabaseHelper.Companion.DESCRIPTION_NOTES
import com.cielio.applicationcontentprovider.database.NotesDatabaseHelper.Companion.TITLE_NOTES

class NotesAdapter(
    private val listener: NoteClickedListener

): RecyclerView.Adapter<NotesViewHolder>() {
    private var mCursor: Cursor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
        NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_item,parent,false))

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        mCursor?.moveToPosition(position)

        holder.noteTile.text = mCursor?.getString(mCursor?.getColumnIndex(TITLE_NOTES) as Int)
        holder.noteDescription.text = mCursor?.getString(mCursor?.getColumnIndex(DESCRIPTION_NOTES) as Int)
        holder.noteButton.setOnClickListener {
            mCursor?.moveToPosition(position)
            listener.noteRemoveItem(mCursor)
            notifyDataSetChanged()
        }
        holder.itemView.setOnClickListener {
            listener.noteClickedItem(mCursor as Cursor)
        }
    }

    override fun getItemCount(): Int {
        return if (mCursor != null) mCursor?.count as Int else 0
    }

    fun setCursor(newCursor: Cursor?){
        mCursor = newCursor
        notifyDataSetChanged()
    }
}

class NotesViewHolder(
        itemView: View
):RecyclerView.ViewHolder(itemView){
    val noteTile = itemView.findViewById<TextView>(R.id.note_title)
    val noteDescription = itemView.findViewById<TextView>(R.id.note_description)
    val noteButton = itemView.findViewById<Button>(R.id.note_button_remove)
}