package com.cielio.applicationcontentprovider

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.LocusId
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.cielio.applicationcontentprovider.database.NotesDatabaseHelper.Companion.DESCRIPTION_NOTES
import com.cielio.applicationcontentprovider.database.NotesDatabaseHelper.Companion.TITLE_NOTES
import com.cielio.applicationcontentprovider.database.NotesProvider.Companion.URI_NOTES
import kotlin.math.E

class NotesDetailFragment: DialogFragment(), DialogInterface.OnClickListener{

    private lateinit var noteEditTitle: EditText
    private lateinit var noteDescription: EditText
    private var id: Long = 0

    companion object{
        private const val EXTRA_ID = "id"
        fun newInstance(id: Long):NotesDetailFragment{
            val bundle = Bundle()
            bundle.putLong(EXTRA_ID, id)
            val notesFragment = NotesDetailFragment()
            notesFragment.arguments = bundle
            return notesFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity?.layoutInflater?.inflate(R.layout.note_detail,null)

        noteEditTitle = view?.findViewById(R.id.note_edit_title) as EditText
        noteDescription = view.findViewById(R.id.note_edt_description) as EditText

        var newNote = true
        if (arguments != null && arguments?.getLong(EXTRA_ID) != 0L){
            id = arguments?.getLong(EXTRA_ID) as Long
            var uri = Uri.withAppendedPath(URI_NOTES, id.toString())
            var cursor =
                    activity?.contentResolver?.query(uri, null,null,null,null)
            if (cursor?.moveToNext() as Boolean){
                newNote = false
                noteEditTitle.setText(cursor.getString(cursor.getColumnIndex(TITLE_NOTES)))
                noteDescription.setText(cursor.getString(cursor.getColumnIndex(DESCRIPTION_NOTES)))
            }
            cursor.close()
        }
        return AlertDialog.Builder(activity as Activity)
                .setTitle(if (newNote) "Nova mensagem" else "Editar mensagem")
                .setView(view)
                .setPositiveButton("Salvar", this)
                .setNegativeButton("Cancelar", this)
                .create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val values = ContentValues()
        values.put(TITLE_NOTES, noteEditTitle.text.toString())
        values.put(DESCRIPTION_NOTES, noteDescription.text.toString())

        if(id != 0L){
            val uri = Uri.withAppendedPath(URI_NOTES, id.toString())
            context?.contentResolver?.update(uri,values, null,null)
        } else{
            context?.contentResolver?.insert(URI_NOTES, values)
        }
    }
}