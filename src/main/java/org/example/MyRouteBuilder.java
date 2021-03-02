package org.example;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.spi.PropertiesComponent;

import java.util.Properties;

/**
 * A Camel Java DSL Router
 */
public class MyRouteBuilder extends EndpointRouteBuilder {

  /**
   * Let's configure the Camel routing rules using Java & Endpoint DSLs
   */
  public void configure() {
    CamelContext camelContext = getContext();

    PropertiesComponent pc = camelContext.getPropertiesComponent();
    pc.setLocation("classpath:connection.properties");
    pc.loadProperties();

    from(timer("myTimer").period(30_000).repeatCount(3))
        .setHeader(Exchange.HTTP_URI, constant("https://jigsaw.w3.org/HTTP/Basic/"))
        // This works
        //.to("https://inline?authMethod=Basic&authPassword={{prop.password}}&authUsername={{prop.username}}&authenticationPreemptive=true")
        // This doesn't
        .to(https("inline").authUsername("{{prop.username}}").authPassword("{{prop.password}}").authenticationPreemptive(true))
        .log("${body}");
  }
}
