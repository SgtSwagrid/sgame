package swagrid.graphics

import scala.collection.immutable._

class WorldRenderer {

  private lazy val shader = new ModelShader()

  def render(frame: Frame): Unit = {

    shader.enable()
    shader.loadCamera(frame.camera)
    for ((mesh, models) <- frame.models) {
      shader.loadMesh(mesh)
      for ((texture, models) <- models) {
        if (texture.isDefined)
          shader.loadTexture(texture.get)
        for (model <- models) {
          shader.loadMaterial(model.material)
          shader.render(model.transform)
        }
      }
      shader.unloadMesh()
    }
    shader.disable()
  }
}

class Frame(
    val models: Map[Mesh, Map[Option[Texture], Set[Model]]] = Map(),
    val camera: Camera = null
) {

  def addModel(model: Model): Frame =
    copy(models = models + (model.mesh -> (
      models.getOrElse(model.mesh, Map()) + (model.texture -> (
        models.getOrElse(model.mesh, Map())
          .getOrElse(model.texture, Set()) + model)))))

  def camera_=(camera: Camera): Frame =
    copy(camera = camera)

  def copy(
      models: Map[Mesh, Map[Option[Texture], Set[Model]]] = models,
      camera: Camera = camera): Frame =
    new Frame(models, camera)
}