package com.onlinebookstore.interswitch.shared;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.springframework.util.ReflectionUtils;


/**
 * This is just a util class which will use internally {@link ReflectionUtils} class which is supposed to use only internally in
 * spring, so you should use this class instead of (using simple delegation) {@link ReflectionUtils} directly.
 */
public class ReflectionUtil {
    public static ParameterizedType findByRawType(Type[] genericInterfaces, Class<?> expectedRawType) {
        for (Type type : genericInterfaces) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parametrized = (ParameterizedType) type;
                if (expectedRawType.equals(parametrized.getRawType())) {
                    return parametrized;
                }
            }
        }
        throw new RuntimeException();
    }

}
