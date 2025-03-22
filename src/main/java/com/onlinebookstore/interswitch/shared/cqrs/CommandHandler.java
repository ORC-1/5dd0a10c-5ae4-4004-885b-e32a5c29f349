package com.onlinebookstore.interswitch.shared.cqrs;

/**
 * Defines the interface for handling commands in the CQRS pattern.
 * Implementations of this interface are responsible for processing a specific type of Command.
 *
 * @param <C> The type of the Command(C) this handler processes. It must extend Command<Result>.
 * @param <Result> The type of the result returned after handling the Command.
 */
@SuppressWarnings("checkstyle:InterfaceTypeParameterName")
//Result checkstyle suppressed in favour of readability
public interface CommandHandler<C extends Command<Result>, Result> {
    Result handle(C cmd);
}
