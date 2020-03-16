package swagrid.event

class EventHandler[S, E](
    handlers: Map[Any, Set[(E => Boolean, (S, E) => S)]] =
      Map[Any, Set[(E => Boolean, (S, E) => S)]]()
) {

  def trigger(state: S, event: E): S =
    handlers.flatMap{_._2}
      .filter{_._1(event)}
      .foldLeft(state){(s, h) => h._2(s, event)}

  def add(key: Any = None,
      condition: E => Boolean = _ => true)
      (handler: (S, E) => S): EventHandler[S, E] =
    new EventHandler(handlers = handlers + (key -> (
      handlers.getOrElse(key, Set()) + ((condition, handler)))))

  def remove(key: Any): EventHandler[S, E] =
    new EventHandler(handlers = handlers - key)
}