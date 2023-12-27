package ru.yandex.practicum.sprint12koh11

import java.math.BigDecimal

data class Product(
    val id: String,
    val title: String,
    val price: BigDecimal,
)

data class CartItem(
    val id: String,
    val product: Product,
    val count: Int,
)