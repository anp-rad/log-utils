package com.aspirecsl.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.aspirecsl.log.aspects.MdcAspect;

/**
 * Indicates that a <tt>HasDiagnosticContext</tt> implementation supplies to the MDC of the logging framework.
 *
 * @author anoopr
 * @version 1c
 * @see MdcMap
 * @see MdcValue
 * @see MdcParam
 * @see MdcAspect
 * @see HasDiagnosticContext
 * @since 1c
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface MdcHolder {
    /**
     * Returns an <tt>Array</tt> of keys used to filter the values added to the MDC by a client.
     * <p>This allows a client to supply different values to the MDC from different contexts. All the values supplied by a
     * client are added to the MDC when this value is not overridden. <em>(default behaviour)</em>.
     *
     * @return an <tt>Array</tt> containing header parameters from the request map to be added to the log context.
     */
    String[] filter() default {};

    /**
     * Returns a <tt>String</tt> that is prefixed to the keys stored in the MDC of the logging framework.
     * <p>Only override this if there is more than one <tt>HasDiagnosticContext</tt> object annotated with <tt>MdcHolder</tt>
     * in a method with duplicate keys for values added to the MDC; failing to do so will result in the MDC capturing only the
     * values in the <tt>HasDiagnosticContext</tt> object that appears later in the method signature for duplicate keys.
     * For example:-
     * <pre>
     *
     *     For a method like below:-
     *
     *    {@literal @Log4j2MdcAware}
     *     public void foo({@literal @MdcHolder HasDiagnosticContext one, @MdcHolder HasDiagnosticContext two}){
     *         // do something
     *     }
     *
     *     If objects <b>one</b> and <b>two</b> have duplicate keys that are to be added to the MDC, then this will result in the
     *     values associated with the duplicate keys in object <b>two</b> overwrite the ones for the same keys in the object
     *     <b>one</b> when added to the MDC. To address this issue, the clients can use the annotation as below:-
     *
     *    {@literal @Log4j2MdcAware}
     *     public void foo({@literal @MdcHolder(mdcKeyPrefix = "one") HasDiagnosticContext one, @MdcHolder(mdcKeyPrefix = "two") HasDiagnosticContext two}){
     *         // do something
     *     }
     *
     *     This will ensure that the relevant values from the object <b>one</b> are associated with keys prefixed by "one-", and
     *     the relevant values from the object <b>two</b> are associated with keys prefixed by "two-" when added to the MDC.
     *
     * </pre>
     * <p>Clients are urged to note that the keys are prefixed when <em>they are added to</em> the MDC. Hence, if <em>filters</em>
     * are applied on the <tt>MdcHolder</tt> annotations then the <tt>mdcKeyPrefix</tt> value only need to be specified if there
     * are duplicate keys in the entries filtered to be added to the MDC.
     *
     * @return a <tt>String</tt> that is prefixed to the keys stored in the MDC of the logging framework.
     */
    String mdcKeyPrefix() default "";
}
