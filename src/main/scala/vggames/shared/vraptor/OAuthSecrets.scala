package vggames.shared.vraptor

import java.io.{FileInputStream, File}
import java.util.Properties

import br.com.caelum.vraptor.ioc.{Component, ApplicationScoped}

@Component
@ApplicationScoped
class OAuthSecrets {
  val properties = new Properties()
  val file = new File(System.getProperty("user.home") + "/.vgGames/oauth_secrets")
  if (file.exists) 
  	properties.load(new FileInputStream(file)) 

  def apiKeyFor(providerName: String) = {
    properties.getProperty(providerName + ".api.key", "")
  }

  def apiSecretFor(providerName: String) = {
    properties.getProperty(providerName + ".api.secret", "")
  }
}