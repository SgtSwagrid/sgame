package swagrid

import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw.GLFWErrorCallback._
import org.lwjgl.opengl.GL._
import org.lwjgl.opengl.GL11._
import swagrid.event._

class Window(
    private var _width: Int,
    private var _height: Int,
    var title: String) {

  private var events = new WindowEvents()

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

  def initCallbacks(): Unit = {

    glfwSetMouseButtonCallback(windowId, (_, button, action, _) =>
      events.click.trigger(new ClickEvent(
        this, button, action, cursorX, cursorY)))

    glfwSetKeyCallback(windowId, (_, key, _, action, _) =>
      events.key.trigger(new KeyEvent(
        this, key, action)))

    glfwSetCursorPosCallback(windowId, (_, cursorX, cursorY) => {

      val cx = cursorX.toInt - width / 2
      val cy = height / 2 - cursorY.toInt

      events.cursorMove.trigger(new CursorMoveEvent(
        this, cx, cy, cx - this.cursorX, cy - this.cursorY))

      this._cursorX = cx
      this._cursorY = cy
    })
  }

  def show(): Unit = {

    initCallbacks()

    glfwSetFramebufferSizeCallback(windowId, (_, width, height) => {

      _width = width; _height = height
      glViewport(0, 0, width, height)
      events.resize.trigger(new ResizeEvent(this, width, height))
      update()
    })

    while (!glfwWindowShouldClose(windowId)) {
      update()
    }
    events.close.trigger(new CloseEvent(this))
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
      events.fixedUpdate.trigger(new FixedUpdateEvent(this, fixedDt))
    }

    events.update.trigger(new UpdateEvent(this, dt))
    events.render.trigger(new RenderEvent(this))

    glfwSwapBuffers(windowId)
    glfwPollEvents()
    glfwSetWindowTitle(windowId, title)
  }

  def event(e: WindowEvents => WindowEvents): Window = {
    events = e(events)
    this
  }
}