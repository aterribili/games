package vggames.shared.vraptor

import java.util.concurrent.ConcurrentHashMap

import scala.collection.JavaConverters._
import scala.util.Try

import br.com.caelum.vraptor.ioc.ApplicationScoped
import br.com.caelum.vraptor.ioc.Component
import br.com.caelum.vraptor.ioc.ComponentFactory
import vggames.shared.Game
import vggames.shared.GameEngine

@Component
class GameFactory(cached : GameFactoryCache, data : RequestData) extends ComponentFactory[Game] {

  def getInstance : Game = cached(data.game).getOrElse(
    throw new RuntimeException("Não foi possível criar o jogo [" + data.game + "]. Talvez " +
      "seja necessário registrá-lo na GameFactory"))
}

@Component
@ApplicationScoped
class GameFactoryCache(cache : DescriptionsCache) {

  val games = new ConcurrentHashMap[String, Option[Game]]().asScala

  def apply(gameName : String) : Option[Game] = games.get(gameName) match {
    case Some(game) => game
    case None => attemptToLoad(gameName, cache)
  }

  private def attemptToLoad(name : String, cache : DescriptionsCache) : Option[Game] = {
    val engineName = s"vggames.$name.${name.capitalize}Game"

    games.getOrElseUpdate(engineName, {
      val engine = Try(Class.forName(engineName).newInstance.asInstanceOf[GameEngine]).
        map(new Game(_, cache.get(name)))
      engine.toOption
    })
  }
}

