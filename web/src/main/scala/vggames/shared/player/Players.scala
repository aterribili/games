package vggames.shared.player

import br.com.caelum.vraptor.ioc.Component
import scala.slick.driver.SQLiteDriver.simple._
import scala.slick.session.Database.threadLocalSession
import vggames.shared.Database

case class Player(id : Long, email : String, token : String, var lastTask : Option[String], var activeTime : Long = 0) {
  def level : Long = activeTime / (60 * 15)
  def progress : Double = (activeTime % (60 * 15)).toDouble / (60 * 15) * 100
}

@Component
class Players extends Database {

  def find(token : String) : Option[Player] = {
    onDatabase {
      val query = for (player <- Players if player.token is token) yield player.*
      query.firstOption.map(tuple2Player)
    }
  }

  def findByEmail(email : String) : Option[Player] = {
    onDatabase {
      val query = for (player <- Players if player.email is email) yield player.*
      query.firstOption.map(tuple2Player)
    }
  }

  def +=(p : Player) : Player = {
    onDatabase {
      Players.noId.insert((p.email, p.token, p.lastTask, p.activeTime))
    }
    findByEmail(p.email).get
  }

  def update(p : Player) {
    onDatabase {
      val query = for (player <- Players if player.email is p.email) yield player.*
      query.update(Player.unapply(p).get)
    }
  }

  def finishGroup(p : Player, group : String) {
    onDatabase {
      FinishedGroups.insert((p.id, group))
    }
  }

  def finishedGroups(p : Player) = {
    onDatabase {
      val query = for (group <- FinishedGroups if group.playerId is p.id) yield group.group
      query.list
    }
  }

  def tuple2Player(t : (Long, String, String, Option[String], Long)) = Player(t._1, t._2, t._3, t._4, t._5)
}

object Players extends Table[(Long, String, String, Option[String], Long)]("players") {

  def id = column[Long]("id")
  def email = column[String]("email")
  def token = column[String]("token")
  def lastTask = column[Option[String]]("lastTask")
  def activeTime = column[Long]("activeTime")

  def * = id ~ email ~ token ~ lastTask ~ activeTime
  def noId = email ~ token ~ lastTask ~ activeTime
}

object FinishedGroups extends Table[(Long, String)]("finishedGroups") {

  def playerId = column[Long]("player_id")
  def group = column[String]("group")

  def * = playerId ~ group
}