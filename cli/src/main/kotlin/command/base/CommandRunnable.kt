package command.base

import settings.Settings

interface CommandRunnable {
    fun run(settings: Settings.Data)
}
