package swagrid.graphics

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import swagrid.entity.Entity

class EntityShader extends Shader("vertex.glsl", "fragment.glsl") {

  override protected def onBind(): Unit = {
    glBindAttribLocation(shaderId, 0, "vertex")
    glBindAttribLocation(shaderId, 1, "texmap")
    glBindAttribLocation(shaderId, 2, "normal")
  }

  override protected def onInit(): Unit = {}

  def loadMesh(mesh: Mesh): Unit = {
    glBindVertexArray(0 /*mesh vaoId*/)
    for (i <- 0 to 2) glEnableVertexAttribArray(i)
  }

  def unloadMesh(): Unit = {
    for (i <- 0 to 2) glDisableVertexAttribArray(i)
    glBindVertexArray(0)
  }

  def loadTexture(texture: Texture): Unit = {
    glActiveTexture(GL_TEXTURE0)
    if (texture.opaque) glEnable(GL_CULL_FACE)
    else glDisable(GL_CULL_FACE)
    glBindTexture(GL_TEXTURE_2D, texture.textureId)
  }

  def loadModel(model: Model): Unit = {

  }

  def renderEntity(entity: Entity): Unit = {

  }
}