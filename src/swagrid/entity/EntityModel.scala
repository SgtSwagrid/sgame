package swagrid.entity

import swagrid.graphics.{Frame, Model}
import swagrid.math.vector.Transf3
import swagrid.world.World

class EntityModel(
    model: Model,
    transform: Transf3 = Transf3()
) extends Component {

  override def init(world: World, entity: Entity): World =

    world.event{_.onRender{_.add(entity) {(frame, _) =>
      frame.addModel(model)
    }}}
}