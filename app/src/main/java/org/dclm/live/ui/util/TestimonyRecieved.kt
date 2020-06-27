package org.dclm.live.ui.util

import org.dclm.live.ui.experience.testimony.Testimony

interface TestimonyRecieved {
    fun testimontRecieved(testimony: MutableList<Testimony>)
    fun error()
}