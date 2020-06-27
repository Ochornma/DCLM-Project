package org.dclm.live.ui.experience.prayer

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.prayer_fragment.*
import kotlinx.android.synthetic.main.testimony_fragment.*
import kotlinx.android.synthetic.main.testimony_fragment.email
import kotlinx.android.synthetic.main.testimony_fragment.name
import kotlinx.android.synthetic.main.testimony_fragment.subject
import kotlinx.android.synthetic.main.testimony_fragment.submit_testimony
import kotlinx.android.synthetic.main.testimony_fragment.your_testimony
import org.dclm.live.R
import org.dclm.live.ui.util.PrayerSubmitted

class PrayerFragment : Fragment(), PrayerSubmitted {

    companion object {
        fun newInstance() = PrayerFragment()
    }

    private lateinit var viewModel: PrayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.prayer_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = context?.let { PrayerFactory(this, it) }
        viewModel = factory?.let { ViewModelProvider(this, it).get(PrayerViewModel::class.java) }!!
        submit_testimony.setOnClickListener {
            viewModel.postPrayer(name.text.toString(), email.text.toString(), subject.text.toString(), your_testimony.text.toString())
            name.setText("")
            email.setText("")
            subject.setText("")
            your_testimony.setText("")
        }

    }

    override fun submitted() {
        Toast.makeText(context, activity?.resources?.getString(R.string.prayer_submit), Toast.LENGTH_LONG).show()
    }

    override fun errorInSubmit() {
        Toast.makeText(context, activity?.resources?.getString(R.string.error), Toast.LENGTH_LONG).show()
    }

}