package lingoncraft.client.walker;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalNear;
import dev.langchain4j.agent.tool.Tool;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerWalker {
    private static final Logger log = LoggerFactory.getLogger(lingoncraft.client.finders.PlayerFinder.class);

    @Tool({"Walk to a specified block position. Use this when finding a certain material or block."})
    public void walkToBlockPos(int x, int y, int z) {
        var blockPos = new BlockPos(x, y, z);
        log.info("walkToBlockPos: %s".formatted(blockPos));
        BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalNear(blockPos, 2));
    }
}
