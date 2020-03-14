package swagrid.event

import swagrid.entity.Entity
import swagrid.graphics.Frame
import swagrid.world.World

class RenderEvent()
class UpdateEvent(val dt: Long)
class FixedUpdateEvent(val dt: Long)
class DestroyEvent(val entity: Entity)

case class WorldEvents(
    render: EventHandler[Frame, RenderEvent] = new EventHandler[Frame, RenderEvent](),
    update: EventHandler[World, UpdateEvent] = new EventHandler(),
    fixedUpdate: EventHandler[World, FixedUpdateEvent] = new EventHandler(),
    destroy: EventHandler[World, DestroyEvent] = new EventHandler()
) {

  def render(f: EventHandler[Frame, RenderEvent] =>
      EventHandler[Frame, RenderEvent]): WorldEvents =
    copy(render = f(render))

  def update(f: EventHandler[World, UpdateEvent] =>
      EventHandler[World, UpdateEvent]): WorldEvents =
    copy(update = f(update))

  def fixedUpdate(f: EventHandler[World, FixedUpdateEvent] =>
      EventHandler[World, FixedUpdateEvent]): WorldEvents =
    copy(fixedUpdate = f(fixedUpdate))

  def destroy(f: EventHandler[World, DestroyEvent] =>
      EventHandler[World, DestroyEvent]): WorldEvents =
    copy(destroy = f(destroy))

  def removeHandlers(key: Any): WorldEvents =
    render(_.remove(key))
      .update(_.remove(key))
      .fixedUpdate(_.remove(key))
      .destroy(_.remove(key))
}