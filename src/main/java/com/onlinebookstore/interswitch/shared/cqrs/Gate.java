package com.onlinebookstore.interswitch.shared.cqrs;

public interface Gate {
    <Result> Result execute(Command<Result> command);
}
