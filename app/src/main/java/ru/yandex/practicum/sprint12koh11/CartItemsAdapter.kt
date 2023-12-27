package ru.yandex.practicum.sprint12koh11

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class CartItemsAdapter : RecyclerView.Adapter<CartItemViewHolder>() {

    private var items: List<CartItem> = emptyList()

    var cartItemPlusListener: CartItemPlusListener? = null
    var cartItemMinusListener: CartItemMinusListener? = null

    fun updateItems(newItems: List<CartItem>) {
        val oldItems = items
        items = newItems

        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition].id == newItems[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldItems[oldItemPosition] == newItems[newItemPosition]
            }
        })
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        return CartItemViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.binding.plus.setOnClickListener {
            cartItemPlusListener?.onPlus(item)
        }
        holder.binding.minus.setOnClickListener {
            cartItemMinusListener?.onMinus(item)
        }
    }

    fun interface CartItemPlusListener {
        fun onPlus(item: CartItem)
    }

    fun interface CartItemMinusListener {
        fun onMinus(item: CartItem)
    }
}