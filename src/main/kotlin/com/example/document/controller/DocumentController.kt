package com.example.document.controller

import com.example.document.models.User
import com.example.document.services.DocumentService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/doc")
class DocumentController(private val documentService: DocumentService) {

    @PostMapping("/add/{jobId}")
    @ResponseBody
    fun docGenerate(@RequestBody user: User, @PathVariable jobId: Int) = documentService.docGenerate(user, jobId)
}