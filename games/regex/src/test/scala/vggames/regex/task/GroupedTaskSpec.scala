package vggames.regex.task

import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import vggames.shared.task.Descriptions
import vggames.shared.task.GroupedTask
import vggames.shared.task.TaskGroup
import vggames.shared.task.TaskWithDescription
import vggames.shared.task.IndexedTask
import vggames.shared.task.Descriptions

@RunWith(classOf[JUnitRunner])
class TaskWithDescriptionTaskSpec extends Specification with Mockito {
  "a task with description" should {
    val descriptions = mock[Descriptions]

    "return group description" in {
      descriptions.forGroup("a") returns "description"

      val task = new TaskWithDescription(new IndexedTask(new GroupedTask(new TaskGroup("b", "a"), new TestTask()), 0), descriptions)
      task.description must_== "description"
    }
  }
}
