package lingoncraft.client;

import baritone.api.BaritoneAPI;
import dev.langchain4j.agent.tool.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stopper {
    private static final Logger log = LoggerFactory.getLogger(lingoncraft.client.finders.PlayerFinder.class);

    @Tool({"Stop walking or mining or doing whatever."})
    public void stopIt() {
        BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
    }
}
