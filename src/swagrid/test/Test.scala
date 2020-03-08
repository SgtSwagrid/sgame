package swagrid.test

import org.lwjgl.glfw.GLFW._
import swagrid.{Window, World}

object Test {

  def main(args: Array[String]): Unit = {

    var world = new World()

    new Window(640, 480, "Hello, World!")
      .onUpdate{e =>
        if (e.dt != 0) e.window.title = s"${1000 / e.dt} FPS"
        world.render()
      }.onClick{e =>
        if (e.action == GLFW_PRESS) println(s"Clicked at (${e.cursorX}, ${e.cursorY})!")
      }.show()
  }
}