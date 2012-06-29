package vggames.regex.task;

import java.util.regex.{Pattern, Matcher}

import scala.collection.mutable.ListBuffer

import vggames.regex.task.MatcherTargets.from
import vggames.shared.task.status.Ok
import vggames.shared.task.{Task, JudgedTask}

class CaptureGroup(val matchingTarget : String, val captureGroupTargets : String*) extends Task {

  val validations = ListBuffer[GroupValidation]()
  addAllValidations()

  def addAllValidations() : Unit = {
    validations += new ValidationIfMatch(matchingTarget)
    validations += new ValidationIfAllCapture(captureGroupTargets.toList : _*)
    validations += new ValidationCaptureCorrectGroup(matchingTarget)
    validations += new ValidationIfAllGroupsMatch(captureGroupTargets.toList : _*)
  }

  def judge(challenge : String) : JudgedTask = {
    val matcher : Matcher = Pattern.compile(challenge).matcher(matchingTarget);
    return applyAllValidations(challenge, matcher);

  }

  def applyAllValidations(challenge : String, matcher : Matcher) : JudgedTask = {
    validations.foldLeft[JudgedTask](new Ok())((judge, validation) =>
      judge.getOk match {
        case true => validation.judge(challenge, matcher)
        case false => judge
      })
  }

  def getChallenge : String = {
    "Qual regex d&aacute; match em " + from(matchingTarget).asHtml() + " e captura " + captureTarget + "?"
  }

  def captureTarget : String = {
    from(captureGroupTargets.toList : _*).asHtml()
  }

  override def toString() : String = {
    getChallenge
  }

}
