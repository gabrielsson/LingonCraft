package lingoncraft.client

import baritone.api.BaritoneAPI
import baritone.api.pathing.goals.{GoalBlock, GoalXZ}
import com.mojang.logging.LogUtils
import dev.langchain4j.agent.tool.Tool
<<<<<<< Updated upstream
case class EntityPosition(x: Double, z: Double)
||||||| Stash base
=======
import net.minecraft.util.math.BlockPos
>>>>>>> Stashed changes

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
      Array("`Walk to `x z` to start moving the player to a specified position without caring about the vertical position")
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


    @Tool(
      Array("`Walk to the surface")
    )
    def baritoneSurface(): Unit = {
      LOGGER.info(s"surface")
      BaritoneAPI.getProvider.getPrimaryBaritone.getCommandManager.execute("surface");
    }

  }

}
