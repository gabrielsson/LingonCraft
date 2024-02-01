package lingoncraft.client

import baritone.api.BaritoneAPI
import baritone.api.pathing.goals.GoalXZ
import com.mojang.logging.LogUtils
import dev.langchain4j.agent.tool.Tool

object CommandProviderWrapper {
  private val LOGGER = LogUtils.getLogger

  class GotoChatCommand {
    @Tool(Array("""execute baritone commands like `command arg1 arg2 argN` using supplied documentation"""))
    def baritoneRaw(command: String): Unit = {
      LOGGER.info("Executing command: " + command)
    }

    @Tool(Array("`thisway 1000` then `path` to go in the direction you're facing for a thousand blocks"))
    def baritoneThisWay(n: Int): Unit = {
      LOGGER.info("thisway")
      BaritoneAPI.getProvider.getPrimaryBaritone.getCommandManager
        .execute(s"thisway ${n}")
    }

    @Tool(
      Array("`Walk to `x z` to start moving the player to a specified position")
    )
    def baritoneWalk(x: Int, z: Int): Unit = {
      LOGGER.info(s"set goal: $x, $z")
      BaritoneAPI.getProvider.getPrimaryBaritone.getCustomGoalProcess
        .setGoalAndPath(new GoalXZ(x, z));
    }

    case class PlayerPosition(x: Double, z: Double)

    @Tool(Array("Get the position of the player"))
    def whereAmI(): PlayerPosition = {
      LOGGER.info("whereAmI")
      val player =
        BaritoneAPI.getProvider.getPrimaryBaritone.getPlayerContext.player()
      val x = player.lastRenderX
      val z = player.lastRenderZ
      PlayerPosition(x, z)
    }
  }

}
