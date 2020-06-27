package org.dclm.live.ui.sermon

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import org.dclm.live.R
import org.dclm.live.databinding.ViewNoteFragmentBinding

class ViewNoteFragment : Fragment() {

    companion object {
        fun newInstance() = ViewNoteFragment()
    }

    private lateinit var viewModel: ViewNoteViewModel
    private lateinit var binding: ViewNoteFragmentBinding
    private lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.view_note_fragment, container, false)
        note =  arguments?.let { ViewNoteFragmentArgs.fromBundle(it).note }!!
        val topic: String = arguments?.let { ViewNoteFragmentArgs.fromBundle(it).note?.topic }!!
        val ref: String =  arguments?.let { ViewNoteFragmentArgs.fromBundle(it).note?.description }!!
        val point1: String =  arguments?.let { ViewNoteFragmentArgs.fromBundle(it).note?.point1}!!
        val point2: String =  arguments?.let { ViewNoteFragmentArgs.fromBundle(it).note?.point2 }!!
        val point3: String =  arguments?.let { ViewNoteFragmentArgs.fromBundle(it).note?.point3 }!!
        val point4: String = arguments?.let { ViewNoteFragmentArgs.fromBundle(it).note?.point4 }!!
        val point1ref: String =  arguments?.let { ViewNoteFragmentArgs.fromBundle(it).note?.point1_description }!!
        val point2ref: String =  arguments?.let { ViewNoteFragmentArgs.fromBundle(it).note?.point2_description }!!
        val point3ref: String =  arguments?.let { ViewNoteFragmentArgs.fromBundle(it).note?.point3_description }!!
        val point4ref: String =  arguments?.let { ViewNoteFragmentArgs.fromBundle(it).note?.point4_description }!!
        val speaker: String =  arguments?.let { ViewNoteFragmentArgs.fromBundle(it).note?.speaker }!!
        val decision: String =  arguments?.let { ViewNoteFragmentArgs.fromBundle(it).note?.decision }!!
        val service: String =  arguments?.let { ViewNoteFragmentArgs.fromBundle(it).note?.service }!!



        var body = "<p></p>"
        body = "$body <b>Speaker</b>"


        if (speaker.trim().isNotEmpty()) {
            body = "$body<p></p>$speaker"

        }
        body += "<p></p>"
        body = "$body <b>Service</b>"
        if (service.trim().isNotEmpty()) {
            body = "$body<p></p> $service<p></p><br></br>"

        }

        body += "<p></p>"
        body = "$body <b>Text</b>"
        if (ref.trim().isNotEmpty()) {
            body = "$body<p></p> $ref<p></p><br></br>"

        }

        body += "<p></p>"
        body = "$body <b>Point 1</b>"

        if (point1.trim().isNotEmpty()) {
            body = "$body<p></p>$point1"

        }
        if (point1ref.trim().isNotEmpty()) {
            body = "$body<p></p>$point1ref<p></p><br></br>"

        }

        body += "<p></p>"
        body = "$body <b>Point 2</b>"
        if (point2.trim().isNotEmpty()) {
            body = "$body<p></p>$point2"

        }
        if (point2ref.trim().isNotEmpty()) {
            body = "$body<p></p>$point2ref<p></p><br></br>"

        }

        body += "<p></p>"
        body = "$body <b>Point 3</b>"
        if (point3.trim().isNotEmpty()) {
            body = "$body<p></p>$point3"

        }
        if (point3ref.trim().isNotEmpty()) {
            body = "$body<p></p>$point3ref<p></p><br></br>"

        }

        body += "<p></p>"
        body = "$body <b>Point 4</b>"
        if (point4.trim().isNotEmpty()) {
            body = "$body<p></p>$point4"

        }
        if (point4ref.trim().isNotEmpty()) {
            body = "$body<p></p>$point4ref<p></p><br></br>"

        }
        body += "<p></p>"
        body = "$body <b>Decision</b>"
        if (decision.trim().isNotEmpty()) {
            body = "$body<p></p>$decision"

        }
        body += "<p></p><br></br>"


        binding.body.setText(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml("<p align=\"justify\"> $body</p>", Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml("<p align=\"justify\"> $body</p>")
        })
        binding.topic.setText(topic)
        binding.edit.setOnClickListener { v ->
            val action = ViewNoteFragmentDirections.actionViewNoteFragmentToAddNoteFragment(note)
            Navigation.findNavController(v).navigate(action)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ViewNoteViewModel::class.java)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> activity?.let {
                ShareCompat.IntentBuilder
                    .from(it)
                    .setType("text/plain")
                    .setChooserTitle(R.string.share)
                    .setText(binding.body.text.toString())
                    .setSubject(binding.topic.text.toString())
                    .startChooser()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}