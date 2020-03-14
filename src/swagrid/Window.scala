package swagrid

import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw.GLFWErrorCallback._
import org.lwjgl.opengl.GL._
import org.lwjgl.opengl.GL11._

import scala.collection.mutable

class Window(
    private var _width: Int,
    private var _height: Int,
    var title: String) {

  private var _cursorX = 0; var _cursorY = 0
  private var lastUpdate = 0L; var lastFixedUpdate = 0L
  private val fixedUpdatesPerSecond = 30

  def windowId(): Long = _windowId
  def width(): Int = _width
  def height(): Int = _height
  def cursorX(): Int = _cursorX
  def cursorY(): Int = _cursorY

  private val _windowId = init()

  def init(): Long = {

    glfwInit()
    glfwSetErrorCallback(createPrint(System.err))
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

    val windowId = glfwCreateWindow(width, height, title, 0, 0)
    glfwMakeContextCurrent(windowId)
    createCapabilities()
    glfwShowWindow(windowId)

    glEnable(GL_DEPTH_TEST)
    glEnable(GL_BLEND)
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

    windowId
  }

  def show(): Unit = {

    glfwSetFramebufferSizeCallback(windowId, (_, width, height) => {
      _width = width; _height = height
      glViewport(0, 0, width, height)
      update()
    })

    while (!glfwWindowShouldClose(windowId)) {
      update()
    }
  }

  def update(): Unit = {

    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
    glClearColor(1.0F, 1.0F, 1.0F, 1.0F)

    val now = System.currentTimeMillis()
    val dt = now - lastUpdate
    lastUpdate = now

    val fixedDt = now - lastFixedUpdate
    val targetDt = 1000 / fixedUpdatesPerSecond

    if (fixedDt > targetDt - dt/2) {
      lastFixedUpdate = now
      val fixedUpdateEvent = new FixedUpdateEvent(this, fixedDt)
      fixedUpdateListeners.foreach(_(fixedUpdateEvent))
    }

    val updateEvent = new UpdateEvent(this, dt)
    updateListeners.foreach(_(updateEvent))

    val renderEvent = new RenderEvent(this)
    renderListeners.foreach(_(renderEvent))

    glfwSwapBuffers(windowId)
    glfwPollEvents()
    glfwSetWindowTitle(windowId, title)
  }

  private val renderListeners = mutable.Set[RenderEvent => Unit]()

  def onRender(action: RenderEvent => Unit): Window = {
    renderListeners += action
    this
  }

  class RenderEvent(val window: Window)

  private val updateListeners = mutable.Set[UpdateEvent => Unit]()

  def onUpdate(action: UpdateEvent => Unit): Window = {
    updateListeners += action
    this
  }

  class UpdateEvent(val window: Window, val dt: Long)

  private val fixedUpdateListeners = mutable.Set[FixedUpdateEvent => Unit]()

  def onFixedUpdate(action: FixedUpdateEvent => Unit): Window = {
    fixedUpdateListeners += action
    this
  }

  class FixedUpdateEvent(val window: Window, val dt: Long)

  private val clickListeners = mutable.Set[ClickEvent => Unit]()

  def onClick(action: ClickEvent => Unit): Window = {
    clickListeners += action
    this
  }

  glfwSetMouseButtonCallback(windowId, (_, button, action, _) => {

    val event = new ClickEvent(this, button, action, cursorX, cursorY)
    clickListeners.foreach(_(event))
  })

  class ClickEvent(val window: Window, val button: Int,
      val action: Int, val cursorX: Int, val cursorY: Int)

  private val moveListeners = mutable.Set[MoveEvent => Unit]()

  def onMouseMove(action: MoveEvent => Unit): Window = {
    moveListeners += action
    this
  }

  glfwSetCursorPosCallback(windowId, (_, cx, cy) => {

    val cursorX = cx.toInt - width/2
    val cursorY = height/2 - cy.toInt

    val cursorDX = cursorX - this.cursorX
    val cursorDY = cursorY - this.cursorY

    _cursorX = cursorX; _cursorY = cursorY

    val event = new MoveEvent(this, cursorX, cursorY, cursorDX, cursorDY)
    moveListeners.foreach(_(event))
  })

  class MoveEvent(val window: Window, val cursorX: Int,
      val cursorY: Int, val cursorDX: Int, val cursorDY: Int)

  private val keyListeners = mutable.Set[KeyEvent => Unit]()

  def onKey(action: KeyEvent => Unit): Window = {
    keyListeners += action
    this
  }

  glfwSetKeyCallback(windowId, (_, key, _, action, _) => {

    val event = new KeyEvent(this, key, action)
    keyListeners.foreach(_(event))
  })

  class KeyEvent(val window: Window, val key: Int, val action: Int)

  private val resizeListeners = mutable.Set[ResizeEvent => Unit]()

  def onResize(action: ResizeEvent => Unit): Window = {
    resizeListeners += action
    this
  }

  glfwSetWindowSizeCallback(windowId, (_, width, height) => {

    val event = new ResizeEvent(this, width, height)
    resizeListeners.foreach(_(event))
  })

  class ResizeEvent(val window: Window, val width: Int, val height: Int)
}