package sonder.notes.presentation.screens.notes

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.graphics.Canvas
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.DividerItemDecoration.VERTICAL
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import sonder.notes.R
import sonder.notes.databinding.FragmentNotesListBinding
import sonder.notes.databinding.NotesListItemViewBinding
import sonder.notes.presentation.base.ApplicationActivity
import sonder.notes.presentation.base.BaseFragment
import sonder.notes.presentation.screens.editor.EditorFragment
import sonder.notes.presentation.screens.notes.actions.RecyclerActions
import sonder.notes.presentation.screens.notes.data.entity.Note

class NotesListFragment : BaseFragment() {

    private lateinit var binding: FragmentNotesListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
            binding.tipVisibility = items.isEmpty()
        })
        notesViewModel().fetchData()
    }


    private fun listener() = object : NotesListCallbacks {
        override fun onAdd() {
            root().pushFragment(EditorFragment.newInstance(0,""), true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        activity!!.menuInflater.inflate(R.menu.list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.menu_list_clear) {
            notesViewModel().deleteAll()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecycler() {
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.addItemDecoration(itemDecorator())
        binding.recycler.adapter = Adapter(RecyclerActionsImpl(root()))
        ItemTouchHelper(ItemTouchHelperCallback(recyclerListener)).attachToRecyclerView(binding.recycler)
    }

    private val recyclerListener = object : RecyclerItemTouchHelperListener {
        override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int, position: Int) {
            val id = binding.recycler.adapter.getItemId(position)
            notesViewModel().delete(id)
        }
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

class RecyclerActionsImpl(
    private val root: ApplicationActivity
) : RecyclerActions {
    override fun onEditAction(note: Note){
        root.pushFragment(EditorFragment.newInstance(note.id, note.title), true)
    }
}

class Adapter(
    private val recyclerActionsImpl: RecyclerActionsImpl
) : RecyclerView.Adapter<ViewHolder>() {

    private var items = arrayListOf<AdapterItem>()

    fun push(newItems: List<AdapterItem>) {
        val diff = DiffImpl(items, newItems)
        items = ArrayList(newItems)
        DiffUtil.calculateDiff(diff).dispatchUpdatesTo(this)
    }

    override fun getItemCount() = items.size
    override fun getItemId(position: Int) = items[position].note.id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NotesListItemViewBinding.inflate(inflater, parent, false)
        binding.callbacks = recyclerActionsImpl
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
    val viewForeground: View by lazy { binding.viewForeground }
}

interface RecyclerItemTouchHelperListener {
    fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int, position: Int)
}

class ItemTouchHelperCallback(
    private val listener: RecyclerItemTouchHelperListener
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            val foreground = (it as ViewHolder).viewForeground
            ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foreground)
        }
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        return true
    }

    override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        viewHolder?.let {
            val foreground = (it as ViewHolder).viewForeground
            ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, foreground, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun onChildDrawOver(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        viewHolder?.let {
            val foreground = (it as ViewHolder).viewForeground
            ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(c, recyclerView, foreground, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {
        viewHolder?.let {
            val foreground = (it as ViewHolder).viewForeground
            ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foreground)
        }
    }

}

