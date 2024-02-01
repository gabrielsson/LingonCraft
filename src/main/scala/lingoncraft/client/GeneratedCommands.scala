package lingoncraft.client

import baritone.api.BaritoneAPI
import dev.langchain4j.agent.tool.Tool


class GeneratedCommands {
  def executeCommand(command: String): Unit = {
    println("Executing command: " + command)
    BaritoneAPI.getProvider.getPrimaryBaritone.getCommandManager
      .execute(command)
  }

  @Tool(
    Array(
      """thisway 1000 then path to go in the direction you're facing for a thousand blocks"""
    )
  )
  def thisway(distance: Int): Unit = {
    executeCommand("thisway " + distance)
  }

  @Tool(
    Array(
      """Executes the 'path' command to begin pathfinding. After a goal is set."""
    )
  )
  def path(): Unit = {
    executeCommand("path")
  }

  @Tool(
    Array(
      """goal x y z or goal x z or goal y, then path to set a goal to a certain coordinate then path to it"""
    )
  )
  def goal(x: Int, y: Option[Int] = None, z: Int): Unit = {
    val coordString = y match {
      case Some(yValue) => s"$x $yValue $z"
      case None         => s"$x $z"
    }
    executeCommand("goal " + coordString)
  }

  @Tool(
    Array(
      """goto x y z or goto x z or goto y to go to a certain coordinate (in a single step, starts going immediately)"""
    )
  )
  def goto(x: Int, y: Option[Int] = None, z: Int): Unit = {
    val coordString = y match {
      case Some(yValue) => s"$x $yValue $z"
      case None         => s"$x $z"
    }
    executeCommand("goto " + coordString)
  }

  @Tool(Array("""goal to set the goal to your player's feet"""))
  def setGoalToPlayerFeet(): Unit = {
    executeCommand("goal")
  }

  @Tool(Array("""goal clear to clear the goal"""))
  def clearGoal(): Unit = {
    executeCommand("goal clear")
  }

  @Tool(
    Array(
      """cancel or stop to stop everything, forcecancel is also an option"""
    )
  )
  def stop(): Unit = {
    executeCommand("cancel")
  }

  @Tool(
    Array(
      """cancel or stop to stop everything, forcecancel is also an option"""
    )
  )
  def forceStop(): Unit = {
    executeCommand("forcecancel")
  }

  @Tool(
    Array(
      """click to click your destination on the screen. Right click path to on top of the block, left click to path into it (either at foot level or eye level), and left click and drag to select an area (#help sel to see what you can do with that selection)."""
    )
  )
  def click(): Unit = {
    executeCommand("click")
  }

  @Tool(
    Array(
      """follow player playerName to follow a player. follow players to follow any players in range (combine with Kill Aura for a fun time). follow entities to follow any entities. follow entity pig to follow entities of a specific type."""
    )
  )
  def follow(target: String): Unit = {
    executeCommand(s"follow $target")
  }

  @Tool(
    Array(
      """wp for waypoints. A "tag" is like "home" (created automatically on right clicking a bed) or "death" (created automatically on death) or "user" (has to be created manually). So you might want #wp save user coolbiome, then to set the goal #wp goal coolbiome then #path to path to it. For death, #wp goal death will list waypoints under the "death" tag (remember stuff is clickable!)"""
    )
  )
  def waypoint(
                action: String,
                tag: String,
                coordinate: Option[(Int, Int, Int)] = None
              ): Unit = {
    val coordinateString = coordinate match {
      case Some((x, y, z)) => s"$x $y $z"
      case None            => ""
    }
    executeCommand(s"#wp $action $tag $coordinateString")
  }

  @Tool(
    Array(
      """build to build a schematic. build blah.schematic will load schematics/blah.schematic and build it with the origin being your player feet. build blah.schematic x y z to set the origin. Any of those can be relative to your player (~ 69 ~-420 would build at x=player x, y=69, z=player z-420)."""
    )
  )
  def build(
             schematic: String,
             origin: Option[(Int, Int, Int)] = None
           ): Unit = {
    val originString = origin match {
      case Some((x, y, z)) => s"$x $y $z"
      case None            => ""
    }
    executeCommand(s"build $schematic $originString")
  }

  @Tool(
    Array(
      """schematica to build the schematic that is currently open in schematica"""
    )
  )
  def schematica(): Unit = {
    executeCommand("schematica")
  }

  @Tool(
    Array(
      """tunnel to dig and make a tunnel, 1x2. It will only deviate from the straight line if necessary such as to avoid lava. For a dumber tunnel that is really just cleararea, you can tunnel 3 2 100, to clear an area 3 high, 2 wide, and 100 deep."""
    )
  )
  def tunnel(height: Int, width: Int, depth: Int): Unit = {
    executeCommand(s"tunnel $height $width $depth")
  }

  @Tool(
    Array(
      """farm to automatically harvest, replant, or bone meal crops. Use farm <range> or farm <range> <waypoint> to limit the max distance from the starting point or a waypoint."""
    )
  )
  def farm(range: Int, waypoint: Option[String] = None): Unit = {
    val rangeString = waypoint match {
      case Some(wp) => s"$range $wp"
      case None     => range.toString
    }
    executeCommand(s"farm $rangeString")
  }

  @Tool(
    Array(
      """axis to go to an axis or diagonal axis at y=120 (axisHeight is a configurable setting, defaults to 120)."""
    )
  )
  def axis(): Unit = {
    executeCommand("axis")
  }

  @Tool(
    Array(
      """explore x z to explore the world from the origin of x,z. Leave out x and z to default to player feet. This will continually path towards the closest chunk to the origin that it's never seen before. explorefilter filter.json with optional invert can be used to load in a list of chunks to load."""
    )
  )
  def explore(
               origin: Option[(Int, Int)] = None,
               filter: Option[String] = None,
               invert: Boolean = false
             ): Unit = {
    val (x, z) = origin.getOrElse((0, 0))
    val filterString = filter.map(f => s" $f").getOrElse("")
    val invertString = if (invert) " invert" else ""
    executeCommand(s"explore $x $z$filterString$invertString")
  }

  @Tool(
    Array(
      """invert to invert the current goal and path. This gets as far away from it as possible, instead of as close as possible. For example, do goal then invert to run as far as possible from where you're standing at the start."""
    )
  )
  def invert(): Unit = {
    executeCommand("invert")
  }

  @Tool(
    Array(
      """come tells Baritone to head towards your camera, useful when freecam doesn't move your player position."""
    )
  )
  def come(): Unit = {
    executeCommand("come")
  }

  @Tool(
    Array(
      """blacklist will stop baritone from going to the closest block so it won't attempt to get to it."""
    )
  )
  def blacklist(): Unit = {
    executeCommand("blacklist")
  }

  @Tool(
    Array(
      """eta to get information about the estimated time until the next segment and the goal, be aware that the ETA to your goal is really unprecise."""
    )
  )
  def eta(): Unit = {
    executeCommand("eta")
  }

  @Tool(
    Array(
      """proc to view miscellaneous information about the process currently controlling Baritone."""
    )
  )
  def proc(): Unit = {
    executeCommand("proc")
  }

  @Tool(Array("""repack to re-cache the chunks around you."""))
  def repack(): Unit = {
    executeCommand("repack")
  }

  @Tool(Array("""gc to call System.gc() which may free up some memory."""))
  def gc(): Unit = {
    executeCommand("gc")
  }

  @Tool(
    Array(
      """render to fix glitched chunk rendering without having to reload all of them."""
    )
  )
  def render(): Unit = {
    executeCommand("render")
  }

  @Tool(
    Array(
      """reloadall to reload Baritone's world cache or saveall to save Baritone's world cache."""
    )
  )
  def reloadOrSaveCache(action: String): Unit = {
    executeCommand(action)
  }

  @Tool(
    Array(
      """find to search through Baritone's cache and attempt to find the location of the block."""
    )
  )
  def find(): Unit = {
    executeCommand("find")
  }

  @Tool(
    Array(
      """surface or top to tell Baritone to head towards the closest surface-like area, this can be the surface or highest available air space."""
    )
  )
  def surfaceOrTop(): Unit = {
    executeCommand("surface")
  }

  @Tool(Array("""version to get the version of Baritone you're running."""))
  def version(): Unit = {
    executeCommand("version")
  }

  @Tool(Array("""damn daniel"""))
  def damnDaniel(): Unit = {
    executeCommand("damn daniel")
  }
}