package swagrid.math.vector

import java.nio.FloatBuffer

import org.lwjgl.BufferUtils

import scala.math._

class Vec(private val a: Float*) {

  def +(v: Vec): Vec =
    Vec(dims) {d => a(d) + v(d)}

  def -(v: Vec): Vec =
    this + -v

  def *(s: Float): Vec =
    Vec(dims) {a(_) * s}

  def /(s: Float): Vec =
    this * (1.0F / s)

  def unary_-(): Vec =
    this * -1.0F

  def lengthSquared(): Float =
    a.map{c => c * c}.sum

  def length(): Float =
    sqrt(lengthSquared).toFloat

  def length(len: Float): Vec =
    normalize * len

  def normalize(): Vec =
    this / length

  def dot(v: Vec): Float =
    a.zip(v.a).map{case(s, t) => s * t}.sum

  def cross(v: Vec): Vec =
    Vec(y * v.z - z * v.y, z * v.x - x * v.z,
      x * v.y - y * v.x)

  def outer(v: Vec): Mat =
    Mat(dims, v.dims) {(r, c) => a(r) * v(c)}

  def triple(u: Vec, v: Vec): Float =
    this dot (u cross v)

  def angle(v: Vec): Float =
    acos(normalize dot v.normalize).toFloat

  def distance(v: Vec): Float =
    (v - this).length

  def project(v: Vec): Vec =
    normalize * (this dot v.normalize)

  def reflect(v: Vec): Vec =
    (this project v) * 2.0F - this

  def lerp(v: Vec, s: Float): Vec =
    this * (1.0F - s) + (v * s)

  def midpoint(v: Vec): Vec =
    lerp(v, 0.5F)

  def append(v: Vec): Vec =
    Vec(a ++ v.a :_*)

  def toHomogenous(): Vec =
    Vec(a :+ 1.0F :_*)

  def toCartesian(): Vec =
    Vec(dims-1) {a(_) / a(dims-1)}

  def toBasis(m: Mat): Vec =
    m.invert * this

  def toFloatBuffer(): FloatBuffer = {

    val buffer = BufferUtils.createFloatBuffer(dims)
    a.foreach(buffer.put)
    buffer.flip()
    buffer
  }

  def toSeq(): Seq[Float] = a

  def dims(): Int = a.size

  def apply(d: Int): Float = a(d)

  def x(): Float = a(0)
  def y(): Float = a(1)
  def z(): Float = a(2)
  def w(): Float = a(3)

  override def toString(): String =
    a.mkString("Vec(", ", ", ")")
}

object Vec {

  def apply(a: Float*): Vec = new Vec(a :_*)

  def apply(dims: Int)(generator: Int => Float): Vec =
    Vec(Vector.tabulate(dims)(generator) :_*)

  def repeat(s: Float, dims: Int): Vec =
    Vec(dims) {_ => s}

  def zero(dims: Int): Vec =
    Vec.repeat(0.0F, dims)

  def one(dims: Int): Vec =
    Vec.repeat(1.0F, dims)

  def axis(axis: Int, dims: Int): Vec =
    Vec(dims) {d => if (d == axis) 1.0F else 0.0F}

  def translationOf(m: Mat): Vec =
    m.col(m.width-1).toCartesian

  def scaleOf(m: Mat): Vec =
    Vec(m.height-1) {d => m.col(d).length}
}