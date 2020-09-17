/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.function.context.catalog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

import org.springframework.cloud.function.context.FunctionRegistration;
import org.springframework.cloud.function.context.catalog.SimpleFunctionRegistry.FunctionInvocationWrapper;
import org.springframework.cloud.function.context.config.RoutingFunction;

import net.jodah.typetools.TypeResolver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Dave Syer
 * @author Oleg Zhurakousky
 */
public interface FunctionInspector {

	FunctionRegistration<?> getRegistration(Object function);

	default boolean isMessage(Object function) {
		if (function == null) {
			return false;
		}

		return ((FunctionInvocationWrapper) function).isInputTypeMessage();



//		FunctionRegistration<?> registration = getRegistration(function);
//		if (registration != null && registration.getTarget() instanceof RoutingFunction) {
//			return true;
//		}
//		return registration == null ? false : registration.getType().isMessage();
	}

	default Class<?> getInputType(Object function) {
		if (function == null) {
			return Object.class;
		}
		Type type = ((FunctionInvocationWrapper) function).getInputType();
		Class<?> inputType;
		if (type instanceof ParameterizedType) {
			if (function != null && (((FunctionInvocationWrapper) function).isInputTypePublisher() || ((FunctionInvocationWrapper) function).isInputTypeMessage())) {
				inputType = TypeResolver.resolveRawClass(FunctionTypeUtils.getImmediateGenericType(type, 0), null);
			}
			else {
				inputType = ((FunctionInvocationWrapper) function).getRawInputType();
			}
		}
		else {
			inputType = (Class<?>) type;
		}
		return inputType;
//		return String.class;
//		FunctionRegistration<?> registration = getRegistration(function);
//		return registration == null ? Object.class
//				: registration.getType().getInputType();
	}

	default Class<?> getOutputType(Object function) {
//		Creturn function == null ? Object.class : ((FunctionInvocationWrapper) function).getRawOutputType();
		if (function == null) {
			return Object.class;
		}
		Type type = ((FunctionInvocationWrapper) function).getOutputType();
		Class<?> outputType;
		if (type instanceof ParameterizedType) {
			if (function != null && ((FunctionInvocationWrapper) function).isOutputTypePublisher() || ((FunctionInvocationWrapper) function).isOutputTypeMessage()) {
				outputType = TypeResolver.resolveRawClass(FunctionTypeUtils.getImmediateGenericType(type, 0), null);
			}
			else {
				outputType = ((FunctionInvocationWrapper) function).getRawOutputType();
			}
		}
		else {
			outputType = (Class<?>) type;
		}
		return outputType;
//		return String.class;



//		FunctionRegistration<?> registration = getRegistration(function);
//		return registration == null ? Object.class
//				: registration.getType().getOutputType();
	}

	default Class<?> getInputWrapper(Object function) {
		Class c = function == null ? Object.class : TypeResolver.resolveRawClass(((FunctionInvocationWrapper) function).getInputType(), null);
		if (Flux.class.isAssignableFrom(c)) {
			return c;
		}
		else if (Mono.class.isAssignableFrom(c)) {
			return c;
		}
		else {
			return this.getInputType(function);
		}
		//		FunctionRegistration<?> registration = getRegistration(function);
//		return registration == null ? Object.class
//				: registration.getType().getInputWrapper();
	}

	default Class<?> getOutputWrapper(Object function) {
		Class c  = function == null ? Object.class : TypeResolver.resolveRawClass(((FunctionInvocationWrapper) function).getOutputType(), null);
		if (Flux.class.isAssignableFrom(c)) {
			return c;
		}
		else if (Mono.class.isAssignableFrom(c)) {
			return c;
		}
		else {
			return this.getOutputType(function);
		}
//		FunctionRegistration<?> registration = getRegistration(function);
//		return registration == null ? Object.class
//				: registration.getType().getOutputWrapper();
	}

	default String getName(Object function) {
		return ((FunctionInvocationWrapper) function).getFunctionDefinition();
//		FunctionRegistration<?> registration = getRegistration(function);
//		Set<String> names = registration == null ? Collections.emptySet()
//				: registration.getNames();
//		return names.isEmpty() ? null : names.iterator().next();
	}

}
