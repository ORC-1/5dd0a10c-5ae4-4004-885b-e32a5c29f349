package com.onlinebookstore.interswitch.shared.cqrs;

import java.io.Serializable;

/**
 * Represents a query in the CQRS pattern.
 * A query is a request for information without changing the state of the application.
 * Queries are typically handled by query handlers to retrieve data.
 *
 * @param <Result> The type of the result returned by the query.
 */
//Result checkstyle suppressed in favour of readability.
@SuppressWarnings("checkstyle:InterfaceTypeParameterName")
//Result checkstyle suppressed in favour of readability
public interface Query<Result> extends Serializable {
}
