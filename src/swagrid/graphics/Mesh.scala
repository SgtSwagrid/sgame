package swagrid.graphics

import swagrid.math.vector.Vec

import scala.io.Source

class Mesh {

  val vaoId = 0
  val numVertices = 0

}

object Mesh {

  def fromObj(name: String): Mesh = {

    val lines = Source.fromResource(name).getLines.toList

    val vertices = getVertices(lines)
    val textures = getTextures(lines)
    val normals = getNormals(lines)
    val pointIndices = getPoints(lines)

    val points = List[Vec, Vec, Vec]()
    val indices = Map[(Int, Int, Int), Int]()

    pointIndices.foldLeft((points, indices)) {(i, p) =>
      if (i._2.contains(p)) (i._1, i._2 :+ in)}



  }

  private def getVertices(lines: Seq[String]): List[Vec] =
    lines.filter(_.startsWith("v"))
      .foldLeft(List[Vec]()) {(v, s) => v :+
        Vec(3) {d => s.split(" ")(d+1).toFloat}}

  private def getTextures(lines: Seq[String]): List[Vec] =
    lines.filter(_.startsWith("t"))
      .foldLeft(List[Vec]()) {(t, s) => t :+
        Vec(2) {d => s.split(" ")(d+1).toFloat}}

  private def getNormals(lines: Seq[String]): List[Vec] =
    lines.filter(_.startsWith("n"))
      .foldLeft(List[Vec]()) {(n, s) => n :+
        Vec(3) {d => s.split(" ")(d+1).toFloat}}

  private def getPoints(lines: Seq[String]): List[List[Int]] =
    lines.filter(_.startsWith("f"))
      .foldLeft(List[List[Int]]()) {(f, s) =>
        s.split(" ").drop(1).foldLeft(f) {(p, s) => p :+
          List.tabulate(3) {i => s.split("/")(i)}}}
}