package com.onlinebookstore.interswitch.shared;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import static java.lang.String.format;


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


    public static Class<?> findActualTypeArgumentByRawTypeDeclaredOn(Class<?> baseClass, Class<?> expectedRawType, Class<?> declaredOnClass) {
        Class<?> resolvedType = findActualTypeArgumentDeclaredOnClass(baseClass, expectedRawType);

        if (resolvedType != null) {
            return resolvedType;
        }

        List<Class<?>> resolvedTypes = Stream.of(ArrayUtils.add(baseClass.getInterfaces(), baseClass.getSuperclass()))
                .filter(Objects::nonNull)
                .map(Class.class::cast)
                .filter(declaredOnClass::isAssignableFrom)
                .map(superClass -> findActualTypeArgumentByRawType(superClass, expectedRawType))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (resolvedTypes.size() > 1) {
            throw new IllegalArgumentException(format(
                    "More than one type argument of type %s found in class hierarchy of %s",
                    expectedRawType.getName(), baseClass.getName()
            ));
        }

        if (CollectionUtils.isEmpty(resolvedTypes)) {
            return null;
        }

        return resolvedTypes.get(0);
    }

    public static Class<?> findActualTypeArgumentByRawType(Class<?> baseClass, Class<?> expectedRawType) {
        Class<?> resolvedType = findActualTypeArgumentDeclaredOnClass(baseClass, expectedRawType);

        if (resolvedType != null) {
            return resolvedType;
        }

        List<Class<?>> resolvedTypes = Stream.of(ArrayUtils.add(baseClass.getInterfaces(), baseClass.getSuperclass()))
                .filter(Objects::nonNull)
                .map(Class.class::cast)
                .map(superClass -> findActualTypeArgumentByRawType(superClass, expectedRawType))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (resolvedTypes.size() > 1) {
            throw new IllegalArgumentException(format(
                    "More than one type argument of type %s found in class hierarchy of %s",
                    expectedRawType.getName(), baseClass.getName()
            ));
        }

        if (CollectionUtils.isEmpty(resolvedTypes)) {
            return null;
        }

        return resolvedTypes.get(0);
    }

    private static Class<?> findActualTypeArgumentDeclaredOnClass(Class<?> clazz, Class<?> expectedRawType) {
        return Stream.of(ArrayUtils.add(clazz.getGenericInterfaces(), clazz.getGenericSuperclass()))
                .filter(Objects::nonNull)
                .filter(ParameterizedType.class::isInstance)
                .map(ParameterizedType.class::cast)
                .flatMap(type -> Stream.of(type.getActualTypeArguments()))
                .filter(Class.class::isInstance)
                .map(Class.class::cast)
                .filter(expectedRawType::isAssignableFrom)
                .findFirst()
                .orElse(null);
    }


}
