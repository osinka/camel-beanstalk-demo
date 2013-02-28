package demo

import org.apache.camel.builder.RouteBuilder
import org.apache.camel.{LoggingLevel, Exchange, Processor}
import com.typesafe.config.ConfigFactory

class MyException(msg: String) extends Exception(msg)

class BeanstalkRoute extends RouteBuilder {
  val config = ConfigFactory.load()

  override def configure() {
    val tube = config.getString("tube")

    onException(classOf[MyException])
      .maximumRedeliveries(2)
      .logRetryAttempted(false)
      .logRetryStackTrace(false)
      .logExhausted(true)
      .retriesExhaustedLogLevel(LoggingLevel.ERROR)

    from("beanstalk:%s?consumer.awaitJob=false" format tube)
      .to("seda:process")

    from("seda:process")
      .log("Processing job ${property.beanstalk.jobId} with body ${in.body}")
      .threads(config.getInt("threads"))
      .process(TestProcessor)
      .log("Parsed job ${property.beanstalk.jobId} to body ${in.body}")

    from("timer:dig?period=%sseconds" format config.getMilliseconds("diggerPeriod")/1000)
      .setBody(constant(config.getInt("digBatchSize")))
      .log("Kick ${in.body} buried/delayed tasks")
      .to("beanstalk:%s?command=kick" format tube)
  }

  object TestProcessor extends Processor {
    lazy val sleepMs = config.getMilliseconds("sleep")

    override def process(exchange: Exchange) {
      val in = exchange.getIn

      // try to make integer value out of body
      val i = Integer valueOf in.getBody(classOf[String])
      if (i % 3 == 0)
        throw new MyException("Argument %d, modulo %d".format(i, i%3))

      Thread sleep sleepMs
      in setBody i
    }
  }
}
