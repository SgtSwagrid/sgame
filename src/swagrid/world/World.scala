package swagrid.world

import swagrid.entity.Entity
import swagrid.event._
import swagrid.graphics.{Frame, WorldRenderer}

class World(
    entities: Set[Entity] = Set(),
    events: WorldEvents = new WorldEvents()
) {

  def render(): Unit =
    World.renderer.render(events.render.trigger(
      new Frame(), new RenderEvent()))

  def update(dt: Long): World =
    events.update.trigger(this, new UpdateEvent(dt))

  def fixedUpdate(dt: Long): World =
    events.fixedUpdate.trigger(this, new FixedUpdateEvent(dt))

  def addEntity(entity: Entity): World =
    entity.init(copy(entities = entities + entity))

  def removeEntity(entity: Entity): World =
    events.destroy.trigger(this, new DestroyEvent(entity))
      .copy(entities = entities.filterNot(_ == entity))
      .event(_.removeHandlers(entity))

  def updateEntity(oldEntity: Entity, newEntity: Entity): World =
    newEntity.init(
      copy(entities = entities.filterNot(_ == oldEntity) + newEntity)
        .event(_.removeHandlers(oldEntity)))

  def event(e: WorldEvents => WorldEvents): World =
    copy(events = e(events))

  def copy(
      entities: Set[Entity] = entities,
      events: WorldEvents = events) =
    new World(entities, events)
}

object World {
  private val renderer = new WorldRenderer()
}