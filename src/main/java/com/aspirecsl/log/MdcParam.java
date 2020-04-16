package com.aspirecsl.log;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.aspirecsl.log.aspects.MdcAspect;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that an <tt>Object</tt> has <tt>properties</tt> that are added to the MDC of the logging framework.
 * <tt>Properties</tt> can be <tt>fields</tt> or <tt>getter methods</tt>.
 *
 * @author anoopr
 * @version 1c
 * @see MdcMap
 * @see MdcValue
 * @see MdcHolder
 * @see MdcParams
 * @see MdcAspect
 * @since 1c
 */
@Repeatable(MdcParams.class)
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface MdcParam {
    /**
     * Returns the name of the field whose value is to be added to the MDC.
     * <p>Only (and exactly) one of <tt>field()</tt> or <tt>getter()</tt> should be specified
     *
     * @return the name of the field whose value is to be added to the MDC.
     * @see #getter()
     */
    String field() default "";

    /**
     * Returns the name of the <em>getter</em> method that supplies the value to be added to the MDC.
     * <p>Only (and exactly) one of <tt>field()</tt> or <tt>getter()</tt> should be specified
     *
     * @return the name of the <em>getter</em> method that supplies the value to be added to the MDC.
     * @see #field()
     */
    String getter() default "";

    /**
     * Returns a <em>programmer-friendly</em> name that is used as a key for the value stored in the MDC.
     * <p>If not specified, then uses the value from <tt>field()</tt> or <tt>getter()</tt> <em>(whichever is present)</em>
     * as the key for the value stored in the MDC <em>(default behaviour)</em>.
     *
     * @return a <em>programmer-friendly</em> name that is used as a key for the value stored in the MDC.
     */
    String label() default "";
}
