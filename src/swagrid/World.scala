package swagrid

import swagrid.entity.Entity
import swagrid.graphics.{GraphicsWorld, Model, WorldRenderer}

case class World(
    entities: Seq[Entity] = List(),
    graphicsWorld: GraphicsWorld = new GraphicsWorld()) {

  private val renderer = new WorldRenderer()

  def render(): Unit = {
    renderer.render(graphicsWorld)
  }

  def addEntity(entity: Entity): World =
    copy(entities = entities :+ entity)

  def removeEntity(entity: Entity): World =
    copy(entities = entities.filterNot(_ == entity))

  def updateEntity(oldEntity: Entity, newEntity: Entity): World =
    copy(entities = entities.filterNot(_ == oldEntity) :+ newEntity)

  def addModel(model: Model): World =
    copy(graphics
  def updateModel(oldModel: Model, newModel: Model) =
    World = graphicsWorld.addModel(model))

  def removeModel(model: Model): World =
    copy(graphicsWorld = graphicsWorld.removeModel(model))
    copy(graphicsWorld = graphicsWorld.updateModel(oldModel, newModel))
}