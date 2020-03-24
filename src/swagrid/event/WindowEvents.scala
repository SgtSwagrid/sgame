package swagrid.event

import swagrid.Window

class RenderEvent(val window: Window)

class UpdateEvent(val window: Window, val dt: Long)

class FixedUpdateEvent(val window: Window, val dt: Long)

class ClickEvent(val window: Window, val buttonId: Int,
    val actionId: Int, val cursorX: Int, val cursorY: Int)

class CursorMoveEvent(val window: Window, val cursorX: Int,
    val cursorY: Int, val cursorDX: Int, val cursorDY: Int)

class KeyEvent(val window: Window, val key: Int, val action: Int)

class ResizeEvent(val window: Window, val width: Int, val height: Int)

class CloseEvent(val window: Window)

case class WindowEvents(
    render: MEventHandler[RenderEvent] = new MEventHandler(),
    update: MEventHandler[UpdateEvent] = new MEventHandler(),
    fixedUpdate: MEventHandler[FixedUpdateEvent] = new MEventHandler(),
    click: MEventHandler[ClickEvent] = new MEventHandler(),
    cursorMove: MEventHandler[CursorMoveEvent] = new MEventHandler(),
    key: MEventHandler[KeyEvent] = new MEventHandler(),
    resize: MEventHandler[ResizeEvent] = new MEventHandler(),
    close: MEventHandler[CloseEvent] = new MEventHandler()
) {

  def onRender(f: MEventHandler[RenderEvent] =>
      MEventHandler[RenderEvent]): WindowEvents =
    copy(render = f(render))

  def onUpdate(f: MEventHandler[UpdateEvent] =>
      MEventHandler[UpdateEvent]): WindowEvents =
    copy(update = f(update))

  def onFixedUpdate(f: MEventHandler[FixedUpdateEvent] =>
      MEventHandler[FixedUpdateEvent]): WindowEvents =
    copy(fixedUpdate = f(fixedUpdate))

  def onClick(f: MEventHandler[ClickEvent] =>
      MEventHandler[ClickEvent]): WindowEvents =
    copy(click = f(click))

  def onCursorMove(f: MEventHandler[CursorMoveEvent] =>
      MEventHandler[CursorMoveEvent]): WindowEvents =
    copy(cursorMove = f(cursorMove))

  def onKey(f: MEventHandler[KeyEvent] =>
      MEventHandler[KeyEvent]): WindowEvents =
    copy(key = f(key))

  def onResize(f: MEventHandler[ResizeEvent] =>
      MEventHandler[ResizeEvent]): WindowEvents =
    copy(resize = f(resize))

  def onClose(f: MEventHandler[CloseEvent] =>
      MEventHandler[CloseEvent]): WindowEvents =
    copy(close = f(close))

  def removeHandlers(key: Any): WindowEvents =
    onRender(_.remove(key))
      .onUpdate(_.remove(key))
      .onFixedUpdate(_.remove(key))
      .onClick(_.remove(key))
      .onCursorMove(_.remove(key))
      .onKey(_.remove(key))
      .onResize(_.remove(key))
      .onClose(_.remove(key))
}