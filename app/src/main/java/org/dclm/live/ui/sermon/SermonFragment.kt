package org.dclm.live.ui.sermon

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.dclm.live.R
import org.dclm.live.databinding.SermonFragmentBinding

class SermonFragment : Fragment(), NoteAdapter.OnClicked, NoteAdapter.OnDeleteClicked,
    SearchView.OnQueryTextListener {

    private lateinit var viewModel: SermonViewModel
    private lateinit var binding: SermonFragmentBinding
    private lateinit var noteAdapter: NoteAdapter
    private var mnotes: MutableList<Note>? = null

    companion object {
        fun newInstance() = SermonFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.sermon_fragment, container, false)
        noteAdapter = NoteAdapter(this, this)
        binding.recycler.layoutManager = LinearLayoutManager(activity)
        binding.recycler.adapter = noteAdapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
              // viewModel.delete(noteAdapter.getNoteAt(viewHolder.adapterPosition))
                deleteAlert(noteAdapter.getNoteAt(viewHolder.adapterPosition))

            }
        }).attachToRecyclerView(binding.recycler)

        binding.newNote.setOnClickListener { v ->
            val action = SermonFragmentDirections.actionSermonFragmentToAddNoteFragment(null)
            Navigation.findNavController(v).navigate(action)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = context?.let { SermonFactory(it) }
        viewModel = factory?.let { ViewModelProvider(this, it).get(SermonViewModel::class.java) }!!
        viewModel.getAllNotes()?.observe(viewLifecycleOwner, Observer {
            noteAdapter.setNote(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search, menu)
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun deleteAlert(note: Note) {
        val builder = context?.let {
            AlertDialog.Builder(it, R.style.TextAppearance_DCLM_ALERT)
        }
        builder?.setTitle(getString(R.string.delete_one))
        builder?.setMessage(getString(R.string.do_delete))
        builder?.setNegativeButton(
            getString(R.string.cancel)) { dialogInterface, i ->
            dialogInterface.dismiss()

        }
        builder?.setPositiveButton(getString(R.string.ok)) { dialogInterface, i ->
            viewModel.delete(note)
            Toast.makeText(context, getString(R.string.deleted), Toast.LENGTH_SHORT)
                .show()
        }
        val alertDialog = builder?.create()
        alertDialog?.show()
    }

    override fun clicked(view: View, note: Note) {
       val action = SermonFragmentDirections.actionSermonFragmentToViewNoteFragment(note)
        Navigation.findNavController(view).navigate(action)
    }

    override fun clicked(note: Note) {
        deleteAlert(note)
       // viewModel.delete(note)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        noteAdapter.filter.filter(newText)
        return false
    }

}