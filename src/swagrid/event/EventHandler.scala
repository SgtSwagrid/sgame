package swagrid.event

class IEventHandler[S, E](
    val handlers: Map[Any, Set[(E => Boolean, (S, E) => S)]] =
      Map[Any, Set[(E => Boolean, (S, E) => S)]]()
) {

  def trigger(state: S, event: E): S =
    handlers.flatMap{_._2}
      .filter{_._1(event)}
      .foldLeft(state){(s, h) => h._2(s, event)}

  def add(key: Any = None,
      condition: E => Boolean = _ => true)
      (handler: (S, E) => S): IEventHandler[S, E] =
    new IEventHandler(handlers = handlers + (key -> (
      handlers.getOrElse(key, Set()) + ((condition, handler)))))

  def add(handler: (S, E) => S): IEventHandler[S, E] =
    add()(handler)

  def remove(key: Any): IEventHandler[S, E] =
    new IEventHandler(handlers = handlers - key)
}

class MEventHandler[E](
    handlers: Map[Any, Set[(E => Boolean, E => Unit)]] =
      Map[Any, Set[(E => Boolean, E => Unit)]]()
 ) {

  def trigger(event: E): Unit =
    handlers.flatMap{_._2}
      .filter{_._1(event)}
      .foreach{h => h._2(event)}

  def add(key: Any = None,
          condition: E => Boolean = _ => true)
         (handler: E => Unit): MEventHandler[E] =
    new MEventHandler(handlers = handlers + (key -> (
      handlers.getOrElse(key, Set()) + ((condition, handler)))))

  def add(handler: E => Unit): MEventHandler[E] =
    add()(handler)

  def remove(key: Any): MEventHandler[E] =
    new MEventHandler(handlers = handlers - key)
}