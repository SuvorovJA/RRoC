package ru.sua.rroc.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * аннотация-маркер применимая на методах REST Controller для инициирования конвертации
 * {@code ResponseEntity<T>}  в {@code ResponseEntity<A>}, где А - класс указанный в параметре аннотации
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dto {
    Class<?> value();
}