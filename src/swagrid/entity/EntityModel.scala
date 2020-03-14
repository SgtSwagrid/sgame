package swagrid.entity

import swagrid.World
import swagrid.graphics.{Frame, Model}
import swagrid.math.vector.Transf3

class EntityModel(
    model: Model,
    transform: Transf3 = Transf3()
) extends Component {

  override def render(
      world: World, entity: Entity, frame: Frame): Frame =
    frame.addModel(model.at(entity.transform * transform))
}