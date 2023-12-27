package ru.yandex.practicum.sprint12koh11

import android.content.SharedPreferences
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import ru.yandex.practicum.sprint12koh11.databinding.ActivityMainBinding
import java.math.BigDecimal


class MainActivity : AppCompatActivity() {

    val cartItemsAdapter = CartItemsAdapter()
    lateinit var cart: Cart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val masterAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            masterAlias,
            applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        sharedPreferences.edit {
            putString("my_secret", "secret")
        }


        binding.itemsRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = cartItemsAdapter
        }
        cart = Cart(applicationContext)
        cartItemsAdapter.updateItems(cart.getItems())
        cart.cartItemsListener = Cart.CartItemsListener { changedItems ->
            cartItemsAdapter.updateItems(changedItems)
            binding.sumBtn.text = changedItems.map {
                it.product.price.multiply(BigDecimal(it.count))
            }.sumOf { it }.toString()
        }

        Log.d("SPRINT_11", "getCartItemsFromServer ${cart.getItems()}")


        cartItemsAdapter.cartItemPlusListener = CartItemsAdapter.CartItemPlusListener { cartItem ->
//            val mutableItems = items.toMutableList()
//            val item = mutableItems.find { it.id == cartItem.id }
//            if (item != null) {
//                val newItem = item.copy(count = item.count + 1)
//                val index = mutableItems.indexOf(item)
//                mutableItems[index] = newItem
//            }
//            items = mutableItems
//            items = items.map {
//                if (it.id == cartItem.id) {
//                    it.copy(count = it.count + 1)
//                } else {
//                    it
//                }
//            }
            cart.onItemPlus(cartItem)
        }
        cartItemsAdapter.cartItemMinusListener =
            CartItemsAdapter.CartItemMinusListener { cartItem ->
//            val mutableItems = items.toMutableList()
//            val item = mutableItems.find { it.id == cartItem.id }
//            if (item != null) {
//                if (item.count == 1) {
//                    mutableItems.remove(item)
//                } else {
//                    val newItem = item.copy(count = item.count - 1)
//                    val index = mutableItems.indexOf(item)
//                    mutableItems[index] = newItem
//                }
//            }
//            items = mutableItems
//            items = items.mapNotNull {
//                if (it.id == cartItem.id) {
//                    if (it.count == 1) {
//                        null
//                    } else {
//                        it.copy(count = it.count - 1)
//                    }
//                } else {
//                    it
//                }
//            }
//                .filter { it.count > 0 }
                cart.onItemMinus(cartItem)
            }

    }

    override fun onStop() {
        super.onStop()
        cart.onStop()
    }


}