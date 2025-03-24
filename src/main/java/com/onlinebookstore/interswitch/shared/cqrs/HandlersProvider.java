package com.onlinebookstore.interswitch.shared.cqrs;

import static java.lang.String.format;

import com.onlinebookstore.interswitch.shared.ReflectionUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class HandlersProvider implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    private Map<String, String> handlers = new HashMap<>();

    public HandlersProvider(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @SuppressWarnings("unchecked")
    public <Result> CommandHandler<Command<Result>, Result> getHandler(Command<Result> command) {
        String beanName = handlers.get(command.getClass().getName());

        if (beanName != null) {
            return beanFactory.getBean(beanName, CommandHandler.class);
        } else {
            throw new RuntimeException("command handler not found. Command class is " + command.getClass());
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        handlers.clear();
        String[] commandHandlersNames = beanFactory.getBeanNamesForType(CommandHandler.class);
        for (String beanName : commandHandlersNames) {
            BeanDefinition commandHandler = beanFactory.getBeanDefinition(beanName);
            try {
                Class<?> handlerClass = Class.forName(getBeanClassNameAdaptedForMocks(commandHandler));
                Class<?> commandClass = getHandledCommandType(handlerClass);
                handlers.put(commandClass.getName(), beanName);
                logRegisteredHandler(commandClass, handlerClass, beanName);

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
        private void logRegisteredHandler(Class<?> commandClass, Class<?> handlerClass, String beanName) {
            if (log.isDebugEnabled()) {
                log.debug("Registered command handler '{}' for command class '{}'. Bean: {}",
                        handlerClass.getName(), commandClass.getName(), beanName);
            }
        }

    private static String getBeanClassNameAdaptedForMocks(BeanDefinition commandHandler) {
        String beanClassName = commandHandler.getBeanClassName();
        if (beanClassName.contains("Mockito")) {
            beanClassName = commandHandler.getPropertyValues().get("mockClass").toString();
        }
        return beanClassName;
    }

    private Class<?> getHandledCommandType(Class<?> clazz) {
        if (!implementsCommandHandler(clazz)) {
            throw new IllegalArgumentException(format(
                    "Command Handler '%s' does not implement '%s' interface",
                    clazz.getName(), CommandHandler.class.getName()
            ));
        }
        Class<?> commandType = ReflectionUtil.findActualTypeArgumentByRawType(clazz, Command.class);
        if (commandType == null) {
            throw new IllegalArgumentException(format(
                    "No type argument of type %s found in class hierarchy of %s",
                    Command.class, clazz.getName()
            ));
        }
        return commandType;
    }

    private boolean implementsCommandHandler(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        for (Class<?> implementedInterface : clazz.getInterfaces()) {
            if (CommandHandler.class.isAssignableFrom(implementedInterface)) {
                return true;
            }
        }
        return implementsCommandHandler(clazz.getSuperclass());
    }

}

