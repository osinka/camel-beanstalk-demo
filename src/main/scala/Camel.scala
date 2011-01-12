package com.osinka.beanstalk.camel.demo

object Main {
  def main(args: Array[String]) {
    CamelKernel.boot
    CamelKernel.run
  }
}

object CamelKernel { kernel =>
  import java.util.concurrent.CountDownLatch
  import org.apache.camel.impl.DefaultCamelContext
  import org.apache.camel.{Processor, Exchange}
  import org.apache.camel.builder.RouteBuilder

  private var stopped = new CountDownLatch(1)

  lazy val context = {
    val c = new DefaultCamelContext
    c.addRoutes(routeBuilder)
    c
  }

  def stop {
    context.stop
    stopped.countDown
  }

  def boot {
    context.start

    Runtime.getRuntime.addShutdownHook(new Thread {
        override def run() {
          kernel.stop
        }
      })
  }

  def run {
    stopped.await
  }

  object routeBuilder extends RouteBuilder {
    override def configure {
      from("beanstalk:testTube").
	      wireTap("direct:process")

      from("direct:process").
        log("Processing job ${property.beanstalk.jobId} with body ${in.body}").
        threads(10).
        process(new Processor() {
            override def process(exchange: Exchange) {
              val in = exchange.getIn

              // try to make integer value out of body
              val i = Integer valueOf in.getBody(classOf[String])
              Thread sleep 1000
              in setBody i
            }
        }).
        log("Parsed job ${property.beanstalk.jobId} to body ${in.body}")

      from("timer:dig?period=30seconds").
        setBody(constant(10)).
        log("Kick ${in.body} buried/delayed tasks").
        to("beanstalk:testTube?command=kick")
    }
  }
}
