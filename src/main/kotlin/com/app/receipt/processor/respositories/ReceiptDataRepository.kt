package com.app.receipt.processor.respositories

import com.app.receipt.processor.models.ReceiptModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/* The ReceiptDataRepository interface is a repository for managing ReceiptModel entities in a Kotlin application. */
@Repository
interface ReceiptDataRepository: JpaRepository<ReceiptModel, Long>