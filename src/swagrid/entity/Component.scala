package swagrid.entity

import swagrid.world.World

trait Component {

  def init(world: World, entity: Entity): World = world
}