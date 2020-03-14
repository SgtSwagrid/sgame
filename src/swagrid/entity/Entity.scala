package swagrid.entity

import swagrid.World

case class Entity(components: Seq[Component] = List()) {

  def addComponent(component: Component): Entity =
    copy(components = components :+ component)

  def removeComponent(component: Component): Entity =
    copy(components = components.filterNot(_ == component))

  def updateComponent(oldComponent: Component, newComponent: Component): Entity =
    copy(components = components.filterNot(_ == oldComponent) :+ newComponent)

  def doInit(world: World): World =
    init(components.foldLeft(world.addEntity(this))
      {(w, c) => c.init(w, this)})

  def doUpdate(world: World, dt: Long) =
    update(components.foldLeft(world)
      {(w, c) => c.update(w, this, dt)}, dt)

  def doFixedUpdate(world: World, dt: Long) =
    fixedUpdate(components.foldLeft(world)
      {(w, c) => c.fixedUpdate(w, this, dt)}, dt)

  def doDestroy(world: World): World =
    destroy(components.foldLeft(world)
      {(w, c) => c.destroy(w, this)})
      .removeEntity(this)

  protected def init(world: World): World = world

  protected def update(world: World, dt: Long): World = world

  protected def fixedUpdate(world: World, dt: Long): World = world

  protected def destroy(world: World): World = world
}