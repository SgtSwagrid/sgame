package swagrid.graphics

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import swagrid.math.vector.{Mat, Transf3}

class ModelShader extends Shader(
  "src/swagrid/graphics/vertex.glsl",
  "src/swagrid/graphics/fragment.glsl") {

  private val Fov = 90.0F
  private val AspectRatio = 1.5F
  private val NearClip = 0.3F
  private val FarClip = 1000.0F

  private val Transform = glGetUniformLocation(ShaderId, "transform")
  private val View = glGetUniformLocation(ShaderId, "view")
  private val Projection = glGetUniformLocation(ShaderId, "projection")
  private val Viewport = glGetUniformLocation(ShaderId, "viewport")

  private var mesh: Mesh = null

  override protected def onBind(): Unit = {
    glBindAttribLocation(ShaderId, 0, "vertex")
    glBindAttribLocation(ShaderId, 1, "texmap")
    glBindAttribLocation(ShaderId, 2, "normal")
  }

  override protected def onInit(): Unit = {
    glUniformMatrix4fv(Projection, true, Mat.projection(
        Fov, AspectRatio, NearClip, FarClip).toFloatBuffer)
  }

  def loadMesh(mesh: Mesh): Unit = {
    glBindVertexArray(mesh.vaoId)
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
    glUniformMatrix4fv(View, true, camera.view.toFloatBuffer)
    glUniformMatrix4fv(Projection, true, camera.projection.toFloatBuffer)
    //glUniformMatrix4fv(Viewport, true, camera.viewport.matrix.toFloatBuffer)
    glUniformMatrix4fv(Viewport, true, Mat.identity(4).toFloatBuffer)
  }

  def render(transform: Transf3): Unit = {
    glUniformMatrix4fv(Transform, true, transform.matrix.toFloatBuffer)
    glDrawElements(GL_TRIANGLES, mesh.numVertices, GL_UNSIGNED_INT, 0)
    println(transform)
  }
}