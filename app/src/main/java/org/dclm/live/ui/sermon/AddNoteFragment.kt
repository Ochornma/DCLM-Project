package org.dclm.live.ui.sermon

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import org.dclm.live.R
import org.dclm.live.databinding.AddNoteFragmentBinding
import java.text.DateFormat
import java.util.*

class AddNoteFragment : Fragment() {
    private  var noteId: Note? = null
    private lateinit var date1: String

    companion object {
        fun newInstance() = AddNoteFragment()
    }

    private lateinit var viewModel: AddNoteViewModel
    private lateinit var binding: AddNoteFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_note_fragment, container, false)
        noteId = arguments?.let { AddNoteFragmentArgs.fromBundle(it).note}
        if (noteId != null){
           binding.addSpeaker1.setText(noteId?.speaker)
            binding.addSpeaker1.setText(noteId?.speaker)
            binding.addService1.setText(noteId?.service)
            binding.addTopic1.setText(noteId?.topic)
            binding.addTopicRef1.setText(noteId?.description)
            binding.addPoint11.setText(noteId?.point1)
            binding.addPoint1Ref1.setText(noteId?.point1_description)
            binding.addPoint21.setText(noteId?.point2)
            binding.addPoint2Ref1.setText(noteId?.point2_description)
            binding.addPoint31.setText(noteId?.point3)
            binding.addPoint3Ref1.setText(noteId?.point3_description)
            binding.addPoint41.setText(noteId?.point4)
            binding.addPoint4Ref1.setText(noteId?.point4_description)
            binding.addDecision1.setText(noteId?.decision)
        }
        val calendar = Calendar.getInstance()
        date1 = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = context?.let { AddNoteFactory(it) }
        viewModel = factory?.let { ViewModelProvider(this, it).get(AddNoteViewModel::class.java) }!!
        val calendar = Calendar.getInstance()
        val date1 = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)
        binding.addDate.text = date1
        binding.save.setOnClickListener {
            actionOnNote()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        actionOnNote()
        return super.onOptionsItemSelected(item)
    }

    private fun actionOnNote(){
        if (noteId == null){
            if (binding.addTopic1.text.toString().isNotEmpty() or binding.addTopicRef1.text.toString().isNotEmpty()){
                val note = Note(date= date1, speaker = binding.addSpeaker1.text.toString(), service = binding.addService1.text.toString(), topic = binding.addTopic1.text.toString(),  description = binding.addTopicRef1.text.toString(),
                    point1 = binding.addPoint11.text.toString(), point1_description = binding.addPoint1Ref1.text.toString(), point2 = binding.addPoint21.text.toString(), point2_description = binding.addPoint2Ref1.text.toString(),
                    point3 = binding.addPoint31.text.toString(), point3_description = binding.addPoint3Ref1.text.toString(),
                    point4 = binding.addPoint41.text.toString(), point4_description = binding.addPoint4Ref1.text.toString(), decision = binding.addDecision1.text.toString())
                viewModel.insert(note)
                Navigation.findNavController(binding.root).navigate(R.id.action_addNoteFragment_to_sermonFragment)
            } else{
                Toast.makeText(context, getString(R.string.topic_and_reference), Toast.LENGTH_SHORT).show()
            }
        } else{
            val note = Note( id = noteId!!.id, date= noteId?.date!!, speaker = binding.addSpeaker1.text.toString(), service = binding.addService1.text.toString(), topic = binding.addTopic1.text.toString(),
                point1 = binding.addPoint11.text.toString(), point1_description = binding.addPoint1Ref1.text.toString(), point2 = binding.addPoint21.text.toString(), point2_description = binding.addPoint2Ref1.text.toString(),
                point3 = binding.addPoint31.text.toString(), point3_description = binding.addPoint3Ref1.text.toString(),
                description = binding.addTopicRef1.text.toString(), point4 = binding.addPoint41.text.toString(), point4_description = binding.addPoint4Ref1.text.toString(), decision = binding.addDecision1.text.toString())
            viewModel.update(note)
            Navigation.findNavController(binding.root).navigate(R.id.action_addNoteFragment_to_sermonFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (noteId == null){
            if (binding.addTopic1.text.toString().isNotEmpty() or binding.addTopicRef1.text.toString().isNotEmpty()){
                val note = Note(date= date1, speaker = binding.addSpeaker1.text.toString(), service = binding.addService1.text.toString(), topic = binding.addTopic1.text.toString(),  description = binding.addTopicRef1.text.toString(),
                    point1 = binding.addPoint11.text.toString(), point1_description = binding.addPoint1Ref1.text.toString(), point2 = binding.addPoint21.text.toString(), point2_description = binding.addPoint2Ref1.text.toString(),
                    point3 = binding.addPoint31.text.toString(), point3_description = binding.addPoint3Ref1.text.toString(),
                    point4 = binding.addPoint41.text.toString(), point4_description = binding.addPoint4Ref1.text.toString(), decision = binding.addDecision1.text.toString())
                viewModel.insert(note)
            } else{
                Toast.makeText(context, getString(R.string.topic_and_reference), Toast.LENGTH_SHORT).show()
            }
        } else{
            val note = Note( id = noteId!!.id, date= noteId?.date!!, speaker = binding.addSpeaker1.text.toString(), service = binding.addService1.text.toString(), topic = binding.addTopic1.text.toString(),
                point1 = binding.addPoint11.text.toString(), point1_description = binding.addPoint1Ref1.text.toString(), point2 = binding.addPoint21.text.toString(), point2_description = binding.addPoint2Ref1.text.toString(),
                point3 = binding.addPoint31.text.toString(), point3_description = binding.addPoint3Ref1.text.toString(),
                description = binding.addTopicRef1.text.toString(), point4 = binding.addPoint41.text.toString(), point4_description = binding.addPoint4Ref1.text.toString(), decision = binding.addDecision1.text.toString())
            viewModel.update(note)
        }
    }

}