package vggames.shared.player

import br.com.caelum.vraptor.ioc.{SessionScoped, Component}
import vggames.shared.auth.{AuthorizationVerifier, AuthProvider}

@Component
@SessionScoped
case class Player(val name : String) {
  var provider : AuthProvider = _

  def authorize(authorization : AuthorizationVerifier) = authorization.authorized match {
    case true => provider.accessToken(authorization.verifier)
    case false => provider = null
  }

  def getAuthorized = {
    provider != null
  }

  def getUserName = provider.userName

  def logout = {
    if (getAuthorized) {
      provider.logout
      provider = null
    }
  }
}