package swagrid.graphics

import swagrid.math.vector.{Mat, Transf3}

case class Model(
    mesh: Mesh = null,
    texture: Texture = null,
    material: Material = null,
    transform: Transf3 = Transf3()
)