package com.onlinebookstore.interswitch.shared.cqrs;

import com.onlinebookstore.interswitch.shared.ReflectionUtil;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;



/**
 * Provides a way to retrieve the appropriate QueryHandler for any given Query without manually coding it.
 * It initializes a mapping between Query types and their corresponding QueryHandler bean names
 * during the Spring ContextRefreshedEvent to ensure the handlers are available after the application context is ready.
 */
@Component
@RequiredArgsConstructor
public class QueryHandlersProvider implements ApplicationListener<ContextRefreshedEvent> {
    private final ConfigurableListableBeanFactory beanFactory;

    /**
     * A map storing Query class names as keys and their corresponding QueryHandler bean names as values.
     * This map is populated during the ContextRefreshedEvent.
     */
    private Map<String, String> handlers = new HashMap<>();


    /**
     * Retrieves the appropriate QueryHandler for the given Query.
     *
     * @param query The Query for which to retrieve a handler.
     * @param <Result> The result type of the Query and QueryHandler.
     * @return The QueryHandler that can handle the given Query.
     * @throws RuntimeException if no QueryHandler is found for the given Query.
     */
    @SuppressWarnings({"unchecked", "checkstyle:MethodTypeParameterName"})
    //Result checkstyle suppressed in favour of readability
    public <Result> QueryHandler<Query<Result>, Result> getHandler(Query<Result> query) {
        String beanName = handlers.get(query.getClass().getName());

        if (beanName != null) {
            return beanFactory.getBean(beanName, QueryHandler.class);
        } else {
            throw new RuntimeException("query handler not found. Query class is " + query.getClass());
        }
    }


    /**
     * Handles the ContextRefreshedEvent. This method is called when the Spring Application Context is initialized or refreshed.
     * It populates the 'handlers' map with Query types and their corresponding QueryHandler bean names.
     *
     * @param event The ContextRefreshedEvent.
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, String> handlersFromRefreshedContext = new HashMap<>();
        String[] queryHandlersNames = beanFactory.getBeanNamesForType(QueryHandler.class);
        for (String beanName : queryHandlersNames) {
            BeanDefinition queryHandler = beanFactory.getBeanDefinition(beanName);
            try {
                Class<?> handlerClass = Class.forName(queryHandler.getBeanClassName());
                handlersFromRefreshedContext.put(getHandledQueryType(handlerClass).getName(), beanName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        this.handlers.clear();
        this.handlers = handlersFromRefreshedContext;
    }

    /**
     * Helper method to extract the Query type handled by the QueryHandler class.
     *
     * @param clazz The QueryHandler class.
     * @return The Class<?> representing the Query type handled by the QueryHandler.
     */
    private Class<?> getHandledQueryType(Class<?> clazz) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        //TODO Think of better recursive approach than this with if length == 0
        if (genericInterfaces.length == 0) {
            genericInterfaces = clazz.getSuperclass().getGenericInterfaces();
        }
        ParameterizedType type = ReflectionUtil.findByRawType(genericInterfaces, QueryHandler.class);
        return (Class<?>) type.getActualTypeArguments()[0];
    }


}
