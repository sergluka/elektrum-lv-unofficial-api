import com.github.rvesse.airline.Cli
import com.github.rvesse.airline.parser.errors.ParseException
import command.CmdExportCsv
import command.CmdExportInfluxDb
import command.base.CmdHelp
import command.base.CommandRunnable
import command.base.CustomTypeConverter
import settings.Settings

fun main(args : Array<String>) {

    val builder = Cli.builder<CommandRunnable>("app")
        .withDescription("Get measures of electric meters from 'elektrum.lv' and exports into CSV or InfluxDB")
        .withDefaultCommand(CmdHelp::class.java)
        .withCommands(CmdHelp::class.java, CmdExportCsv::class.java, CmdExportInfluxDb::class.java)
    builder.withParser().withTypeConverter(CustomTypeConverter())
    val parser = builder.build()

    val cmd: CommandRunnable
    try {
        cmd = parser.parse(*args)
    } catch (e: ParseException) {
        throw Exception("Wrong command line: ${args.toList()}: ${e.message}")
    }

    val settings = Settings()
    settings.get("settings.yml") {
        cmd.run(it)
    }
}


