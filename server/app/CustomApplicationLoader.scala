import com.marcospereira.play.i18n.HoconMessagesApiProvider
import controllers.AssetsComponents
import core.metrics.MetricsFilter
import play.api.http.HttpErrorHandler
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator}
import cdd.clients.GithubClient
import cdd.services.FigmaService

import play.filters.HttpFiltersComponents
import play.api.i18n._
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.routing.Router

import router.Routes
import user.controllers.authentication.Auth

class CustomApplicationLoader extends ApplicationLoader {
  def load(context: ApplicationLoader.Context) = {
    LoggerConfigurator(context.environment.classLoader).foreach {
      _.configure(context.environment)
    }
    new CustomComponents(context).application
  }
}

class CustomComponents(context: ApplicationLoader.Context)
    extends BuiltInComponentsFromContext(context) with AhcWSComponents with HttpFiltersComponents with AssetsComponents
    with I18nComponents {

  // configuration
  val conf = core.utils.Configuration(configuration)
  val settings = new core.utils.Settings(conf)
  core.metrics.Metrics.start(applicationLifecycle, conf)

  // override messagesApi to use Hocon config
  override lazy val messagesApi = new HoconMessagesApiProvider(environment, configuration, langs, httpConfiguration).get

  // use this ws client to benefit from automatic metrics and logging
  lazy val instrumentedWSClient = core.metrics.WSClientInstrumented(wsClient)

  // executionContexts
  val defaultEc = controllerComponents.executionContext

  override def httpFilters = Seq(csrfFilter, securityHeadersFilter, allowedHostsFilter) ++ MetricsFilter(conf)

  // services
  lazy val jsMessages = new jsmessages.JsMessagesFactory(messagesApi).all
  lazy val apiKeyService = new user.services.ApiKeyService()

  lazy val auth = new Auth(settings, apiKeyService)(defaultEc, materializer, messagesApi, controllerComponents)

  override lazy val httpErrorHandler: HttpErrorHandler = new core.api.HttpErrorHandler()(messagesApi)
  lazy val githubToken = configuration.get[String]("github.token")
  lazy val githubClient = new GithubClient(wsClient, githubToken)
  lazy val figmaService = new FigmaService(wsClient, conf)

  lazy val router: Router = new Routes(
    httpErrorHandler,
    new cdd.controllers.GithubController(controllerComponents, githubClient),
    new cdd.controllers.FigmaController(controllerComponents, figmaService, githubClient),
    new core.controllers.XAssets(environment, httpErrorHandler, assetsMetadata, controllerComponents),
    new front.StateController(controllerComponents)
    // new user.controllers.HomeController(controllerComponents, jsMessages)
  )
}
