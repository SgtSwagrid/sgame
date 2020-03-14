package swagrid.entity

import swagrid.World
import swagrid.graphics.Frame
import swagrid.math.vector.Transf3

class Entity(
    val transform: Transf3 = Transf3(),
    val components: Seq[Component] = List()) {

  def transform(transformation: Transf3 => Transf3): Entity =
    transform_=(transformation(transform))

  def transform_=(transform: Transf3): Entity =
    new Entity(transform = transform)

  def addComponent(component: Component): Entity =
    new Entity(components = components :+ component)

  def removeComponent(component: Component): Entity =
    new Entity(components = components
      .filterNot(_ == component))

  def updateComponent(oldComponent: Component, newComponent: Component): Entity =
    new Entity(components = components
      .filterNot(_ == oldComponent) :+ newComponent)

  def doInit(world: World): World =
    init(components.foldLeft(world)
      {(w, c) => c.init(w, this)})

  def doUpdate(world: World, dt: Long): World =
    update(components.foldLeft(world)
      {(w, c) => c.update(w, this, dt)}, dt)

  def doFixedUpdate(world: World, dt: Long): World =
    fixedUpdate(components.foldLeft(world)
      {(w, c) => c.fixedUpdate(w, this, dt)}, dt)

  def doRender(world: World, frame: Frame): Frame =
    render(world, components.foldLeft(frame)
      {(f, c) => c.render(world, this, f)})

  def doDestroy(world: World): World =
    destroy(components.foldLeft(world)
      {(w, c) => c.destroy(w, this)})

  protected def init(world: World): World = world

  protected def update(world: World, dt: Long): World = world

  protected def fixedUpdate(world: World, dt: Long): World = world

  protected def render(world: World, frame: Frame): Frame = frame

  protected def destroy(world: World): World = world
}