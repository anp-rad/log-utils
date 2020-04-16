package com.aspirecsl.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.aspirecsl.log.aspects.MdcAspect;

/**
 * Indicates that this object supplies a value to the MDC of the logging framework.
 * <p>The value added to the MDC of the logging framework is the <tt>String</tt> representation of this object returned by
 * its <tt>toString()</tt> method.
 *
 * @author anoopr
 * @version 1c
 * @see MdcMap
 * @see MdcHolder
 * @see MdcParam
 * @see MdcAspect
 * @since 1c
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface MdcValue {
    /**
     * Returns the name of this argument which will be used as the key to associate its value in the MDC.
     *
     * @return the name of this argument which will be used as the key to associate its value in the MDC.
     */
    String name();

    /**
     * Returns <tt>True</tt> is this value is nullable. Otherwise, <tt>False</tt>.
     * <p>This allows clients to specify a property to be added to the MDC even if it is null. For example:-
     * <pre>
     *
     *     When specified as below:
     *
     *    {@literal @Log4j2MdcAware}
     *     public void foo({@literal @MdcValue}(name = "foo", nullable = false) Object foo){
     *         // do something
     *     }
     *
     *     The MDC will have the value of <b>foo</b> stored as "null". If <em>(nullable = false)</em> is not specified with the
     *     annotation then the value of <b>foo</b> won't be added to the MDC.
     * </pre>
     *
     * @return <tt>True</tt> is this value is nullable. Otherwise, <tt>False</tt>.
     */
    boolean nullable() default false;
}
