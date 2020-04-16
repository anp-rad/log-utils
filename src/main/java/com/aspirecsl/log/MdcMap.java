package com.aspirecsl.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.aspirecsl.log.aspects.MdcAspect;

/**
 * Indicates that a <tt>Map</tt> supplies to the MDC of the logging framework.
 * <p>The <tt>Map</tt> either supplies to the MDC directly or contains nested <tt>Map</tt>s that supply to the MDC. When the
 * <tt>Map</tt> contains nested <tt>Map</tt>s that supply to the MDC, then {@link #nestedMapKeys()} is used to obtain the
 * keys that are associated with these nested <tt>Map</tt>s.
 *
 * @author anoopr
 * @version 1c
 * @see MdcValue
 * @see MdcHolder
 * @see MdcParam
 * @see MdcAspect
 * @since 1c
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface MdcMap {
    /**
     * Returns an <tt>Array</tt> of keys <em>(case-insensitive)</em> used to filter the values added to the MDC by a client.
     * <p>This allows a client to supply different values to the MDC from different contexts. All the values supplied by a
     * client are added to the MDC when this value is not overridden. <em>(default behaviour)</em>.
     *
     * @return an <tt>Array</tt> containing header parameters from the request map to be added to the log context.
     */
    String[] filter() default {};

    /**
     * Returns the keys associated with the nested maps that act as the suppliers to the MDC of the logging framework.
     * <p>If not overridden then treats the annotated map as the supplier to the MDC of the logging framework
     * <em>(default behaviour)</em>.
     * <p>For example:-
     * <pre>
     *
     *     When specified as below:
     *
     *    {@literal @MDCAware}
     *     public void{@literal foo(@MDCMap(nestedMapKeys = {"this", "that"}) Map<String,String> bar)}{
     *         // do something
     *     }
     *
     *     Then bar.get("this") and bar.get("that") returns the nested maps that supply to the MDC of the logging framework.
     * </pre>
     *
     * @return the keys associated with the nested maps that act as the suppliers to the MDC of the logging framework.
     */
    String[] nestedMapKeys() default {};

    /**
     * Returns a <tt>String</tt> that is prefixed to the keys stored in the MDC of the logging framework.
     * <p>Only override this if there is more than one <tt>Map</tt> parameter annotated with <tt>MdcMap</tt> in a method
     * with duplicate keys for values added to the MDC; failing to do so will result in the MDC capturing only the values
     * in the <tt>Map</tt> that appears later in the method signature for duplicate keys. For example:-
     * <pre>
     *
     *     For a method like below:-
     *
     *    {@literal @Log4j2MdcAware}
     *     public void foo({@literal @MdcMap Map<?,?> one, @MdcMap Map<?,?> two}){
     *         // do something
     *     }
     *
     *     If maps <b>one</b> and <b>two</b> have duplicate keys that are to be added to the MDC, then this will result in the
     *     values associated with the duplicate keys in map <b>two</b> overwrite the ones for the same keys in the map <b>one</b>
     *     when added to the MDC. To address this issue, the clients can use the annotation as below:-
     *
     *    {@literal @Log4j2MdcAware}
     *     public void foo({@literal @MdcMap(mdcKeyPrefix = "map1") Map<?,?> one, @MdcMap(mdcKeyPrefix = "map2") Map<?,?> two}){
     *         // do something
     *     }
     *
     *     This will ensure that the relevant values from the map <b>one</b> are associated with keys prefixed by "map1-", and
     *     the relevant values from the map <b>two</b> are associated with keys prefixed by "map2-" when added to the MDC.
     *
     * </pre>
     * <p>Clients should note that the keys are prefixed when <em>they are added to</em> the MDC. Hence, if <em>filters</em>
     * are applied on the <tt>MdcMap</tt> annotations then the <tt>mdcKeyPrefix</tt> value only need to be specified if there
     * are duplicate keys in the entries filtered to be added to the MDC.
     *
     * @return a <tt>String</tt> that is prefixed to the keys stored in the MDC of the logging framework.
     */
    String mdcKeyPrefix() default "";
}
