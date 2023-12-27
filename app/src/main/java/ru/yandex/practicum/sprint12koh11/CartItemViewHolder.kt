package ru.yandex.practicum.sprint12koh11

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.yandex.practicum.sprint12koh11.databinding.VCartItemBinding
import java.text.DecimalFormat

class CartItemViewHolder(
    parentView: ViewGroup,
    val binding: VCartItemBinding = VCartItemBinding.inflate(
        LayoutInflater.from(parentView.context),
        parentView,
        false
    )
): RecyclerView.ViewHolder(
    binding.root
) {

    val currencyFormatter = DecimalFormat.getCurrencyInstance()

    fun bind(item: CartItem) {
        binding.title.text = item.product.title
        binding.price.text = currencyFormatter.format(item.product.price)
        binding.count.text = item.count.toString()
    }
}