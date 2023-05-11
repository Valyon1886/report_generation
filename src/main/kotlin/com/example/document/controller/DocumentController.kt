package com.example.document.controller

import com.aspose.words.Document
import com.example.document.models.User
import com.example.document.services.DocumentService
import org.apache.coyote.Response
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.io.File


@RestController
@RequestMapping("/doc")
class DocumentController(private val documentService: DocumentService) {

    @PostMapping("/add/{jobId}")
    @ResponseBody
    fun docGenerate(@RequestBody user: User, @PathVariable jobId: Int) = documentService.docGenerate(user, jobId)

    @GetMapping("/getAllFileNames/{userId}")
    @ResponseBody
    fun getAllFileNames(@PathVariable userId: Int): MutableList<String> = documentService.getAllFileNames(userId)

    @GetMapping("/getFile/{fileName}")
    fun dowloadFile(@PathVariable fileName: String): ResponseEntity<ByteArray> = documentService.dowloadFile(fileName)
}