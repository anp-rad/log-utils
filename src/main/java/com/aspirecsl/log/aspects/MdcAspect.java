package com.aspirecsl.log.aspects;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import com.aspirecsl.log.HasDiagnosticContext;
import com.aspirecsl.log.Log4j2MdcAware;
import com.aspirecsl.log.MdcHolder;
import com.aspirecsl.log.MdcMap;
import com.aspirecsl.log.MdcMaps;
import com.aspirecsl.log.MdcParam;
import com.aspirecsl.log.MdcParams;
import com.aspirecsl.log.MdcValue;
import com.aspirecsl.log.Slf4jMdcAware;

/**
 * Provides <b>cross-cutting</b> advice to populate the MDC of the logging framework in the relevant methods.
 *
 * @author anoopr
 * @version 1c
 * @see Log4j2MdcAware
 * @see HasDiagnosticContext
 * @since 1c
 */
@Aspect
public class MdcAspect {

    /**
     * Annotations for the method parameters that are added to the MDC of a logging framework.
     */
    private static final List<Class<?>> MDC_ANNOTATIONS =
            Arrays.asList(MdcMap.class, MdcMaps.class, MdcValue.class, MdcHolder.class, MdcParam.class, MdcParams.class);

    /**
     * <tt>Pointcut</tt> describing a <tt>Log4j2MdcAware</tt> annotated method.
     *
     * @see Log4j2MdcAware
     */
    @Pointcut("@annotation(com.aspirecsl.log.Log4j2MdcAware) && execution(* *(..))")
    public void log4j2MdcAwareAnnotatedMethod() {
    }

    /**
     * <tt>Pointcut</tt> describing a <tt>Slf4jMdcAware</tt> annotated method.
     *
     * @see Slf4jMdcAware
     */
    @Pointcut("@annotation(com.aspirecsl.log.Slf4jMdcAware) && execution(* *(..))")
    public void slf4jMdcAwareAnnotatedMethod() {
    }

    /**
     * <tt>Cross-cutting</tt> concerns that are implemented around a <tt>Log4j2MdcAware</tt> annotated method.
     * <p>This <tt>cross-cutting</tt> advice encompasses the following steps:-
     * <ol>
     *     <li>add method parameters <em>(if annotated correctly)</em> to the MDC of the logging framework.</li>
     * </ol>
     *
     * @param pjp the join-point object holding the <em>advised</em> method's state and static information.
     * @return the return value of the <em>advised</em> method.
     * @throws Throwable                if the method invoked while calling <tt>pjp.proceed(...)</tt> throws an exception.
     * @throws IllegalArgumentException if the annotation is incompatible with the parameter type that it annotates.
     */
    @Around("log4j2MdcAwareAnnotatedMethod()")
    public Object log4j2MdcAwareMethodAdvice(ProceedingJoinPoint pjp) throws Throwable {
        return addToMdcIfApplicable(pjp, Log4j2MdcImplementationAware.getInstance());
    }

    /**
     * <tt>Cross-cutting</tt> concerns that are implemented around a <tt>Slf4jMdcAware</tt> annotated method.
     * <p>This <tt>cross-cutting</tt> advice encompasses the following steps:-
     * <ol>
     *     <li>add method parameters <em>(if annotated correctly)</em> to the MDC of the logging framework.</li>
     * </ol>
     *
     * @param pjp the join-point object holding the <em>advised</em> method's state and static information.
     * @return the return value of the <em>advised</em> method.
     * @throws Throwable                if the method invoked while calling <tt>pjp.proceed(...)</tt> throws an exception.
     * @throws IllegalArgumentException if the annotation is incompatible with the parameter type that it annotates.
     */
    @Around("slf4jMdcAwareAnnotatedMethod()")
    public Object slf4jMdcAwareMethodAdvice(ProceedingJoinPoint pjp) throws Throwable {
        return addToMdcIfApplicable(pjp, Slf4jMdcImplementationAware.getInstance());
    }

    /**
     * Adds relevant method parameters <em>(if annotated correctly)</em> to the MDC of the logging framework.
     * <p>Additionally, removes the values added by this <tt>AspectJ</tt> advice once the <em>advised</em> method
     * completes execution. This ensures that no stale properties are left behind in the MDC by this advice.
     *
     * @param pjp    the join-point object holding the <em>advised</em> method's state and static information.
     * @param mdcApi the API for the MDC implementation of the logging framework in use.
     * @return the return value of the <em>advised</em> method.
     * @throws Throwable                if the method invoked while calling <tt>pjp.proceed(...)</tt> throws an exception.
     * @throws IllegalArgumentException if the annotation is incompatible with the parameter type that it annotates,
     *                                  or the client specifies more than one type of MDC annotation on a parameter. Full list of
     *                                  MDC parameter annotations can be found on {@link #MDC_ANNOTATIONS}.
     */
    private Object addToMdcIfApplicable(ProceedingJoinPoint pjp, MdcImplementationAware mdcApi) throws Throwable {
        final List<String> mdcKeys = new ArrayList<>();
        try {
            final Object[] args = pjp.getArgs();
            final MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
            final Method method = methodSignature.getMethod();
            final Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                final Object value = args[i];
                final Parameter parameter = parameters[i];

                final Set<? extends Class<? extends Annotation>> mdcAnnotationSet =
                        Arrays.stream(parameter.getAnnotations())
                                .map(Annotation::annotationType)
                                .filter(MDC_ANNOTATIONS::contains)
                                .collect(Collectors.toSet());
                if (mdcAnnotationSet.size() > 1) {
                    throw new IllegalArgumentException("MDC parameter annotations are mutually exclusive.");
                }
                mdcKeys.addAll(processMdcMapsIfApplicable(parameter, value, mdcApi));
                mdcKeys.addAll(processMdcValueIfApplicable(parameter, value, mdcApi));
                mdcKeys.addAll(processMdcHolderIfApplicable(parameter, value, mdcApi));
                mdcKeys.addAll(processMdcParamsIfApplicable(parameter, value, mdcApi));
            }
            return pjp.proceed();
        } finally {
            // remove the values added to MDC by this advice; irrespective of the completion status of the service
            if (!mdcKeys.isEmpty()) {
                mdcApi.removeAll(mdcKeys);
            }
        }
    }

    /**
     * Adds relevant values from a parameter of type <tt>HasDiagnosticContext</tt> to the MDC of the logging framework.
     *
     * @param parameter the <tt>Parameter</tt> object from the <em>advised</em> method's formal arguments list.
     * @param value     the actual value of the argument in the <em>advised</em> method's execution.
     * @param mdcApi    the API for the MDC implementation of the logging framework in use.
     * @return A <tt>List</tt> containing the keys corresponding to the values added to the MDC; or an empty list if no values
     * were added to the MDC.
     * @throws IllegalArgumentException if the <tt>MdcHolder</tt> annotated argument <em>(if present)</em> is not of type
     *                                  <tt>HasDiagnosticContext</tt>
     * @see MdcHolder
     * @see HasDiagnosticContext
     */
    private List<String> processMdcHolderIfApplicable(Parameter parameter, Object value, MdcImplementationAware mdcApi) {
        final MdcHolder annotation = parameter.getAnnotation(MdcHolder.class);
        if (value != null && annotation != null) {
            if (HasDiagnosticContext.class.isAssignableFrom(value.getClass())) {
                final Map<String, String> mdc = ((HasDiagnosticContext) value).mappedDiagnosticContext();
                return addToMdcApplyingFilter(mdcApi, mdc, annotation.filter(), annotation.mdcKeyPrefix());
            } else {
                throw new IllegalArgumentException(
                        "@MdcHolder is allowed only on com.aspirecsl.log.utils.HasDiagnosticContext objects.");
            }
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Adds a parameter value to the MDC of the logging framework.
     *
     * @param parameter the <tt>Parameter</tt> object from the <em>advised</em> method's formal arguments list.
     * @param value     the actual value of the argument in the <em>advised</em> method's execution.
     * @param mdcApi    the API for the MDC implementation of the logging framework in use.
     * @return A <tt>List</tt> containing the keys corresponding to the values added to the MDC; or an empty list if no values
     * were added to the MDC.
     * @see MdcValue
     */
    private List<String> processMdcValueIfApplicable(Parameter parameter, Object value, MdcImplementationAware mdcApi) {
        final MdcValue annotation = parameter.getAnnotation(MdcValue.class);
        if (annotation != null && (value != null || annotation.nullable())) {
            mdcApi.put(annotation.name(), String.valueOf(value));
            return Collections.singletonList(annotation.name());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Adds the contents of a <tt>Map</tt> to the MDC of the logging framework as per the specified <tt>MdcMap</tt> annotation.
     * <p>If the holder annotation, <tt>MdcMaps</tt>, is used on the <tt>parameter</tt>, then adds properties specified by each
     * of the held <tt>MdcMap</tt> annotation. If neither <tt>MdcMap</tt> nor <tt>MdcMaps</tt> is used on the
     * <tt>parameter</tt>, or the parameter <tt>value</tt> is <tt>null</tt>, then this method does not add anything to the MDC,
     * and returns an empty list.
     *
     * @param parameter the <tt>Parameter</tt> object from the <em>advised</em> method's formal arguments list.
     * @param value     the actual value of the argument in the <em>advised</em> method's execution.
     * @param mdcApi    the API for the MDC implementation of the logging framework in use.
     * @return A <tt>List</tt> containing the keys corresponding to the values added to the MDC; or an empty list if no values
     * were added to the MDC.
     * @throws IllegalArgumentException if the <tt>MdcMap</tt> annotated argument <em>(if present)</em> is not of type
     *                                  {@literal Map<String,String>}; or a key specified in the <tt>nestedMapKeys</tt> of the <tt>MdcMap</tt> annotation is
     *                                  associated with a value that is not of type {@literal Map<String,String>}.
     * @see MdcMap
     */
    private List<String> processMdcMapsIfApplicable(Parameter parameter, Object value, MdcImplementationAware mdcApi) {
        if (value == null) {
            return Collections.emptyList();
        }
        final MdcMap mdcMapAnnotation = parameter.getAnnotation(MdcMap.class);
        if (mdcMapAnnotation != null) {
            return addMdcMap(value, mdcApi, mdcMapAnnotation);
        } else {
            final MdcMaps mdcMaps = parameter.getAnnotation(MdcMaps.class);
            if (mdcMaps != null) {
                return Arrays.stream(mdcMaps.value())
                        .map(mdcMap -> addMdcMap(value, mdcApi, mdcMap))
                        .reduce(Collections.emptyList(), (a, b) -> {
                            final List<String> result = new ArrayList<>(a);
                            result.addAll(b);
                            return result;
                        });
            } else {
                return Collections.emptyList();
            }
        }
    }

    /**
     * Adds the contents of a <tt>Map</tt> to the MDC of the logging framework as per the specified <tt>mdcMap</tt> annotation.
     *
     * @param value  the actual value of the argument in the <em>advised</em> method's execution.
     * @param mdcApi the API for the MDC implementation of the logging framework in use.
     * @param mdcMap the annotation specifying how the contents of the map are added to the MDC
     * @return A <tt>List</tt> containing the keys corresponding to the values added to the MDC; or an empty list if no values
     * were added to the MDC.
     * @throws IllegalArgumentException if the <tt>MdcMap</tt> annotated argument <em>(if present)</em> is not of type
     *                                  {@literal Map<String,String>}; or a key specified in the <tt>nestedMapKeys</tt> of the <tt>MdcMap</tt> annotation is
     *                                  associated with a value that is not of type {@literal Map<String,String>}.
     * @see MdcMap
     */
    private List<String> addMdcMap(Object value, MdcImplementationAware mdcApi, MdcMap mdcMap) {
        final String[] nestedMapKeys = mdcMap.nestedMapKeys();
        try {
            // un-safe casts will throw a runtime exception
            @SuppressWarnings("unchecked") final Map<String, String> mdc =
                    nestedMapKeys.length == 0
                            ? (Map<String, String>) value
                            : Arrays.stream(nestedMapKeys)
                            .map(nestedMapKey -> ((Map<String, String>) ((Map<String, Object>) value)
                                    .get(nestedMapKey)))
                            .filter(Objects::nonNull)
                            .reduce(Collections.emptyMap(), (a, b) -> {
                                final Map<String, String> result = new HashMap<>();
                                result.putAll(a);
                                result.putAll(b);
                                return result;
                            });
            return addToMdcApplyingFilter(mdcApi, mdc, mdcMap.filter(), mdcMap.mdcKeyPrefix());
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("@MdcMap is allowed only on Map<String,String> objects. \n"
                    + "@MdcMap(nestedMapKeys = {\"this\"}) is allowed only on Map objects that return a "
                    + "Map<String,String> nested map when outerMap.get(\"this\") is called.");
        }
    }

    /**
     * Adds the <tt>properties</tt> specified by the <tt>MdcParam</tt> annotation from the given <tt>value</tt> to the MDC of
     * the logging framework. <tt>Properties</tt> can be specified via <tt>fields</tt> or <tt>getter methods</tt>.
     * <p>If the holder annotation, <tt>MdcParams</tt>, is used on the <tt>parameter</tt>, then adds properties specified by each
     * of the held <tt>MdcParam</tt> annotation. If neither <tt>MdcParam</tt> nor <tt>MdcParams</tt> is used on the
     * <tt>parameter</tt>, or the parameter <tt>value</tt> is <tt>null</tt>, then this method does not add anything to the MDC,
     * and returns an empty list.
     *
     * @param parameter the <tt>Parameter</tt> object from the <em>advised</em> method's formal arguments list.
     * @param value     the actual value of the argument in the <em>advised</em> method's execution.
     * @param mdcApi    the API for the MDC implementation of the logging framework in use.
     * @return A <tt>List</tt> containing the keys corresponding to the values added to the MDC; or an empty list if no values
     * were added to the MDC.
     * @see MdcParam
     * @see MdcParams
     */
    private List<String> processMdcParamsIfApplicable(Parameter parameter, Object value, MdcImplementationAware mdcApi) {
        if (value == null) {
            return Collections.emptyList();
        }
        final MdcParam mdcParamAnnotation = parameter.getAnnotation(MdcParam.class);
        if (mdcParamAnnotation != null) {
            return reflectivelyAddFieldOrMethodResultToMdc(value, mdcApi, mdcParamAnnotation);
        } else {
            final MdcParams mdcParams = parameter.getAnnotation(MdcParams.class);
            if (mdcParams != null) {
                return Arrays.stream(mdcParams.value())
                        .map(mdcParam -> reflectivelyAddFieldOrMethodResultToMdc(value, mdcApi, mdcParam))
                        .reduce(Collections.emptyList(), (a, b) -> {
                            final List<String> result = new ArrayList<>(a);
                            result.addAll(b);
                            return result;
                        });
            } else {
                return Collections.emptyList();
            }
        }
    }

    /**
     * Adds the <tt>properties</tt> specified by the <tt>MdcParam</tt> annotation from the given <tt>value</tt> to the MDC of
     * the logging framework. <tt>Properties</tt> can be specified via <tt>fields</tt> or <tt>getter methods</tt>.
     *
     * @param value    the actual value of the argument in the <em>advised</em> method's execution.
     * @param mdcApi   the API for the MDC implementation of the logging framework in use.
     * @param mdcParam the annotation specifying the source for the value to be added to the MDC
     * @return A <tt>List</tt> containing the keys corresponding to the values added to the MDC; or an empty list if no values
     * were added to the MDC.
     * @throws IllegalArgumentException if <tt>MdcParam</tt> does not specify either <tt>field</tt> or <tt>getter</tt> values,
     *                                  or if it specifies both <tt>field</tt> and <tt>getter</tt> values.
     * @see MdcParam
     */
    private List<String> reflectivelyAddFieldOrMethodResultToMdc(Object value, MdcImplementationAware mdcApi, MdcParam mdcParam) {
        if (mdcParam.field().isEmpty() && mdcParam.getter().isEmpty()) {
            throw new IllegalArgumentException("Either field() or getter() should be specified.");
        }
        if (!(mdcParam.field().isEmpty() || mdcParam.getter().isEmpty())) {
            throw new IllegalArgumentException("Exactly one of field() or getter() should be specified.");
        }

        final List<String> mdcKeys = new ArrayList<>();

        if (!mdcParam.field().isEmpty()) {
            final String mdcKey = mdcParam.label().isEmpty() ? mdcParam.field() : mdcParam.label();
            Arrays.stream(value.getClass().getDeclaredFields())
                    .filter(field -> field.getName().equals(mdcParam.field()))
                    .findFirst()
                    .ifPresent(field -> {
                        field.setAccessible(true);
                        try {
                            mdcApi.put(mdcKey, "" + field.get(value));
                            mdcKeys.add(mdcKey);
                        } catch (IllegalAccessException ignore) {
                        }
                    });
        } else if (!mdcParam.getter().isEmpty()) {
            final String mdcKey = mdcParam.label().isEmpty() ? mdcParam.getter() : mdcParam.label();
            Arrays.stream(value.getClass().getDeclaredMethods())
                    .filter(method -> method.getName().equals(mdcParam.getter()))
                    .findFirst()
                    .ifPresent(method -> {
                        method.setAccessible(true);
                        try {
                            mdcApi.put(mdcKey, "" + method.invoke(value));
                            mdcKeys.add(mdcKey);
                        } catch (InvocationTargetException | IllegalAccessException ignore) {
                        }
                    });
        }
        return mdcKeys;
    }

    /**
     * Adds relevant entries from the <tt>inputMap</tt> <em>(after applying the specified <tt>filter</tt>)</em> into the MDC.
     *
     * @param mdcApi       the API for the MDC implementation of the logging framework in use.
     * @param inputMap     the <tt>Map</tt> containing values added to the MDC.
     * @param filter       the <em>case-insensitive</em> filters to select relevant values from the <tt>inputMap</tt>
     * @param mdcKeyPrefix the <tt>String</tt> prefixed to the keys before associating a value with them in the MDC.
     * @return A <tt>List</tt> containing the keys corresponding to the values added to the MDC; or an empty list if no values
     * were added to the MDC.
     */
    private List<String> addToMdcApplyingFilter(MdcImplementationAware mdcApi, Map<String, String> inputMap, String[] filter,
            String mdcKeyPrefix) {
        return inputMap.entrySet()
                .stream()
                .filter(entry ->
                        filter.length == 0
                                || Arrays.stream(filter).anyMatch(e -> e.equalsIgnoreCase(entry.getKey())))
                .map(entry -> {
                    final String key =
                            mdcKeyPrefix.isEmpty()
                                    ? entry.getKey()
                                    : mdcKeyPrefix + "-" + entry.getKey();
                    mdcApi.put(key, entry.getValue());
                    return key;
                })
                .collect(Collectors.toList());
    }
}
