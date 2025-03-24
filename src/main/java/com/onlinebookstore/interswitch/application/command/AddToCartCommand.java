package com.onlinebookstore.interswitch.application.command;

import com.onlinebookstore.interswitch.shared.cqrs.Command;
import lombok.Value;

import java.util.UUID;

@Value
public class AddToCartCommand implements Command<Void> {
    UUID book;
    UUID userId;
    String sessionId;
    int quantity;
}
