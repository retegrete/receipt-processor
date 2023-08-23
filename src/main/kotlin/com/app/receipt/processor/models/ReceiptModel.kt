package com.app.receipt.processor.models

import jakarta.persistence.ElementCollection
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class ReceiptModel(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        var retailer: String,
        var purchaseDate: String,
        var purchaseTime: String,
        @ElementCollection
        var items: List<ItemDescription>,
        var total: String
)
@Embeddable
data class ItemDescription(
        var shortDescription: String,
        var price: String
)

