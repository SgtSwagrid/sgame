package swagrid.entity

import swagrid.World

trait Component {

  def init(world: World, entity: Entity): World

  def update(world: World, entity: Entity, dt: Long): World

  def fixedUpdate(world: World, entity: Entity, dt: Long): World

  def destroy(world: World, entity: Entity): World
}