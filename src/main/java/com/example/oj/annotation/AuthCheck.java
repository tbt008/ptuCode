package com.example.oj.annotation;

import com.example.oj.common.Permission;
import org.apache.xmlbeans.UserType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {
        Permission permission() default Permission.ADMINISTRATOR;

}
