package swagrid.graphics

import swagrid.math.vector.Vec
import scala.io.Source
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._

class Mesh(val vaoId: Int, val numVertices: Int, vboIds: Seq[Int]) {

  def delete(): Unit = {
    glDeleteVertexArrays(vaoId)
    vboIds.foreach(glDeleteBuffers(_))
  }
}

object Mesh {

  def fromPoints(vertices: Seq[Vec], textures: Seq[Vec],
      normals: Seq[Vec], indices: Seq[Int]): Mesh = {

    val vaoId = glGenVertexArrays()
    glBindVertexArray(vaoId)

    glBindBuffer(GL_);
  }

  def fromObj(name: String): Mesh = {

    val lines = Source.fromResource(name).getLines.toList

    val vertices = lines.filter(_.startsWith("v"))
      .foldLeft(Vector[Vec]()) {(v, s) => v :+
        Vec(3) {d => s.split(" ")(d+1).toFloat}}

    val textures = lines.filter(_.startsWith("t"))
      .foldLeft(Vector[Vec]()) {(t, s) => t :+
        Vec(2) {d => s.split(" ")(d+1).toFloat}}

    val normals = lines.filter(_.startsWith("n"))
      .foldLeft(Vector[Vec]()) {(n, s) => n :+
        Vec(3) {d => s.split(" ")(d+1).toFloat}}

    val faces = lines.filter(_.startsWith("f"))
      .foldLeft(Vector[Seq[Int]]()) {(f, s) =>
        s.split(" ").drop(1).foldLeft(f) {(p, s) => p :+
          Vector.tabulate(3) {i => s.split("/")(i).toInt}}}

    val (points, indices, _) = faces.foldLeft(
      List[(Vec, Vec, Vec)](),
      List[Int](),
      Map[(Int, Int, Int), Int]()
    ) {(a, p) =>
      if (a._3.contains(p(0), p(1), p(2)))
        (a._1, a._2 :+ a._3(p(0), p(1), p(2)), a._3)
      else
        (a._1 :+ (vertices(p(0)), textures(p(1)), normals(p(2))),
          a._2 :+ a._1.size,
          a._3 + ((p(0), p(1), p(2)) -> a._1.size))
    }

    val (v, t, n) = points.unzip3(p => (p._1, p._2, p._3))
    fromPoints(v, t, n, indices)
  }
}