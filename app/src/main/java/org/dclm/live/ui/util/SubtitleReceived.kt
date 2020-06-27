package org.dclm.live.ui.util

import org.dclm.live.ui.radio.SubTitle

interface SubtitleReceived {
    fun subtitle(subTitles: SubTitle?)
    fun error(subTitles: SubTitle?)
}