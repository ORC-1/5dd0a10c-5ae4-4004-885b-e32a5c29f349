package com.onlinebookstore.interswitch.shared.cqrs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StandardGate implements Gate {
    private final HandlersProvider handlersProvider;

    @Override
    public <Result> Result execute(Command<Result> command) {
        CommandHandler<Command<Result>, Result> handler = handlersProvider.getHandler(command);
        return handler.handle(command);
    }

}
