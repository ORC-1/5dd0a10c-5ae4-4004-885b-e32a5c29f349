package com.onlinebookstore.interswitch.shared.cqrs;

/**
 * Defines the interface for a query gate.
 * A query gate is responsible for handling the execution of queries.
 * It acts as a central point for dispatching queries to their corresponding handlers.
 */
public interface QueryGate {

    @SuppressWarnings("checkstyle:MethodTypeParameterName")
    //Result checkstyle suppressed in favour of readability
    <Result> Result handle(Query<Result> query);
}
