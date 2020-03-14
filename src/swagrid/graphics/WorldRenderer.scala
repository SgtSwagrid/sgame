package swagrid.graphics

import scala.collection.immutable._

class WorldRenderer {

  private val shader = new ModelShader()

  def render(world: GraphicsWorld): Unit = {

    for ((mesh, models) <- world.models) {
      shader.loadMesh(mesh)
      for ((texture, models) <- models) {
        shader.loadTexture(texture)
        for (model <- models) {
          shader.loadMaterial(model.material)
          shader.render(model.transform)
        }
      }
      shader.unloadMesh()
    }
  }
}

class GraphicsWorld(val models:
    Map[Mesh, Map[Texture, Set[Model]]] = Map()) {

  def addModel(model: Model): GraphicsWorld =
    new GraphicsWorld(models + (model.mesh -> (
      models.getOrElse(model.mesh, Map()) + (model.texture -> (
        models(model.mesh).getOrElse(model.texture, Set()) + model)))))

  def removeModel(model: Model): GraphicsWorld =
    new GraphicsWorld(models + (model.mesh -> (
      models.getOrElse(model.mesh, Map()) + (model.texture -> (
        models(model.mesh).getOrElse(model.texture, Set()) - model)))))

  def updateModel(oldModel: Model, newModel: Model): GraphicsWorld =
    removeModel(oldModel).addModel(newModel)
}