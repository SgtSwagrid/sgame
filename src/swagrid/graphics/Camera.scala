package swagrid.graphics

import swagrid.math.vector.Mat

abstract class Camera {

  val fov = 90.0F
  val aspectRatio = 1.5F
  val nearClip = 0.3F
  val farClip = 1000.0F

  def projection(): Mat = Mat.projection(fov, aspectRatio, nearClip, farClip)
  abstract def transform(): Mat
  def view(): Mat = transform.invert
}
