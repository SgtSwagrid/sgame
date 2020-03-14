package swagrid.entity

import swagrid.World
import swagrid.graphics.{Camera, Frame}
import swagrid.math.vector.Transf3

class EntityCamera(
    camera: Camera = Camera(),
    transform: Transf3 = Transf3()
) extends Component {

  override def render(
      world: World, entity: Entity, frame: Frame): Frame =
    frame.camera_=(camera.at(entity.transform * transform))
}