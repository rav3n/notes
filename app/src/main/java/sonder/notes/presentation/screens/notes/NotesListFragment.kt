package sonder.notes.presentation.screens.notes

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.DividerItemDecoration.VERTICAL
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sonder.notes.R
import sonder.notes.databinding.FragmentNotesListBinding
import sonder.notes.databinding.NotesListItemViewBinding
import sonder.notes.presentation.base.BaseFragment
import sonder.notes.presentation.screens.editor.EditorFragment
import sonder.notes.presentation.screens.notes.data.entity.Note

class NotesListFragment : BaseFragment() {

    private lateinit var binding: FragmentNotesListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNotesListBinding.inflate(inflater, container, false)
        binding.callbacks = listener()
        initRecycler()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notesViewModel().notes.observe(this, Observer {
            val items = arrayListOf<AdapterItem>()
            it!!.forEach { items.add(AdapterItem(it)) }
            (binding.recycler.adapter as Adapter).push(items)
        })
    }

    private fun listener() = object : NotesListCallbacks {
        override fun onAdd() {
            root().pushFragment(EditorFragment.newInstance(0,""), true)
        }
    }

    private fun initRecycler() {
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.addItemDecoration(itemDecorator())
        binding.recycler.adapter = Adapter()
    }

    private fun itemDecorator(): RecyclerView.ItemDecoration? {
        val decoration = DividerItemDecoration(context, VERTICAL)
        decoration.setDrawable(ContextCompat.getDrawable(context!!,R.drawable.notes_list_item_decorator)!!)
        return decoration
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