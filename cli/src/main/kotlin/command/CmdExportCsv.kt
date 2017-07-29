package command

import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import com.github.rvesse.airline.annotations.help.Examples
import command.base.CmdCommonOptions
import command.base.CommandRunnable
import settings.Settings
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime

@Command(name = "csv", description = "Export into CSV file")
@Examples(examples = arrayOf("csv --period month -o output.csv 2017-01 2017-07"))
class CmdExportCsv : CmdCommonOptions(), CommandRunnable {

    @Option(name = arrayOf("-o", "--output"), description = "Output file. Omit it to print into console")
    private val fileName: String = ""

    override fun run(settings: Settings.Data) {
        if (!fileName.isEmpty()) {
            val file = File(fileName)
            if (file.exists()) {
                throw FileAlreadyExistsException(file)
            }
        }

        export(fetch(settings))
    }

    private fun export(meters: Map<LocalDateTime, Double?>) {
        if (fileName.isEmpty()) {
            meters.forEach { date, amount -> println("$date;$amount") }
        } else {
            FileOutputStream(fileName).use {
                meters.forEach { date, amount -> it.write("$date;$amount\n".toByteArray()) }
            }
        }
    }
}
