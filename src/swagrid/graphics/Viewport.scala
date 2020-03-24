package swagrid.graphics

import swagrid.Window
import swagrid.math.vector.Mat

class Viewport(val window: Window)
              (val width: Int = window.width,
               val height: Int = window.height,
               val xOffset: Int = 0,
               val yOffset: Int = 0) {

  val minX = xOffset - width / 2
  val minY = yOffset - height / 2
  val maxX = xOffset + width / 2
  val maxY = yOffset + height / 2

  val aspectRatio = width.toFloat / height.toFloat

  val relativeXOffset = 2.0F * xOffset.toFloat / window.width.toFloat
  val relativeYOffset = 2.0F * yOffset.toFloat / window.height.toFloat

  val relativeWidth = width.toFloat / window.width.toFloat
  val relativeHeight = height.toFloat / window.height.toFloat

  val matrix = Mat.translate(relativeXOffset, relativeYOffset, 0.0F) *
      Mat.scale(relativeWidth, relativeHeight, 1.0F)
}