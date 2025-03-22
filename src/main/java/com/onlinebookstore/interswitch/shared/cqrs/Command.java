package com.onlinebookstore.interswitch.shared.cqrs;

import java.io.Serializable;

/**
 * Represents a command in the CQRS pattern.
 * A command is an intent to change the state of the application.
 * Commands are typically dispatched to a command handler for processing.
 *
 * @param <Result> The type of the result returned after the command is processed.
 */
@SuppressWarnings("checkstyle:InterfaceTypeParameterName")
public interface Command<Result> extends Serializable {
}
