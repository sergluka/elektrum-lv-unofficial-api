package settings

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import java.nio.file.Files
import java.nio.file.Paths
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.nodes.Tag

open class Settings {

    open class Credentials(open var login: String = "", open var password: Password = Password())
    data class InfluxDb(var url: String = "", var database: String = "") : Credentials()

    data class Data(var elektrum: Credentials = Credentials(), var influxdb: InfluxDb = InfluxDb()) {

        fun process(): Boolean {
            val pass1_changed = elektrum.password.process()
            val pass2_changed = influxdb.password.process()
            return pass1_changed || pass2_changed
        }

        fun clearPlainPasswords() {
            elektrum.password.plain = ""
            influxdb.password.plain = ""
        }
    }


    fun get(path: String, fn: (data: Data) -> Unit) {
        val yaml = Yaml(Constructor(Data::class.java))
        val data = yaml.load(read(path)) as Data
        val changed = data.process()
        fn(data)
        if (changed) {
            data.clearPlainPasswords()
            write(path, data)
        }
    }

    open fun read(path: String): String {
        Files.newBufferedReader(Paths.get(path)).use {
            return it.readText()
        }
    }

    open fun write(path: String, data: Data) {
        val yaml = Yaml()
        val text = yaml.dumpAs(data, Tag.MAP, DumperOptions.FlowStyle.BLOCK)
        Files.newBufferedWriter(Paths.get(path)).use {
            it.write(text)
        }
    }
}
