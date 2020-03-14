package swagrid.test

import org.lwjgl.glfw.GLFW._
import swagrid.entity.{Entity, EntityCamera, EntityModel}
import swagrid.graphics.Camera
import swagrid.math.vector.{Mat, Transf3, Vec}
import swagrid.Window
import swagrid.world.World

object Test {

  def main(args: Array[String]): Unit = {

    var world = new World()
      .addEntity(new Entity(
        components = List(new EntityCamera()),
        transform = Transf3().position_=(Vec(0, 0, -2))
      )).addEntity(new Entity(
        components = List(new EntityModel(model = null))
      ))

    new Window(640, 480, "Hello, World!")
      .onRender{_ => world.render()}
      .onUpdate{e => world = world.update(e.dt)}
      .onFixedUpdate{e => world = world.fixedUpdate(e.dt)}
      .show()
  }
}