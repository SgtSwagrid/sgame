package swagrid.graphics

import scala.io.Source
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._

abstract class Shader(VertexFile: String, FragmentFile: String) {

  private val VertexId = compile(VertexFile, GL_VERTEX_SHADER)
  private val FragmentId = compile(FragmentFile, GL_FRAGMENT_SHADER)
  protected val ShaderId = glCreateProgram()

  glAttachShader(ShaderId, VertexId)
  glAttachShader(ShaderId, FragmentId)

  onBind()
  glLinkProgram(ShaderId)
  glValidateProgram(ShaderId)

  glUseProgram(ShaderId)
  onInit()
  glUseProgram(0)

  private def compile(shaderFile: String, shaderType: Int): Int = {

    val shaderSrc = Source.fromFile(shaderFile).getLines().mkString("\n")

    val shaderId = glCreateShader(shaderType)
    glShaderSource(shaderId, shaderSrc)
    glCompileShader(shaderId)

    if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
      System.err.println(s"Error in $shaderFile:\n")
      System.err.println(glGetShaderInfoLog(shaderId, 500))
      System.exit(0)
    }
    shaderId
  }

  def enable(): Unit = glUseProgram(ShaderId)
  def disable(): Unit = glUseProgram(0)

  protected def onBind(): Unit
  protected def onInit(): Unit
}