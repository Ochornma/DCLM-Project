package org.dclm.live.ui.sermon

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.dclm.live.ui.util.DCLMRepository

class SermonViewModel(val context: Context) : ViewModel() {
    private var repository: DCLMRepository? = null
    private var allNotes: LiveData<MutableList<Note>>? = null

    init {
        repository = DCLMRepository(context)
        allNotes = repository?.getAllNotes()

    }

    fun delete(note: Note) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository?.delete(note)
            }
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository?.deleteAll()
            }
        }
    }

    fun getAllNotes(): LiveData<MutableList<Note>>? {
        return allNotes
    }
}