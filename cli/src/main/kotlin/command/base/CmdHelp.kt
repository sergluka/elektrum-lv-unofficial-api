package command.base

import com.github.rvesse.airline.help.Help
import settings.Settings

class CmdHelp<T> : CommandRunnable, Help<T>() {
    override fun run(settings: Settings.Data) {
        super.run()
    }
}
