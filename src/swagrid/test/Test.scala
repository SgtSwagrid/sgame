package swagrid.test

import swagrid.Window
import swagrid.entity.{Entity, EntityCamera, EntityModel}
import swagrid.graphics.{Camera, Mesh, Model}
import swagrid.math.vector.{Mat, Quat, Transf3, Vec}
import swagrid.world.World

import scala.math._

object Test {

  def main(args: Array[String]): Unit = {

    var world = new World()
      .addEntity(new Entity(
        transform = Transf3(Mat.translate(0, 0, 0)),
        components = List(new EntityCamera(new Camera()))))
      .addEntity(new Entity(
        transform = Transf3(Mat.translate(0.01F, 0, -5.0F) * Quat.angleAxis(Pi.toFloat / 4, Vec.axis(1, 3)).toMatrix),
        components = List(new EntityModel(
          model = Model(mesh = Mesh.fromObj("res/mesh/cube.obj"))))))

    new Window(640, 480, "Test")
      .event{
        _.onRender(_.add{e => world.render(e)})
         .onUpdate{_.add{e => world = world.update(e)}}
         .onFixedUpdate{_.add{e => world = world.fixedUpdate(e)}}
      }.show()
  }
}