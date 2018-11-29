# Play framework template

Template of an application using play framework (v2.6) and compile time dependency injection

Contains :

 - basic dependencies (ws, tests)
 - Compilation options to keep the code secure
 - automatic formating to keep a coherent style
 - basic metrics reportings
 - Assets management (digest, gzip)

## Run

```
sbt run
```

(or `~run` for automatic reloading)

## Layout

```
.
├── app                  # application sources
│   ├── core                  # common classes that can be reused in other modules
│   ├── user                  # "user" module
│       ├── controllers
│       ├── models
│       ├── services
│       └── views
├── conf                 # contains app configuration: `application.conf`, `logback.xml` ...
│   └── evolutions            # sql scripts to execute on db (if any)
├── project              # sbt project configuration and plugins
├── target               # directory used by sbt during compilation
└── test                 # application tests
```


#### Influxdb - Grafana

cf projects documentation:

- [Influxdb](https://docs.influxdata.com/influxdb/v1.2/introduction/installation/)
- [Grafana](http://docs.grafana.org/installation/)

## Logs

Logs are essential in an application. They allow developers and sys admins to monitor and debug the app.
So logs should be **efficient** and **used carefuly**

To write logs, you need an instance of the `Logger` object. You can use the trait in `core.utils.Settings` to automatically get a configured instance.

Example:

```scala
class MyAlgorithm extends core.utils.Logging {
  // here I have an instance of `play.api.Logger`
  // this is equivalent to `val logger = play.api.Logger("MyAlgorithm")`

  logger.info("...")
}
```

You have several log levels available : `trace`, `debug`, `info`, `warn` and `error`.

An application should use several log levels to give several levels of information. A simple rule is to use `trace` and `debug` for information usefull during development and that could help debug some complex parts of the system. <br>
`info` is used for information on the normal execution of a program. <br>
`warn` is used for non fatal informations. <br>
`error` is used to log exceptions and all that can stop an application.

⚠️ You **must not** log confidential information like user passwords, user personal information, secret keys ...


Example:

```scala

class MyService extends Logging {
  def createUser(userData: UserData): Future[Option[User]] = {
    // don't log all userData, it may contain sensitive information
    logger.debug(s"creating user with data ${userData.name}")
    userStorage.create(userData)
      .recoverWith {
        case DuplicatedElement =>
          logger.warn(s"User ${userData.name} already exists")
          Future.successful(None)
        case exception =>
          logger.error(s"Error while creating user ${userData.name}", exception)
          Future.failed(exception)
      }
  }
}
```

Logs are written to `stdout` and also to the file `logs/application.conf`.
Play uses [logback](https://logback.qos.ch). The configuration file is `conf/logback.xml`.

[Read also](https://www.clever-cloud.com/blog/engineering/2016/05/23/let-your-logs-help-you/)

