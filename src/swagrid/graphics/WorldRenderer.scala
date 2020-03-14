package swagrid.graphics

import scala.collection.immutable._

class WorldRenderer {

  private lazy val shader = new ModelShader()

  def render(frame: Frame): Unit = {

    shader.loadCamera(frame.camera)
    for ((mesh, models) <- frame.models) {
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

class Frame(
    val models: Map[Mesh, Map[Texture, Set[Model]]] = Map(),
    val camera: Camera = Camera()
) {

  def addModel(model: Model): Frame =
    copy(models = models + (model.mesh -> (
      models.getOrElse(model.mesh, Map()) + (model.texture -> (
        models(model.mesh).getOrElse(model.texture, Set()) + model)))))

  def camera_=(camera: Camera): Frame =
    copy(camera = camera)

  def copy(
      models: Map[Mesh, Map[Texture, Set[Model]]] = models,
      camera: Camera = camera): Frame =
    new Frame(models, camera)
}