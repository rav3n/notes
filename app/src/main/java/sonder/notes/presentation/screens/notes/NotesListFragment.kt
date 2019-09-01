package sonder.notes.presentation.screens.notes

import androidx.lifecycle.Observer
import android.graphics.Canvas
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import android.view.*
import androidx.recyclerview.widget.*
import kotlinx.android.synthetic.main.fragment_notes_list.*
import kotlinx.android.synthetic.main.notes_list_item_view.view.*
import sonder.notes.R
import sonder.notes.data.entities.Note
import sonder.notes.presentation.base.ApplicationActivity
import sonder.notes.presentation.base.BaseFragment
import sonder.notes.presentation.screens.editor.EditorFragment
import sonder.notes.presentation.screens.notes.actions.RecyclerActions

class NotesListFragment : BaseFragment() {

    private val recyclerListener = object : RecyclerItemTouchHelperListener {
        override fun onSwiped(holder: RecyclerView.ViewHolder, direction: Int, position: Int) {
            recycler.adapter?.getItemId(position)?.apply {
                notesViewModel().delete(this)
            }
        }
    }

    private val touchHelper = ItemTouchHelper(ItemTouchHelperCallback(recyclerListener))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notes_list, container, false)
    }

    private fun itemDecorator(): RecyclerView.ItemDecoration {
        return DividerItemDecoration(requireContext(), VERTICAL).apply {
            setDrawable(ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.notes_list_item_decorator
                )!!
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_add.setOnClickListener {
            root().pushFragment(EditorFragment.newInstance(0,""))
        }
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.addItemDecoration(itemDecorator())
        recycler.adapter = Adapter(RecyclerActionsImpl(root()))
        touchHelper.attachToRecyclerView(recycler)

        notesViewModel().notes.observe(this, Observer {
            tip.visibility = if (it.isNullOrEmpty()) View.VISIBLE else View.GONE
            val items = arrayListOf<AdapterItem>()
            it?.forEach { items.add(AdapterItem(it)) }
            (recycler.adapter as Adapter).push(items)
        })
        notesViewModel().fetchData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_list_clear) {
            notesViewModel().deleteAll()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic fun newInstance() = NotesListFragment()
    }
}

class RecyclerActionsImpl(
    private val root: ApplicationActivity
) : RecyclerActions {
    override fun onEditAction(note: Note){
        root.pushFragment(EditorFragment.newInstance(note.id, note.text))
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
        return ViewHolder(inflater.inflate(R.layout.notes_list_item_view, parent, false)) {
            recyclerActionsImpl.onEditAction(items[it].note)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.title.text = items[position].note.text
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

class ViewHolder(view: View, callback: (index: Int) -> Unit) : RecyclerView.ViewHolder(view) {
    init {
        view.setOnClickListener { callback.invoke(adapterPosition) }
    }
    val viewForeground: View by lazy { view.view_foreground }
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

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        viewHolder.let {
            val foreground = (it as ViewHolder).viewForeground
            ItemTouchHelper.Callback.getDefaultUIUtil().onDraw(c, recyclerView, foreground, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        viewHolder.let {
            val foreground = (it as ViewHolder).viewForeground
            ItemTouchHelper.Callback.getDefaultUIUtil().onDrawOver(c, recyclerView, foreground, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        viewHolder.let {
            val foreground = (it as ViewHolder).viewForeground
            ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foreground)
        }
    }

}

