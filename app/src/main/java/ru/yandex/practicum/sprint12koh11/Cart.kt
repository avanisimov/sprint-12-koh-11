package ru.yandex.practicum.sprint12koh11

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal
import java.util.UUID
import kotlin.random.Random

class Cart(
    private val context: Context
) {

    private val sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE)
    private val gson: Gson = Gson()

    private var items: List<CartItem>? = null

    var cartItemsListener:CartItemsListener? = null


    fun getItems(): List<CartItem> {
        if (items == null) {
            val itemsFromCache = getItemsFromCache()
            items = if (itemsFromCache != null) {
                itemsFromCache
            } else {
                val itemsFromServer = getCartItemsFromServer()
                saveItemsToCache(itemsFromServer)
                itemsFromServer
            }
        }
        return items ?: emptyList()
    }

    fun onItemPlus(cartItem: CartItem) {
        items = items?.map {
            if (it.id == cartItem.id) {
                it.copy(count = it.count + 1)
            } else {
                it
            }
        }
        items?.also {
            cartItemsListener?.onItemsChanged(it)
        }
    }

    fun onItemMinus(cartItem: CartItem) {
        items = items?.mapNotNull {
            if (it.id == cartItem.id) {
                if (it.count == 1) {
                    null
                } else {
                    it.copy(count = it.count - 1)
                }
            } else {
                it
            }
        }
        items?.also {
            cartItemsListener?.onItemsChanged(it)
        }
    }

    fun onStop() {
        items?.also {
            saveItemsToCache(it)
        }
    }

    private fun getItemsFromCache(): List<CartItem>? {
        val json: String? = sharedPreferences.getString("items", null)
        return if (json != null) {
            gson.fromJson<List<CartItem>>(json,  object : TypeToken<List<CartItem>>() {}.type)
        } else {
            null
        }
    }

    private fun saveItemsToCache(newItems: List<CartItem>) {
        val json = gson.toJson(newItems)
        sharedPreferences.edit()
            .putString("items", json)
            .apply()
    }

    private fun getCartItemsFromServer(): List<CartItem> {
        return (1..5).map { index ->
            CartItem(
                id = "item_$index",
                product = Product(
                    id = UUID.randomUUID().toString(),
                    title = "Товар $index",
                    price = BigDecimal(Random.nextInt(100, 200)).divide(BigDecimal(100))
                ),
                count = 1
            )
        }
    }

    fun interface CartItemsListener {
        fun onItemsChanged(items: List<CartItem>)
    }
}