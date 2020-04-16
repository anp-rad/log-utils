package com.aspirecsl.log.aspects;

import java.util.List;
import java.util.Map;

/**
 * Interacts with the Mapped Diagnostic Context <em>(MDC)</em> implementation of a logging framework.
 *
 * @author anoopr
 * @version 1c
 * @since 1c
 */
interface MdcImplementationAware {
    /**
     * Adds the given <tt>key:value</tt> pair to the MDC of the relevant logging framework
     *
     * @param key   the key to add to the MDC.
     * @param value the value to associate with the <tt>key</tt> in the MDC.
     */
    void put(String key, String value);

    /**
     * Adds the contents of the specified <tt>map</tt> to the MDC of the relevant logging framework
     *
     * @param map the map containing the <tt>key:value</tt> pairs to be added to the MDC.
     */
    void putAll(Map<String, String> map);

    /**
     * Removes the values associated with the specified <tt>keys</tt> form the MDC of the logging framework
     *
     * @param keys the keys corresponding to the values to be removed from the MDC of the logging framework
     */
    void removeAll(List<String> keys);
}
