package swagrid.math.vector

class Transf3(val position: Vec, val orientation: Quat, val scale: Vec) {

  def matrix(): Mat =
    Mat.transform3(position, orientation, scale)

  def transformAbs(m: Mat): Transf3 = {
    val t: Mat = m * matrix
    Transf3(Vec.translationOf(t), Quat.rotation3Of(t), Vec.scaleOf(t))
  }

  def transformRel(m: Mat): Transf3 = {
    val t: Mat = matrix * m
    Transf3(Vec.translationOf(t), Quat.rotation3Of(t), Vec.scaleOf(t))
  }

  def *(t: Transf3): Transf3 =
    transformRel(t.matrix)

  def *(v: Vec): Vec =
    matrix * v

  def invert(): Transf3 =
    Transf3(matrix.invert)

  def position_=(p: Vec): Transf3 =
    Transf3(p, orientation, scale)

  def translateAbs(t: Vec): Transf3 =
    Transf3(position + t, orientation, scale)

  def translateRel(t: Vec): Transf3 =
    Transf3(position + orientation.rotate(t), orientation, scale)

  def orientation_=(o: Quat): Transf3 =
    Transf3(position, o, scale)

  def rotateAbs(r: Quat): Transf3 =
    Transf3(position, r * orientation, scale)

  def rotateRel(r: Quat): Transf3 =
    Transf3(position, orientation * r, scale)

  def scale_=(s: Vec): Transf3 =
    Transf3(position, orientation, s)

  override def toString(): String =
    matrix.toString
}

object Transf3 {

  def apply(position: Vec = Vec.zero(3),
            orientation: Quat = Quat.identity(),
            scale: Vec = Vec.one(3)): Transf3 =
    new Transf3(position, orientation, scale)

  def apply(m: Mat): Transf3 =
    new Transf3(Vec.translationOf(m), Quat.rotation3Of(m), Vec.scaleOf(m))
}