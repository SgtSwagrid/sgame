package swagrid.graphics

import org.lwjgl.BufferUtils._
import swagrid.math.vector.{Transf3, Vec}

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

    val vboIds = List((0, 3), (1, 2), (2, 3))
      .foldLeft(List[Int]()) {(ids, vbo) =>

        val vboId = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboId)

        val buffer = createFloatBuffer(vbo._2 * vertices.size)
        buffer.put(vertices.flatMap(v => Array(v.x, v.y, v.z)).toArray)
        buffer.flip()

        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
        glVertexAttribPointer(vbo._1, vbo._2, GL_FLOAT, false, 0, 0)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
        ids :+ vboId
    }
    glBindVertexArray(0)
    new Mesh(vaoId, indices.size, vboIds)
  }

  def fromObj(name: String, transform: Transf3 = Transf3()): Mesh = {

    val lines = Source.fromResource(name).getLines.toList

    val vertices = lines.filter(_.startsWith("v"))
      .foldLeft(Vector[Vec]()) {(v, s) => v :+
        Vec(3) {d => s.split(" ")(d+1).toFloat}}

    val textures = lines.filter(_.startsWith("vt"))
      .foldLeft(Vector[Vec]()) {(t, s) => t :+
        Vec(2) {d => s.split(" ")(d+1).toFloat}}

    val normals = lines.filter(_.startsWith("vn"))
      .foldLeft(Vector[Vec]()) {(n, s) => n :+
        Vec(3) {d => s.split(" ")(d+1).toFloat}}

    val faces = lines.filter(_.startsWith("f"))
      .foldLeft(Vector[Seq[Int]]()) {(f, s) =>
        s.split(" ").drop(1).foldLeft(f) {(p, s) => p :+
          Vector.tabulate(3) {i => s.split("/")(i).toInt - 1}}}

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
    fromPoints(v.map(transform * _), t, n, indices)
  }
}