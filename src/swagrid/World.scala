package swagrid

import swagrid.entity.Entity
import swagrid.graphics.{Frame, Model, WorldRenderer}

class World(entities: Set[Entity] = Set()) {

  def render(): Unit =
    World.renderer.render(entities.foldLeft(new Frame())
      {(f, e) => e.doRender(this, f)})

  def update(dt: Long): World =
    entities.foldLeft(this)
      {(w, e) => e.doUpdate(w, dt)}

  def fixedUpdate(dt: Long): World =
    entities.foldLeft(this)
      {(w, e) => e.doFixedUpdate(w, dt)}

  def addEntity(entity: Entity): World =
    entity.doInit(new World(entities = entities + entity))

  def removeEntity(entity: Entity): World =
    entity.doDestroy(new World(entities = entities
      .filterNot(_ == entity)))

  def updateEntity(oldEntity: Entity, newEntity: Entity): World =
    new World(entities = entities
      .filterNot(_ == oldEntity) + newEntity)
}

object World {
  private val renderer = new WorldRenderer()
}