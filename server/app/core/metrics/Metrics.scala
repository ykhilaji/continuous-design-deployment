package core.metrics

import scala.concurrent.Future

import core.utils.{Configuration, Logging}
import kamon.Kamon
import kamon.metric.instrument.Time
import play.api.inject.ApplicationLifecycle

object Metrics extends Logging {

  def start(lifecycle: ApplicationLifecycle, config: Configuration): Unit = {
    val enabled = config.getBoolean("metrics.enabled").required
    val jmxEnabled = config
      .getBoolean("kamon.system-metrics.jmx-enabled")
      .optional
      .getOrElse(false)

    if (enabled) {
      Kamon.start(config.configuration.underlying)
      val start = System.currentTimeMillis()
      Kamon.metrics.gauge("uptime", Time.Milliseconds)(() => System.currentTimeMillis() - start)
      lifecycle.addStopHook(() => {
        logger.info("Shutting down kamon")
        Future.successful(Kamon.shutdown())
      })

      if (jmxEnabled) {
        CpuUsageMetrics.register(Kamon.metrics)
      }
    }
  }
}

import java.lang.management.ManagementFactory

import com.sun.management.OperatingSystemMXBean
import kamon.metric.GenericEntityRecorder
import kamon.metric.instrument.InstrumentFactory
import kamon.system.jmx.JmxSystemMetricRecorderCompanion

/** Get cpu usage for JMX Beans */
object CpuUsageMetrics extends JmxSystemMetricRecorderCompanion("jmx-cpu") {
  def apply(instrumentFactory: InstrumentFactory): CpuUsageMetrics =
    new CpuUsageMetrics(instrumentFactory)
}

class CpuUsageMetrics(instrumentFactory: InstrumentFactory) extends GenericEntityRecorder(instrumentFactory) {
  val operatingSystemBean = ManagementFactory
    .getOperatingSystemMXBean()
    .asInstanceOf[OperatingSystemMXBean]

  gauge("process-cpu-usage", () => {
    (operatingSystemBean.getProcessCpuLoad() * 100).toLong
  })

  gauge("system-cpu-load", () => {
    (operatingSystemBean.getSystemCpuLoad() * 100).toLong
  })
}
