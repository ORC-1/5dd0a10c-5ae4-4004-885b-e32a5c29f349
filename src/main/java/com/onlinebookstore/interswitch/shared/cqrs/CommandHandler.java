package com.onlinebookstore.interswitch.shared.cqrs;

public interface CommandHandler<C extends Command<Result>, Result> {
    Result handle(C cmd);
}
