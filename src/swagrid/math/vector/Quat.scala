package swagrid.math.vector

import java.nio.FloatBuffer

import org.lwjgl.BufferUtils

import scala.math._

class Quat(private val a: Float*) {

  def +(q: Quat): Quat =
    Quat {d => a(d) + q(d)}

  def -(q: Quat): Quat =
    this + -q

  def *(s: Float): Quat =
    Quat {a(_) * s}

  def /(s: Float): Quat =
    this * (1.0F / s)

  def unary_-(): Quat =
    this * -1.0F

  def norm(): Float =
    sqrt(a.map{c => c * c}.sum).toFloat

  def normalize(): Quat =
    this / norm

  def dot(q: Quat): Float =
    a.zip(q.a).map{case(s, t) => s * t}.sum

  def *(q: Quat): Quat = {

    val (s1, s2) = (scalar, q.scalar)
    val (v1, v2) = (vector, q.vector)

    Quat(s1 * s2 + (v1 dot v2),
      (v2 * s1) + (v1 * s2) + (v1 cross v2))
  }

  def *(v: Vec): Vec =
    (this * Quat(v)).vector

  def rotate(v: Vec): Vec =
    (this * Quat(v) * invert).vector

  def conjugate(): Quat =
    Quat(scalar, -vector)

  def invert(): Quat =
    conjugate / norm

  def slerp(q: Quat, t: Float): Quat = {

    val (qn, dot) = {
      val qn = q.normalize
      val dot = normalize dot qn
      if (dot >= 0.0F) (qn, dot)
      else (-qn, -dot)
    }

    val a = acos(dot).toFloat
    val s1 = (sin(a * t) / sin(a)).toFloat
    val s0 = cos(a * t).toFloat - dot * s1

    ((this * s0) + (qn * s1)).normalize
  }

  def toMatrix(): Mat = {

    val r = normalize
    val (a, b, c, d) = (r(0), r(1), r(2), r(3))

    Mat.rows(
      Vec(a*a + b*b - c*c - d*d, 2*b*c - 2*a*d, 2*b*d + 2*a*c, 0.0F),
      Vec(2*b*c + 2*a*d, a*a - b*b + c*c - d*d, 2*c*d - 2*a*b, 0.0F),
      Vec(2*b*d - 2*a*c, 2*c*d + 2*a*b, a*a - b*b - c*c + d*d, 0.0F),
      Vec(0.0F, 0.0F, 0.0F, 1.0F)
    )
  }

  def angle(): Float =
    2.0F * atan2(vector.length, scalar).toFloat

  def axis(): Vec =
    vector.normalize

  def scalar(): Float = a(0)

  def vector(): Vec =
    Vec(3){d => a(d+1)}

  def toFloatBuffer(): FloatBuffer = {

    val buffer = BufferUtils.createFloatBuffer(4)
    a.foreach(buffer.put)
    buffer.flip()
    buffer
  }

  def apply(d: Int) = a(d)

  override def toString(): String =
    a.mkString("Quat(", ", ", ")")
}

object Quat {

  def apply(a: Float*): Quat = new Quat(a :_*)

  def apply(generator: Int => Float): Quat =
    Quat(Vector.tabulate(4)(generator) :_*)

  def apply(s: Float, v: Vec): Quat =
    Quat {d => if (d==0) s else v(d-1)}

  def apply(v: Vec): Quat =
    Quat(0.0F, v)

  def identity(): Quat =
    Quat(1.0F, Vec.zero(3))

  def angleAxis(angle: Float, axis: Vec) =
    Quat(cos(angle / 2.0F).toFloat,
      axis.length(sin(angle / 2.0F).toFloat))

  def angleAxis(angle: Float, axis: Int) =
    Quat(angle, Vec.axis(axis, 3))

  def euler(yaw: Float, pitch: Float, roll: Float): Quat =
    angleAxis(yaw, 1) * angleAxis(-pitch, 0) * angleAxis(roll, 2)

  def rotate2(angle: Float): Mat = {
    val (c, s) = (cos(angle).toFloat, sin(angle).toFloat)
    Mat.rows(Vec(c, -s, 0.0F), Vec(s, c, 0.0F), Vec(0.0F, 0.0F, 1.0F))
  }

  def rotate2About(angle: Float, origin: Vec): Mat =
    Mat.translate(origin) * rotate2(angle) * Mat.translate(-origin)

  def rotate3About(rot: Quat, origin: Vec): Mat =
    Mat.translate(origin) * rot.toMatrix * Mat.translate(-origin)

  def rotationMatrixOf(m: Mat): Mat = {
    val s = Mat.scale(Vec.scaleOf(m)).invert
    val t = Mat.translate(-Vec.translationOf(m))
    s * t * m
  }

  def angle2Of(m: Mat): Float = {
    val r = rotationMatrixOf(m)
    atan2(r(1, 0), r(0, 0)).toFloat
  }

  def rotation3Of(m: Mat): Quat = {

    val r = rotationMatrixOf(m)

    Quat(
      sqrt(1.0F + r(0, 0) + r(1, 1) + r(2, 2))
        .toFloat * 0.5F,
      sqrt(1.0F + r(0, 0) - r(1, 1) - r(2, 2))
        .toFloat * 0.5F * signum(r(2, 1) - r(1, 2)),
      sqrt(1.0F - r(0, 0) + r(1, 1) - r(2, 2))
        .toFloat * 0.5F * signum(r(0, 2) - r(2, 0)),
      sqrt(1.0F - r(0, 0) - r(1, 1) + r(2, 2))
        .toFloat * 0.5F * signum(r(1, 0) - r(0, 1))
    ).normalize
  }
}