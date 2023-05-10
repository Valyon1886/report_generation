package com.example.document.models

import org.springframework.data.mongodb.core.mapping.Document

@Document("document")
data class Document(
    val id: Int,
    val name: String
)