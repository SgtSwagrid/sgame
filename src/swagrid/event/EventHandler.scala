package swagrid.event

class EventHandler[S, E](
    handlers: Map[Any, Set[((S, E) => S, E => Boolean)]] = Map()
) {

  def trigger(state: S, event: E): S =
    handlers.flatMap{_._2}.foldLeft(state)
      {(s, h) => h._1(s, event)}

  def add(key: Any = None,
      condition: E => Boolean = _ => true)
      (handler: (S, E) => S): EventHandler[S, E] =
    new EventHandler(handlers = handlers + (key -> (
      handlers.getOrElse(key, Set()) + ((handler, condition)))))

  def remove(key: Any): EventHandler[S, E] =
    new EventHandler(handlers = handlers - key)
}