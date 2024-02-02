package lingoncraft.client

import baritone.api.BaritoneAPI
import com.mojang.logging.LogUtils
import dev.langchain4j.agent.tool.Tool

class PlayerFinder {
  private val log = LogUtils.getLogger

  case class PlayerPosition(x: Double, z: Double)

  @Tool(Array("Get the position of the player"))
  def whereAmI(): PlayerPosition = {
    log.info("whereAmI")
    val player =
      BaritoneAPI.getProvider.getPrimaryBaritone.getPlayerContext.player()
    val x = player.lastRenderX
    val z = player.lastRenderZ
    PlayerPosition(x, z)
  }
}
