package com.aspirecsl.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.aspirecsl.log.aspects.MdcAspect;

/**
 * Indicates that a method is aware of the Mapped Diagnostic Context <em>(MDC)</em> of the Slf4j logging framework.
 * <p>This is an <em>indicative</em> annotation and using this alone will not add anything to the MDC. Users should use one of
 * the <tt>MdcMap, MdcValue, MdcHolder or MdcLambda</tt> annotations on the formal parameters of a <tt>MDCAware</tt> annotated
 * method for them <em>(or their properties)</em> to be added to the MDC of the Slf4j logging framework.
 *
 * @author anoopr
 * @version 1c
 * @see MdcMap
 * @see MdcValue
 * @see MdcHolder
 * @see MdcParam
 * @see MdcAspect
 * @see HasDiagnosticContext
 * @since 1c
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Slf4jMdcAware {
}
