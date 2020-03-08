package swagrid.graphics

class Viewport(val xMin: Int, val yMin: Int, val xMax: Int, val yMax: Int) {

  def width(): Int = xMax - xMin
  def height(): Int = yMax - yMin

  def xCentre(): Int = (xMin + xMax) / 2
  def yCentre(): Int = (yMin + yMax) / 2
}