package swagrid.graphics

import scala.io.Source
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._

abstract class Shader(vertexShaderFile: String, fragmentShaderFile: String) {

  private var shaderProgramId = 0
  private var vertexShaderId = 0
  private var fragmentShaderId = 0

  def init(): Unit = {

    vertexShaderId = compile(vertexShaderFile, GL_VERTEX_SHADER)
    fragmentShaderId = compile(fragmentShaderFile, GL_FRAGMENT_SHADER)

    shaderProgramId = glCreateProgram()
    glAttachShader(shaderProgramId, vertexShaderId)
    glAttachShader(shaderProgramId, fragmentShaderId)

    onBind()
    glLinkProgram(shaderProgramId)
    glValidateProgram(shaderProgramId)

    glUseProgram(shaderProgramId)
    onInit()
    glUseProgram(0)
  }

  private def compile(shaderFile: String, shaderType: Int): Int = {

    val shaderSrc = Source.fromResource(shaderFile,
        getClass().getClassLoader()).mkString("\n")

    val shaderId = glCreateShader(shaderType)
    glShaderSource(shaderId, shaderSrc)
    glCompileShader(shaderId)

    if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
      System.err.println(glGetShaderInfoLog(shaderId, 500))
      System.exit(0)
    }
    shaderId
  }

  protected def shaderId: Int = shaderProgramId

  def enable(): Unit = glUseProgram(shaderProgramId)
  def disable(): Unit = glUseProgram(0)

  protected abstract def onBind(): Unit = ???
  protected abstract def onInit(): Unit = ???
}