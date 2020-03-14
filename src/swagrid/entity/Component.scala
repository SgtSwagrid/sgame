package swagrid.entity

import swagrid.World
import swagrid.graphics.Frame

trait Component {

  def init(world: World, entity: Entity): World = world

  def update(world: World, entity: Entity, dt: Long): World = world

  def fixedUpdate(world: World, entity: Entity, dt: Long): World = world

  def render(world: World, entity: Entity, frame: Frame): Frame = frame

  def destroy(world: World, entity: Entity): World = world
}