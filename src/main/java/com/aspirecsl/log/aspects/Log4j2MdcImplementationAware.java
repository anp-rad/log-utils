package com.aspirecsl.log.aspects;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.ThreadContext;

/**
 * Interacts with the Mapped Diagnostic Context <em>(MDC)</em> implementation of the Log4j2 logging framework.
 * <p>This class conforms to the <tt>Singleton</tt> pattern.
 *
 * @author anoopr
 * @version 1c
 * @since 1c
 */
final class Log4j2MdcImplementationAware implements MdcImplementationAware {

    /**
     * The hidden constructor.
     * <p></p>The clients should use the <tt>getInstance()</tt> method to obtain a <em>lazy-loaded</em> <tt>singleton</tt>.
     */
    private Log4j2MdcImplementationAware() {
    }

    /**
     * Returns the <em>lazy-loaded</em> <tt>singleton</tt> instance of this type
     *
     * @return the <em>lazy-loaded</em> <tt>singleton</tt> instance of this type
     */
    static MdcImplementationAware getInstance() {
        return Helper.INSTANCE;
    }

    /**
     * Adds the given <tt>key:value</tt> pair to the MDC of the Log4j2 logging framework
     *
     * @param key   the key to add to the MDC.
     * @param value the value to associate with the <tt>key</tt> in the MDC.
     */
    @Override
    public void put(String key, String value) {
        ThreadContext.put(key, value);
    }

    /**
     * Adds the contents of the specified <tt>map</tt> to the MDC of the Log4j2 logging framework.
     *
     * @param map the map containing the <tt>key:value</tt> pairs to be added to the MDC.
     */
    @Override
    public void putAll(Map<String, String> map) {
        ThreadContext.putAll(map);
    }

    /**
     * Removes the values associated with the specified <tt>keys</tt> form the MDC of the Log4j2 logging framework
     *
     * @param keys the keys corresponding to the values to be removed from the MDC of the Log4j2 logging framework
     */
    public void removeAll(List<String> keys) {
        ThreadContext.removeAll(keys);
    }

    /**
     * A private helper class the holds a <tt>singleton</tt> <tt>Log4j2MdcImplementationAware</tt> instance which is returned to
     * clients via the <tt>Log4j2MdcImplementationAware.getInstance()</tt> method.
     */
    private static class Helper {

        /**
         * The <tt>singleton</tt> <tt>Log4j2MdcImplementationAware</tt> instance
         */
        private static final MdcImplementationAware INSTANCE = new Log4j2MdcImplementationAware();
    }
}
