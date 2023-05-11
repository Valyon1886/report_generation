package com.example.document.services

import com.aspose.words.*
import com.example.document.models.Employer
import com.example.document.models.User
import com.mongodb.client.MongoClients
import com.mongodb.client.gridfs.GridFSBuckets
import com.mongodb.client.gridfs.model.GridFSUploadOptions
import com.mongodb.client.model.Filters
import okhttp3.*
import okhttp3.RequestBody.Companion.asRequestBody
import org.bson.Document
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class DocumentService {

    val pathTemplateFile:String = "C:\\Users\\DenielP\\IdeaProjects\\Document\\src\\main\\kotlin\\com\\example\\document\\templates\\temp.docx"
    val pathResFile:String = "C:\\Users\\DenielP\\IdeaProjects\\Document\\src\\main\\kotlin\\com\\example\\document\\templates\\new.docx"
    val pathResFile2:String = "C:\\Users\\DenielP\\IdeaProjects\\Document\\src\\main\\kotlin\\com\\example\\document\\templates\\new2.docx"

    class Sender(val brigadirName: String, val taskId: String,
                 val listOfMembers: String, val taskDate: String?, val currentDate: String, val taskName: String?,
                 val taskCostFull: Int?, val taskCostPer: String)

    fun getFullName(user: User): String = user.lastName + " " + user.firstName + " " + user.secondName

    fun getAllEmployers(user: User): String{
        var res: String = ""
        for (i in user.employers!!){
            res+=i.firstName + " " + i.secondName + " " + i.lastName
            if (user.employers!!.last()!=i)
                res+=", "
        }
        return res
    }

    fun getMainCost(user: User, jobId: Int): Int{
        var res: Int = 0;
        val completedSubTasks = user.completedTasks?.get(jobId)?.completedSubTasks
        if (completedSubTasks != null) {
            for (i in completedSubTasks) {
                for (j in i.employers!!) {
                    res+=j.cost*j.clock
                }
                for (j in i.materials!!){
                    res+= j.count!! * j.cost!!
                }
            }
        }
//        for (i in user.completedTasks?.get(jobId)?.completedSubTasks){
//            for (j in i.employers!!) {
//                res+=j.cost*j.clock
//            }
//            for (j in i.materials!!){
//                res+= j.count!! * j.cost!!
//            }
//        }
        return res
    }

    fun tableGenerate(user: User, jobId: Int): com.aspose.words.Document{
        var ind: Int = 1
        var ind2: Int = 1
        val doc = Document(pathResFile)
        val table1 = doc.getFirstSection().getBody().getTables().get(0)
        val table2 = doc.getFirstSection().getBody().getTables().get(1)

        val completedSubTasks = user.completedTasks?.get(jobId)?.completedSubTasks
        if (completedSubTasks != null) {
            for (i in completedSubTasks.indices) {
                val temp_employers: MutableList<Employer>? = completedSubTasks[i].employers

                if (temp_employers != null) {
                    for (k in temp_employers) {
                        table1.getRows().add(Row(doc))
                        table1.rows.get(ind2).getCells().add(Cell(doc))
                        table1.rows.get(ind2).cells.get(0).appendChild(Paragraph((doc)))
                        table1.rows.get(ind2).cells.get(0).getFirstParagraph().getRuns()  // number
                            .add(Run(doc, (ind2).toString()))

                        table1.rows.get(ind2).getCells().add(Cell(doc))
                        table1.rows.get(ind2).cells.get(1).appendChild(Paragraph((doc)))
                        table1.rows.get(ind2).cells.get(1).getFirstParagraph().getRuns()  // task name
                            .add(Run(doc, completedSubTasks[i].name))

                        table1.rows.get(ind2).getCells().add(Cell(doc))
                        table1.rows.get(ind2).cells.get(2).appendChild(Paragraph((doc)))
                        table1.rows.get(ind2).cells.get(2).getFirstParagraph().getRuns()  // fio of employee
                                .add(Run(doc, k.lastName+" "+ k.firstName+" "
                                        +k.secondName))
                        

                        table1.rows.get(ind2).getCells().add(Cell(doc))
                        table1.rows.get(ind2).cells.get(3).appendChild(Paragraph((doc)))
                        table1.rows.get(ind2).cells.get(3).getFirstParagraph().getRuns()  // clock of employer
                                .add(Run(doc, k.clock.toString()))
                        

                        table1.rows.get(ind2).getCells().add(Cell(doc))
                        table1.rows.get(ind2).cells.get(4).appendChild(Paragraph((doc)))
                        table1.rows.get(ind2).cells.get(4).getFirstParagraph().getRuns()  // cost of emloyer
                                .add(Run(doc, k.cost.toString()))

                        table1.rows.get(ind2).getCells().add(Cell(doc))
                        table1.rows.get(ind2).cells.get(5).appendChild(Paragraph((doc)))
                        table1.rows.get(ind2).cells.get(5).getFirstParagraph().getRuns()  // main cost
                                .add(Run(doc, (k.clock*k.cost).toString()))

                        ind2++
                    }
                }
                


                val materials = completedSubTasks[i].materials
                if (materials != null) {
                    for (j in materials) {
                        table2.getRows().add(Row(doc))
                        table2.rows.get(ind).getCells().add(Cell(doc))
                        table2.rows.get(ind).cells.get(0).appendChild(Paragraph((doc)))
                        table2.rows.get(ind).cells.get(0).getFirstParagraph().getRuns()  // task name
                            .add(Run(doc, ind.toString()))

                        table2.rows.get(ind).getCells().add(Cell(doc))
                        table2.rows.get(ind).cells.get(1).appendChild(Paragraph((doc)))
                        table2.rows.get(ind).cells.get(1).getFirstParagraph().getRuns()  // fio of employee
                            .add(Run(doc, j.name))

                        table2.rows.get(ind).getCells().add(Cell(doc))
                        table2.rows.get(ind).cells.get(2).appendChild(Paragraph((doc)))
                        table2.rows.get(ind).cells.get(2).getFirstParagraph().getRuns()  // clock of employer
                            .add(Run(doc, (j.count).toString()))

                        table2.rows.get(ind).getCells().add(Cell(doc))
                        table2.rows.get(ind).cells.get(3).appendChild(Paragraph((doc)))
                        table2.rows.get(ind).cells.get(3).getFirstParagraph().getRuns()  // cost of emloyer
                            .add(Run(doc, (j.cost).toString()))

                        table2.rows.get(ind).getCells().add(Cell(doc))
                        table2.rows.get(ind).cells.get(4).appendChild(Paragraph((doc)))
                        table2.rows.get(ind).cells.get(4).getFirstParagraph().getRuns()  // main cost
                            .add(Run(doc, (j.count?.times(j.cost!!)).toString()))
                        ind++
                    }
                }
            }
        }

        return doc

    }

    fun docGenerate(user: User, jobId: Int){
        var brName: String = getFullName(user)
        var id: Long? = user.completedTasks?.get(jobId)?.id
        var members: String = getAllEmployers(user)
        var date: String? = user.completedTasks?.get(jobId)?.beginDate
        var name: String? = user.completedTasks?.get(jobId)?.name
        var costRub: Int = getMainCost(user, jobId)
        val localDate = LocalDate.now() //For reference
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        val nameDoc: String = name.toString()+"_"+user.id.toString()

        var doc = Document(pathTemplateFile)
        val sender = Sender(brName, id.toString(), members, date, localDate.format(formatter), name, costRub*100/100, "00")
        val engine = ReportingEngine()
        engine.buildReport(doc, sender, "s")
        doc.save(pathResFile)
        doc = tableGenerate(user, jobId)
        doc.save(pathResFile)
        println("Все сработало!")
        val client = MongoClients.create("mongodb://localhost:27017/renovations")
        val database = client.getDatabase("renovations")
        val gridFSBucket = GridFSBuckets.create(database)//?

        val stream = ByteArrayOutputStream()
        doc.save(stream, SaveFormat.DOCX)

        val bytes = stream.toByteArray()
        val session = client.startSession()

        val metaData = Document()
        metaData.append("filename", "$nameDoc.docx")

        val uploadOptions = GridFSUploadOptions()
            .chunkSizeBytes(1024 * 1024)
            .metadata(metaData)

        gridFSBucket.uploadFromStream(session, nameDoc, ByteArrayInputStream(bytes), uploadOptions)
        session.close()

        File(pathResFile).delete()
    }

    fun getAllFileNames(userId: Int): MutableList<String>{
        val client = MongoClients.create("mongodb://localhost:27017/renovations")
        val database = client.getDatabase("renovations")
        val gridFSBucket = GridFSBuckets.create(database)

        val cursor = gridFSBucket.find()
        val fileNames = mutableListOf<String>()

        for (gridFSFile in cursor) {
            val fileName = gridFSFile.filename
            if (fileName.contains(userId.toString())) {
                fileNames.add(fileName)
            }
        }
        return fileNames
    }

    fun dowloadFile(fileName: String): ResponseEntity<ByteArray> {
        val client = MongoClients.create("mongodb://localhost:27017/renovations")
        val database = client.getDatabase("renovations")
        val gridFSBucket = GridFSBuckets.create(database)
        val filter = Filters.eq("filename", fileName)
        val fileInfo = gridFSBucket.find(filter).first()
        val fileId = fileInfo?.id
        val downloadStream = fileId?.let { gridFSBucket.openDownloadStream(it) }
        val outputStream = ByteArrayOutputStream()

        downloadStream.use { input ->
            outputStream.use { output ->
                input?.copyTo(output)
            }
        }

        val doc2 = Document(ByteArrayInputStream(outputStream.toByteArray()))
        doc2.save(pathResFile2)
        val response: ResponseEntity<ByteArray> = ResponseEntity(outputStream.toByteArray(), HttpStatus.OK)
        val file = File(pathResFile2)
        val inputStream = FileInputStream(file)
        val inputStreamResource = InputStreamResource(inputStream)

//        return ResponseEntity.ok()
//            .contentType(MediaType("application/octet-stream"))
//            .contentLength(file.length())
//            .body()

//        File(pathResFile2).delete()
        return response
//        val file = File(pathResFile2)
//        val requestBody = MultipartBody.Builder()
//            .setType(MultipartBody.FORM)
//            .addFormDataPart("file", file.name, file.asRequestBody("application/octet-stream".toMediaTypeOrNull()))
//            .build()


    }

}