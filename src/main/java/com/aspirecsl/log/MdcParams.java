package com.aspirecsl.log;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.aspirecsl.log.aspects.MdcAspect;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Holder for <tt>repeatable</tt> <tt>MdcParam</tt> annotations
 * <p>This holder interface is <em>required</em> for defining <tt>repeatable</tt> annotations.
 *
 * @author anoopr
 * @version 1c
 * @see MdcMap
 * @see MdcValue
 * @see MdcParam
 * @see MdcHolder
 * @see MdcAspect
 * @since 1c
 */
@Target({ PARAMETER })
@Retention(RUNTIME)
public @interface MdcParams {
    /**
     * Returns an array of <code>MdcParam</code> annotations.
     * <p>This method is <em>required</em> for defining <tt>repeatable</tt> annotations.
     *
     * @return An array of <code>MdcParam</code> annotations.
     */
    MdcParam[] value();
}
