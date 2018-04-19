package sonder.notes.presentation.screens.notes

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sonder.notes.databinding.FragmentNotesListBinding
import sonder.notes.databinding.NotesListItemViewBinding
import sonder.notes.presentation.base.BaseFragment
import sonder.notes.presentation.screens.notes.data.entity.Note

class NotesListFragment : BaseFragment() {

    private lateinit var binding: FragmentNotesListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNotesListBinding.inflate(inflater, container, false)
        binding.callbacks = listener()
        initRecycler()
        return binding.root
    }

    private fun listener()= object : NotesListCallbacks {
        override fun onAdd() {
            (binding.recycler.adapter as Adapter).addNewItem()
        }
    }

    private fun initRecycler() {
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.adapter = Adapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Thread(runnable(this)).start()
    }

    private fun runnable(context: NotesListFragment) = Runnable {
        val items = arrayListOf(AdapterItem(Note(1)), AdapterItem(Note(2)), AdapterItem(Note(3)))
        context.root().runOnUiThread { (binding.recycler.adapter as Adapter).push(items) }
    }

    companion object {
        @JvmStatic fun newInstance() = NotesListFragment()
    }
}

class Adapter : RecyclerView.Adapter<ViewHolder>() {

    private var items = arrayListOf<AdapterItem>()

    fun push(newItems: List<AdapterItem>) {
        val diff = DiffImpl(items, newItems)
        items = ArrayList(newItems)
        DiffUtil.calculateDiff(diff).dispatchUpdatesTo(this)
    }

    fun addNewItem() {
        val newItems = ArrayList(items)
        newItems.add(AdapterItem(Note((items.size).toLong())))
        push(newItems)
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NotesListItemViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.item = items[position]
        holder.binding.executePendingBindings()
    }

}

class DiffImpl(
        private val oldItems: List<AdapterItem>,
        private val newItems: List<AdapterItem>)
: DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size
    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].note.id == newItems[newItemPosition].note.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].note == newItems[newItemPosition].note
    }
}

data class AdapterItem(val note: Note)

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding: NotesListItemViewBinding = DataBindingUtil.bind(view)!!
}