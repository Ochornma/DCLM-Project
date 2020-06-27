package org.dclm.live.ui.radio

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.dclm.live.ui.util.DCLMRepository
import org.dclm.live.ui.util.SubtitleReceived

class RadioViewModel(val context: Context, subtitleReceived: SubtitleReceived) : ViewModel() {
    var checkButton: MutableLiveData<Boolean>? = null
    var link: MutableLiveData<String>? = null
    private var repository: DCLMRepository? = null


    init {
        checkButton = MutableLiveData()
        link = MutableLiveData()
        repository = DCLMRepository(context, subtitleReceived)
    }

    fun playPause(state: Boolean) {
      /*  var state = state
        state = RadioFragment.state
        checkButton!!.postValue(state)*/
    }

    fun jsonParse(url: String) {
        repository?.jsonParse(url)
    }

    fun english(){
        link?.postValue("ENGLISH")
    }

    fun french(){
        link?.postValue("FRENCH")
    }

    fun portugal(){
        link?.postValue("PORTUGUESE/EGUN")
    }

    fun yoruba(){
        link?.postValue("YORUBA")
    }


    fun igbo(){
        link?.postValue("IGBO")
    }

    fun hausa(){
        link?.postValue("HAUSA")
    }
}
