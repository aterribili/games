package vggames.shared.auth

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import vggames.shared.auth.twitter.TwitterAuthProvider

class ProvidersSpec extends Specification with Mockito {
  val twitterAuthPovider = mock[TwitterAuthProvider]
  val aTestAuthProvider = mock[AuthProvider]

  twitterAuthPovider.name returns "twitter"
  aTestAuthProvider.name returns "TestProvider"

  val providers = new Providers(List(twitterAuthPovider, aTestAuthProvider))

  "providers" should {
    "have the twitter provider" in {
      providers.quantity must_== 2
      providers("twitter") must_== twitterAuthPovider
      providers("TestProvider") must_== aTestAuthProvider
    }

    "find the twitter provider" in {
      providers(twitterAuthPovider.name) must_== twitterAuthPovider
    }
  }
}
