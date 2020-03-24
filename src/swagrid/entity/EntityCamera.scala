package swagrid.entity

import swagrid.graphics.Camera
import swagrid.math.vector.Transf3
import swagrid.world.World

class EntityCamera(
    camera: Camera,
    transform: Transf3 = Transf3()
) extends Component {

  override def init(world: World, entity: Entity): World =

    world.event{_.onRender{_.add(entity) {(frame, _) =>
      frame.camera_=(camera.at(entity.transform * transform))
    }}}
}