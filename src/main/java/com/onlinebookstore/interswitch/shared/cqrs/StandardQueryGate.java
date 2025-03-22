package com.onlinebookstore.interswitch.shared.cqrs;

import org.springframework.stereotype.Component;

/**
 * Default implementation of the QueryGate interface.
 * This class acts as a central point for handling query dispatching.
 * It retrieves the appropriate QueryHandler from the QueryHandlersProvider and delegates the query execution to it.
 */
@Component
public class StandardQueryGate implements QueryGate {

    private final QueryHandlersProvider handlersProvider;

    public StandardQueryGate(QueryHandlersProvider handlersProvider) {
        this.handlersProvider = handlersProvider;
    }

    @SuppressWarnings("checkstyle:MethodTypeParameterName")
    //Result checkstyle suppressed in favour of readability
    @Override
    public <Result> Result handle(Query<Result> query) {
        QueryHandler<Query<Result>, Result> handler = handlersProvider.getHandler(query);
        return handler.execute(query);
    }
}
