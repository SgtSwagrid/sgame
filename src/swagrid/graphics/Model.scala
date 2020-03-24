package swagrid.graphics

import swagrid.math.vector.Transf3

case class Model(
  mesh: Mesh = null,
  texture: Option[Texture] = None,
  material: Material = null,
  transform: Transf3 = Transf3()
) {
  def at(transform: Transf3): Model =
    copy(transform = transform)
}