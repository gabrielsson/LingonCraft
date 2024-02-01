package lingoncraft.client

import baritone.api.BaritoneAPI
import com.mojang.logging.LogUtils
import dev.langchain4j.agent.tool.Tool

object CommandProviderWrapper {
  private val LOGGER = LogUtils.getLogger

  class GotoChatCommand {
    @Tool(Array("""execute baritone commands like `command arg1 arg2`"""))
    private def baritoneRaw(command: String): Unit = {
      LOGGER.info("Executing command: " + command)
      BaritoneAPI.getProvider.getPrimaryBaritone.getCommandManager
        .execute(command)
    }
  }
}
