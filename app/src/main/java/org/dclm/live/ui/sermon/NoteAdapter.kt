package org.dclm.live.ui.sermon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.dclm.live.R
import org.dclm.live.databinding.NoteItemBinding
import java.util.*
import kotlin.collections.ArrayList

class NoteAdapter(val click: OnClicked, val deleteClicked: OnDeleteClicked) : RecyclerView.Adapter<NoteAdapter.NoteHolder>(), Filterable {
  private var note:MutableList<Note> = ArrayList()
    private var filteredNote:MutableList<Note> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
       val binding = DataBindingUtil.inflate<NoteItemBinding>(LayoutInflater.from(parent.context), R.layout.note_item, parent, false)
        return NoteHolder(binding)
    }

    override fun getItemCount(): Int {
        return note.size
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
       val notes = note[position]
        holder.binding.note = notes
        holder.binding.root.setOnClickListener {
            click.clicked(it, notes)
        }
        holder.binding.deleteOne.setOnClickListener {
            deleteClicked.clicked(note[position])
        }
    }
    fun setNote( note: MutableList<Note>){
        this.note = note
        filteredNote.addAll(note)
        notifyDataSetChanged()
    }

    fun getNoteAt(position: Int): Note {
        return note[position]
    }

    inner class NoteHolder(var binding: NoteItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    interface OnClicked {
        fun clicked(view: View, note: Note)
    }

    interface OnDeleteClicked {
        fun clicked(note: Note)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charString = constraint.toString().trim {
                    it <= ' ' }.toLowerCase()
                note.clear()
                if (charString.isEmpty()) {
                    note.addAll(filteredNote)
                } else {
                    for (row in filteredNote) {
                        if (row.topic?.toLowerCase(Locale.ROOT)?.trim()
                                ?.contains(charString.toLowerCase())!!
                        ) {
                            note.add(row)
                        }
                    }
                }
                val hasilFilter = FilterResults()
                hasilFilter.values = note
                return hasilFilter
            }

            override fun publishResults(
                constraint: CharSequence,
                results: FilterResults
            ) {
                notifyDataSetChanged()
            }
        }
    }
}