package com.example.document.services

import com.aspose.words.*
import com.example.document.models.Employer
import com.example.document.models.User
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class DocumentService {

    val pathTemplateFile:String = "C:\\Users\\DenielP\\IdeaProjects\\Document\\src\\main\\kotlin\\com\\example\\document\\templates\\temp.docx"
    val pathResFile:String = "C:\\Users\\DenielP\\IdeaProjects\\Document\\src\\main\\kotlin\\com\\example\\document\\templates\\new.docx"

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

    fun tableGenerate(user: User, jobId: Int){
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

        doc.save(pathResFile)

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

        val doc = Document(pathTemplateFile)
        val sender = Sender(brName, id.toString(), members, date, localDate.format(formatter), name, costRub*100/100, "00")
        val engine = ReportingEngine()
        engine.buildReport(doc, sender, "s")
        doc.save(pathResFile)
        tableGenerate(user, jobId)
        println("Все сработало!")

    }

}