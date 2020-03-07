package math.vector

import scala.math._

class Mat(private val a: Seq[Float]*) {

  def +(m: Mat): Mat =
    Mat(height, width) {(r, c) => a(r)(c) + m(r, c)}

  def -(m: Mat): Mat =
    this + m.negate

  def *(m: Mat): Mat =
    Mat(height, m.width) {row(_) dot m.col(_)}

  def *(v: Vec): Vec =
    Vec(height) {v dot row(_)}

  def *(s: Float): Mat =
    Mat(height, width) {a(_)(_) * s}

  def /(s: Float): Mat =
    this * (1.0F / s)

  def negate(): Mat =
    this * -1.0F

  def power(i: Int): Mat = i match {
    case _ if i > 1 => this * power(i-1)
    case _ if i < 0 => invert.power(-i)
    case 1 => this
    case 0 => Mat.identity(height)
  }

  def transpose(): Mat =
    Mat(width, height) {(r, c) => a(c)(r)}

  def invert(): Mat = adjugate / determinant

  def determinant(): Float =
    if (height == 1) a(0)(0)
    else (0 until width).map{c => a(0)(c) * cofactor(0, c)}.sum

  def adjugate(): Mat =
    cofactor.transpose

  def cofactor(): Mat =
    Mat(height, width) {cofactor(_, _)}

  def cofactor(row: Int, col: Int): Float =
    minor(row, col) * (if (row + col % 2 == 0) 1.0F else -1.0F)

  def minor(): Mat =
    Mat(height, width) {minor(_, _)}

  def minor(row: Int, col: Int): Float =
    submatrix(row, col).determinant

  def submatrix(row: Int, col: Int): Mat =
    Mat(height-1, width-1) {(r, c) =>
      a(r + (r >= row))(c + (c >= col))}

  def trace(): Float =
    (0 until width).map{i => a(i)(i)}.sum

  def interpolate2(m: Mat, t: Float): Mat = {

    val (t1, t2) = (Vec.translationOf(this), Vec.translationOf(m))
    val (a1, a2) = (Quat.angle2Of(this), Quat.angle2Of(m))
    val (s1, s2) = (Vec.scaleOf(this), Vec.scaleOf(m))

    val angle = ((a2 - a1) - ((a2 - a1) > Pi) * 2.0F * Pi).toFloat
    Mat.transform2(t1.lerp(t2, t), angle * t, s1.lerp(s2, t))
  }

  def interpolate3(m: Mat, t: Float): Mat = {

    val (t1, t2) = (Vec.translationOf(this), Vec.translationOf(m))
    val (r1, r2) = (Quat.rotation3Of(this), Quat.rotation3Of(m))
    val (s1, s2) = (Vec.scaleOf(this), Vec.scaleOf(m))

    Mat.transform3(t1.lerp(t2, t), r1.slerp(r2, t), s1.lerp(s2, t))
  }

  def row(r: Int):
    Vec = Vec(a(r) :_*)

  def col(c: Int): Vec =
    Vec(height) {d => a(c)(d)}

  def height(): Int = a.size

  def width(): Int = a(0).size

  def apply(r: Int, c: Int): Float = a(r)(c)

  override def toString(): String =
    a.map(_.mkString("(", ", ", ")")).mkString("Mat(", ", ", ")")

  private implicit def boolToInt(b: Boolean): Int =
    if (b) 1 else 0
}

object Mat {

  def apply(a: Seq[Float]*): Mat = new Mat(a :_*)

  def apply(height: Int, width: Int)(generator: (Int, Int) => Float): Mat =
    Mat(Vector.tabulate(height, width)(generator) :_*)

  def identity(size: Int): Mat =
    Mat(size, size) {(r, c) => if (r == c) 1.0F else 0.0F}

  def zero(height: Int, width: Int): Mat =
    Mat(height, width) {(_, _) => 0.0F}

  def rows(rows: Vec*): Mat =
    Mat(rows.size, rows(0).dims) {(r, c) => rows(r)(c)}

  def cols(cols: Vec*): Mat =
    Mat(cols(0).dims, cols.size) {(r, c) => cols(c)(r)}

  def translate(transl: Vec): Mat =

    Mat(transl.dims+1, transl.dims+1) {(r, c) =>
      if (r == c) 1.0F
      else if (c == transl.dims) transl(r)
      else 0.0F
    }

  def scale(scale: Vec): Mat =

    Mat(scale.dims+1, scale.dims+1) {(r, c) =>
      if (r == c && c == scale.dims) 1.0F
      else if (r == c) scale(r)
      else 0.0F
    }

  def transform2(transl: Vec, angle: Float, scale: Vec): Mat =
    translate(transl) * Quat.rotate2(angle) * scale(scale)

  def transform2(transl: Vec, angle: Float): Mat =
    translate(transl) * Quat.rotate2(angle)

  def transform3(transl: Vec, rot: Quat, scale: Vec): Mat =
    translate(transl) * rot.toMatrix * scale(scale)

  def transform3(transl: Vec, rot: Quat): Mat =
    translate(transl) * rot.toMatrix

  def projection(fov: Float, aspect: Float, near: Float, far: Float): Mat = {

    val width = 1.0F / tan(fov / 2.0F).toFloat
    val flen = far - near

    Mat.rows(
      Vec(width, 0.0F, 0.0F, 0.0F),
      Vec(0.0F, width/aspect, 0.0F, 0.0F),
      Vec(0.0F, 0.0F, -(near+far)/flen, -2*near*far/flen),
      Vec(0.0F, 0.0F, -1.0F, 0.0F)
    )
  }
}