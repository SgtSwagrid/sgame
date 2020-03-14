package swagrid.entity

import swagrid.World
import swagrid.graphics.Model
import swagrid.math.vector.Mat

class EntityModel(model: Model) extends Component {

  //override def transform(): Mat = entity.transform

  override protected def init(world: World, entity: Entity): World =
    world.addModel(model)

  override protected def update(world: World, entity: Entity, dt: Long): World =
    world.updateEntity

  override protected def fixedUpdate(world: World, entity: Entity, dt: Long): World =
    world

  override protected def destroy(world: World, entity: Entity): World =
    world.removeModel(model)
}
