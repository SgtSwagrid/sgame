package swagrid.graphics

import swagrid.math.vector.{Mat, Transf3}

case class Camera(
    fov: Float = 90.0F,
    aspectRatio: Float = 1.5F,
    nearClip: Float = 0.3F,
    farClip: Float = 1000.0F,
    transform: Transf3 = Transf3()
) {

  def projection(): Mat =
    Mat.projection(fov, aspectRatio, nearClip, farClip)

  def view(): Mat =
    transform.matrix.invert

  def at(transform: Transf3): Camera =
    copy(transform = transform)
}