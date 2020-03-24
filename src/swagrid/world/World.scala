package swagrid.world

import swagrid.entity.Entity
import swagrid.event._
import swagrid.graphics.{Frame, WorldRenderer}

class World(
    val entities: Set[Entity] = Set(),
    val events: WorldEvents = new WorldEvents()
) {

  def render(event: RenderEvent): Unit =
    World.renderer.render(events.render.trigger(
      new Frame(), event))

  def update(event: UpdateEvent): World =
    events.update.trigger(this, event)

  def fixedUpdate(event: FixedUpdateEvent): World =
    events.fixedUpdate.trigger(this, event)

  def addEntity(entity: Entity): World = {
    val world = entity.init(copy(entities = entities + entity))
    world.events.create.trigger(world, new CreateEvent(entity))
  }

  def removeEntity(entity: Entity): World =
    events.destroy.trigger(this, new DestroyEvent(entity))
      .copy(entities = entities.filter(_ != entity))
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