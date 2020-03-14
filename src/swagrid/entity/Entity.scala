package swagrid.entity

import swagrid.math.vector.Transf3
import swagrid.world.World

class Entity(
    val transform: Transf3 = Transf3(),
    val components: Seq[Component] = List()
) {

  def init(world: World): World = world

  def transform(t: Transf3 => Transf3): Entity =
    transform_=(t(transform))

  def transform_=(transform: Transf3): Entity =
    copy(transform = transform)

  def copy(
      transform: Transf3 = transform,
      components: Seq[Component] = components): Entity =
    new Entity(transform, components)
}