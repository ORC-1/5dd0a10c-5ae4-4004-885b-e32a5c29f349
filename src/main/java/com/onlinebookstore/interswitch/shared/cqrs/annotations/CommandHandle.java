package com.onlinebookstore.interswitch.shared.cqrs.annotations;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * Markup annotation for Command Handler classes. Provides transactions for all methods.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
@Transactional
@Inherited
@Documented
public @interface CommandHandle {
}
