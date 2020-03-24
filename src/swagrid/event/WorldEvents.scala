package swagrid.event

import swagrid.entity.Entity
import swagrid.graphics.Frame
import swagrid.world.World

class CreateEvent(val entity: Entity)

class DestroyEvent(val entity: Entity)

case class WorldEvents(
    render: IEventHandler[Frame, RenderEvent] = new IEventHandler(),
    update: IEventHandler[World, UpdateEvent] = new IEventHandler(),
    fixedUpdate: IEventHandler[World, FixedUpdateEvent] = new IEventHandler(),
    create: IEventHandler[World, CreateEvent] = new IEventHandler(),
    destroy: IEventHandler[World, DestroyEvent] = new IEventHandler()
) {

  def onRender(f: IEventHandler[Frame, RenderEvent] =>
      IEventHandler[Frame, RenderEvent]): WorldEvents =
    copy(render = f(render))

  def onUpdate(f: IEventHandler[World, UpdateEvent] =>
      IEventHandler[World, UpdateEvent]): WorldEvents =
    copy(update = f(update))

  def onFixedUpdate(f: IEventHandler[World, FixedUpdateEvent] =>
      IEventHandler[World, FixedUpdateEvent]): WorldEvents =
    copy(fixedUpdate = f(fixedUpdate))

  def onCreate(f: IEventHandler[World, CreateEvent] =>
      IEventHandler[World, CreateEvent]): WorldEvents =
    copy(create = f(create))

  def onDestroy(f: IEventHandler[World, DestroyEvent] =>
      IEventHandler[World, DestroyEvent]): WorldEvents =
    copy(destroy = f(destroy))

  def removeHandlers(key: Any): WorldEvents =
    onRender(_.remove(key))
      .onUpdate(_.remove(key))
      .onFixedUpdate(_.remove(key))
      .onCreate(_.remove(key))
      .onDestroy(_.remove(key))
}