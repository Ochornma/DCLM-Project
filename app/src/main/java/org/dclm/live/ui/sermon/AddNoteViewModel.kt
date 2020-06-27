package org.dclm.live.ui.sermon

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.dclm.live.ui.util.DCLMRepository

class AddNoteViewModel(val context: Context) : ViewModel() {
    private var repository: DCLMRepository? = null

    init {
        repository = DCLMRepository(context)
    }

    fun update(note: Note?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository!!.update(note!!)
            }
        }
    }

    fun insert(note: Note?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository!!.insert(note!!)
            }
        }
    }

    fun delete(note: Note?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository!!.delete(note!!)
            }
        }

    }
}