package swagrid.test

import swagrid.Window
import swagrid.entity.{Entity, EntityCamera, EntityModel}
import swagrid.graphics.{Mesh, Model}
import swagrid.math.vector.{Mat, Transf3, Vec}
import swagrid.world.World

object Test {

  def main(args: Array[String]): Unit = {

    var world = new World()
      .addEntity(new Entity(
        transform = Transf3(Mat.translate(0, 0, -5)),
        components = List(new EntityCamera())))
      .addEntity(new Entity(
        transform = Transf3(),
        components = List(new EntityModel(
          model = Model(mesh = Mesh.fromObj("/res/mesh/cube.obj"))))))

    new Window(640, 480, "Test")
      .onUpdate{e =>
        world = world.update(e.dt)
      }.show()
  }
}