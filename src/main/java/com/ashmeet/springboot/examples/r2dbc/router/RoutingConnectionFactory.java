package com.ashmeet.springboot.examples.r2dbc.router;

import static com.ashmeet.springboot.examples.r2dbc.model.AppConstants.SUBSCRIPTION_CONTEXT;

import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import reactor.core.publisher.Mono;

/*

 */
public class RoutingConnectionFactory extends AbstractRoutingConnectionFactory {

  @Override
  protected Mono<Object> determineCurrentLookupKey() {
    return Mono.deferContextual(Mono::just)
        .filter(contextView -> contextView.hasKey(SUBSCRIPTION_CONTEXT))
        .map(contextView -> contextView.get(SUBSCRIPTION_CONTEXT));
  }
}
