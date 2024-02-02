package lingoncraft.client.finders;

import baritone.api.BaritoneAPI;
import dev.langchain4j.agent.tool.Tool;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class PlayerFinder {
    private static final Logger log = LoggerFactory.getLogger(PlayerFinder.class);

    @Tool({"Get the block position of the player"})
    public BlockPos whereAmI() {
        log.info("whereAmI");
        return new BlockPos(BaritoneAPI.getProvider().getPrimaryBaritone().getPlayerContext().playerFeet());
    }
}