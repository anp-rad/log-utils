package com.aspirecsl.log;

import java.util.Map;

import com.aspirecsl.log.aspects.MdcAspect;

/**
 * Holds elements that can be added to the Mapped Diagnostic Context <em>(MDC)</em> of a logging framework.
 * <p>Mapped Diagnostic Context helps in distinguishing interleaved log outputs from different sources. MDC is supported by many
 * popular logging frameworks like Log4j, Log4j2 and Logback. Since MDC is built into these frameworks values added to it are
 * easily accessed by the appender and produced in the log output. For more information on MDC implementations please see
 * <a href=http://logback.qos.ch/manual/mdc.html>Logback</a> and
 * <a href=https://logging.apache.org/log4j/2.x/manual/thread-context.html>Log4j2</a> manuals.
 * <p>Implementations of this type must expose the relevant elements to be added to the logging framework's MDC via a
 * <tt>java.util.Map</tt> object by overriding the {@link #mappedDiagnosticContext()} method.
 *
 * @author anoopr
 * @version 1c
 * @see Log4j2MdcAware
 * @see MdcAspect
 * @since 1c
 */
@FunctionalInterface
public interface HasDiagnosticContext {

    /**
     * Returns a <tt>Map</tt> containing <em>key:value</em> pairs that can be added to the MDC of a logging framework
     *
     * @return a <tt>Map</tt> containing <em>key:value</em> pairs that can be added to the MDC of a logging framework
     */
    Map<String, String> mappedDiagnosticContext();
}
