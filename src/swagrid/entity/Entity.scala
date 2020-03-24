package swagrid.entity

import swagrid.math.vector.Transf3
import swagrid.world.World

class Entity(
    val transform: Transf3 = Transf3(),
    val components: Seq[Component] = List()
) {

  def init(world: World): World =
    components.foldLeft(world){(w, c) => c.init(w, this)}

  def move(t: Transf3 => Transf3): Entity =
    at(t(transform))

  def at(transform: Transf3): Entity =
    copy(transform = transform)

  def updateComponent(oldComp: Component, newComp: Component): Entity =
    copy(components = components.filter(_ != oldComp) :+ newComp)

  def addComponent(comp: Component): Entity =
    copy(components = components :+ comp)

  def removeComponent(comp: Component): Entity =
    copy(components = components.filter(_ != comp))

  def copy(
      transform: Transf3 = transform,
      components: Seq[Component] = components): Entity =
    new Entity(transform, components)
}