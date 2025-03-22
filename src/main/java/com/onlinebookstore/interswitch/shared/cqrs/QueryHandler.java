package com.onlinebookstore.interswitch.shared.cqrs;

/**
 * Defines the interface for handling queries in the CQRS pattern.
 * Implementations of this interface are responsible for processing a specific type of Query
 * and returning the corresponding result.
 *
 * @param <Q> The type of the Query this handler processes. It must extend Query<Result>.
 * @param <Result> The type of the result returned after handling the Query.
 */
@SuppressWarnings("checkstyle:InterfaceTypeParameterName")
//Result checkstyle suppressed in favour of readability
public interface QueryHandler<Q extends Query<Result>, Result> {
    Result execute(Q cmd);
}
