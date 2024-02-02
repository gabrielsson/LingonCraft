package lingoncraft.client

import baritone.api.BaritoneAPI
import baritone.api.pathing.goals.GoalXZ
import com.mojang.logging.LogUtils
import dev.langchain4j.agent.tool.Tool
case class EntityPosition(x: Double, z: Double)

object CommandProviderWrapper {
  private val LOGGER = LogUtils.getLogger

  class GotoChatCommand {
    @Tool(Array("""execute baritone commands like `command arg1 arg2 argN` using supplied documentation"""))
    def baritoneRaw(command: String): Boolean = {
      LOGGER.warn("Raw command: " + command)
      BaritoneAPI.getProvider.getPrimaryBaritone.getCommandManager
        .execute(command)
    }

    @Tool(Array("`thisway 1000` then `path` to go in the direction you're facing for a thousand blocks"))
    def baritoneThisWay(n: Int): Boolean = {
      LOGGER.info("thisway")
      BaritoneAPI.getProvider.getPrimaryBaritone.getCommandManager
        .execute(s"thisway ${n}")
    }

    @Tool(
      Array("`Walk to `x z` to start moving the player to a specified position")
    )
    def baritoneWalk(x: Double, z: Double): Unit = {
      LOGGER.info(s"set goal: $x, $z")
      BaritoneAPI.getProvider.getPrimaryBaritone.getCustomGoalProcess
        .setGoalAndPath(new GoalXZ(x.toInt, z.toInt));
    }


    @Tool(Array("Get the position of  the player"))
    def whereAmI(): EntityPosition = {
      LOGGER.info("whereAmI")
      val player =
        BaritoneAPI.getProvider.getPrimaryBaritone.getPlayerContext.player()
      val x = player.lastRenderX
      val z = player.lastRenderZ
      EntityPosition(x, z)
    }
  }

}
