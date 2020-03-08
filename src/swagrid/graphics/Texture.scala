package swagrid.graphics

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL30._

import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import javax.imageio.ImageIO
import org.lwjgl.BufferUtils

class Texture(fileName: String, _opaque: Boolean = false) {

  var _textureId = -1

  def textureId(): Int = {

    if (textureId == -1) {
      val (image, buffer) = loadPng(fileName)
      _textureId = createTexture(image, buffer)
    }
    _textureId
  }

  def opaque(): Boolean = _opaque

  private def loadPng(fileName: String): (BufferedImage, ByteBuffer) = {

    val stream = getClass().getClassLoader().getResourceAsStream(fileName)
    val image = ImageIO.read(stream)
    val pixels = Array.ofDim[Int](image.getWidth * image.getHeight)
    image.getRGB(0, 0, image.getWidth, image.getHeight, pixels, 0, image.getWidth)
    val buffer = BufferUtils.createByteBuffer(image.getWidth * image.getHeight * 4)

    for (y <- 0 until image.getHeight) {
      for (x <- 0 until image.getWidth) {
        val pixel = pixels(x + y * image.getWidth)
        buffer.put(((pixel >> 16) & 0xFF).toByte)
        buffer.put(((pixel >> 8) & 0xFF).toByte)
        buffer.put((pixel & 0xFF).toByte)
        buffer.put((pixel >> 24).toByte)
      }
    }
    buffer.flip()
    (image, buffer)
  }

  private def createTexture(image: BufferedImage, buffer: ByteBuffer): Int = {

    val textureId = glGenTextures()
    glBindTexture(GL_TEXTURE_2D, textureId)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_NEAREST)
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth,
        image.getHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer)
    glGenerateMipmap(GL_TEXTURE_2D)
    textureId
  }
}