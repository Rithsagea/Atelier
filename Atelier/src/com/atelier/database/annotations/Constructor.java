package com.atelier.database.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.atelier.database.Factory;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Constructor {
	public Class<? extends Factory<?>> value();
}
