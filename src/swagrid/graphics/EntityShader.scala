package swagrid.graphics

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import swagrid.entity.Entity
import swagrid.math.vector.Mat

class EntityShader extends Shader("vertex.glsl", "fragment.glsl") {

  private val FOV = 90.0F
  private val ASPECT_RATIO = 1.5F
  private val NEAR_CLIP = 0.3F
  private val FAR_CLIP = 1000.0F

  private val transform = glGetUniformLocation(shaderId, "transform")
  private val view = glGetUniformLocation(shaderId, "view")
  private val projection = glGetUniformLocation(shaderId, "projection")
  private val viewport = glGetUniformLocation(shaderId, "viewport")

  private var mesh: Mesh = null

  override protected def onBind(): Unit = {
    glBindAttribLocation(shaderId, 0, "vertex")
    glBindAttribLocation(shaderId, 1, "texmap")
    glBindAttribLocation(shaderId, 2, "normal")
  }

  override protected def onInit(): Unit = {
    glUniformMatrix4fv(projection, true, Mat.projection(
        FOV, ASPECT_RATIO, NEAR_CLIP, FAR_CLIP).toFloatBuffer)
  }

  def loadMesh(mesh: Mesh): Unit = {
    glBindVertexArray(0 /*mesh vaoId*/)
    for (i <- 0 to 2) glEnableVertexAttribArray(i)
    this.mesh = mesh
  }

  def unloadMesh(): Unit = {
    this.mesh = null
    for (i <- 0 to 2) glDisableVertexAttribArray(i)
    glBindVertexArray(0)
  }

  def loadTexture(texture: Texture): Unit = {
    glActiveTexture(GL_TEXTURE0)
    if (texture.opaque) glEnable(GL_CULL_FACE)
    else glDisable(GL_CULL_FACE)
    glBindTexture(GL_TEXTURE_2D, texture.textureId)
  }

  def loadMaterial(material: Material): Unit = {

  }

  def loadCamera(camera: Camera): Unit = {
    glUniformMatrix4fv(view, true, camera.view.toFloatBuffer)
    glUniformMatrix4fv(projection, true, camera.projection.toFloatBuffer)
  }

  def loadViewport(viewport: Viewport): Unit = {

  }

  def render(transf: Mat): Unit = {
    glUniformMatrix4fv(transform, true, transf.toFloatBuffer)
    glDrawArrays(GL_TRIANGLES, 0, mesh.numVertices)
  }
}