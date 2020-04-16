package util;

import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.ThreadContext;

import com.aspirecsl.log.HasDiagnosticContext;
import com.aspirecsl.log.Log4j2MdcAware;
import com.aspirecsl.log.MdcHolder;
import com.aspirecsl.log.MdcMap;
import com.aspirecsl.log.MdcMaps;
import com.aspirecsl.log.MdcParam;
import com.aspirecsl.log.MdcParams;
import com.aspirecsl.log.MdcValue;

/**
 * Uses {@link Log4j2MdcAware} annotation.
 * <p>This class has no <em>real-world</em> use and only exists to facilitate the unit testing of <tt>Log4jMdcAware</tt>,
 * <tt>MdcParams</tt>, <tt>MdcHolder</tt>, <tt>MdcMap</tt>, and <tt>MdcValue</tt> annotations and <tt>MdcAspect</tt> class.
 *
 * @author anoopr
 * @version 1c
 * @since 1c
 */
@SuppressWarnings("unused")
public class Log4j2MdcAwareAnnotationClient {

    /**
     * Holds the MDC from the logging framework to be verified in tests.
     */
    public Map<String, String> mdc = Collections.emptyMap();

    @Log4j2MdcAware
    public void log4j2MdcAwareWithoutParameterAnnotation(Object input) {
        mdc = ThreadContext.getImmutableContext();
    }

    // MdcMap uses

    @Log4j2MdcAware
    public void mdcMapArg(
            @MdcMap
                    Object input) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapWithKeyPrefixArg(
            @MdcMap(mdcKeyPrefix = "custom")
                    Map<?, ?> input) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapWithFilterArg(
            @MdcMap(filter = { "foo-one" })
                    Map<?, ?> one) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapWithFilterArgCaseInsensitive(
            @MdcMap(filter = { "FoO-oNe" })
                    Map<?, ?> one) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapWithNestedMapArg(
            @MdcMap(nestedMapKeys = { "nested2" })
                    Map<?, ?> one) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapWithFilterAndKeyPrefixArg(
            @MdcMap(filter = { "foo-one" }, mdcKeyPrefix = "custom")
                    Map<?, ?> one) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapWithNestedMapAndKeyPrefixArg(
            @MdcMap(nestedMapKeys = { "nested2" }, mdcKeyPrefix = "custom")
                    Map<?, ?> one) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapWithNestedMapAndFilterArg(
            @MdcMap(nestedMapKeys = { "nested1" }, filter = { "nested1-foo-two" })
                    Map<?, ?> one) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapWithNestedMapFilterAndKeyPrefixArg(
            @MdcMap(nestedMapKeys = { "nested1" }, filter = { "nested1-foo-two" }, mdcKeyPrefix = "custom")
                    Map<?, ?> one) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapArgs(
            @MdcMap
                    Map<?, ?> one,
            @MdcMap
                    Map<?, ?> two) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapArgsWithMdcKeyPrefix(
            @MdcMap(mdcKeyPrefix = "one")
                    Map<?, ?> one,
            @MdcMap(mdcKeyPrefix = "two")
                    Map<?, ?> two) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapArgsWithAndWithoutMdcKeyPrefix(
            @MdcMap(mdcKeyPrefix = "custom")
                    Map<?, ?> one,
            @MdcMap
                    Map<?, ?> two) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapWithNestedMapsWithAndWithoutMdcKeyPrefix(
            @MdcMap(nestedMapKeys = { "nested1" }, mdcKeyPrefix = "custom")
                    Map<?, ?> one,
            @MdcMap(nestedMapKeys = { "nested3" })
                    Map<?, ?> two) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapWithAndWithoutFilter(
            @MdcMap(filter = { "foo-one" })
                    Map<?, ?> one,
            @MdcMap
                    Map<?, ?> two) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapWithNestedMapsWithAndWithoutFilter(
            @MdcMap(nestedMapKeys = { "nested1" }, filter = { "nested1-foo-two" })
                    Map<?, ?> one,
            @MdcMap(nestedMapKeys = { "nested4" })
                    Map<?, ?> two) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapArgWithAnotherNonAnnotatedArg(
            @MdcMap
                    Map<?, ?> one,
            Map<?, ?> two) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapWithUnknownNestedMapKey(
            @MdcMap(nestedMapKeys = { "unknown-key" })
                    Map<?, ?> one) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapWithNestedMapAndUnSatisfiedFilterArg(
            @MdcMap(nestedMapKeys = { "nested2" }, filter = { "absent-key" })
                    Map<?, ?> one) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapWithUnSatisfiedFilterArg(
            @MdcMap(filter = { "absent-key" })
                    Map<?, ?> one) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapWrappedInMdcMaps(
            @MdcMaps({ @MdcMap(filter = "this"),
                    @MdcMap(nestedMapKeys = { "nested1" }, filter = { "nested1-foo-two" }) })
                    Map<?, ?> map) {
        mdc = ThreadContext.getImmutableContext();
    }

    public void mdcMapWithoutLog4j2MdcAware(
            @MdcMap
                    Map<?, ?> map) {
        mdc = ThreadContext.getImmutableContext();
    }

    // MdcParam uses

    @Log4j2MdcAware
    public void mdcParamUseWithField(
            @MdcParam(field = "functionName", label = "function-name")
                    Object foo) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcParamUseWithGetter(
            @MdcParam(getter = "getFunctionVersion", label = "function-version")
                    Object foo) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcParamUseWithFieldAndDefaultLabel(
            @MdcParam(field = "functionName")
                    Object foo) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcParamUseWithGetterAndDefaultLabel(
            @MdcParam(getter = "getFunctionVersion")
                    Object foo) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void multiMdcParamUse(
            @MdcParam(field = "functionName", label = "function-name")
            @MdcParam(getter = "getFunctionVersion", label = "function-version")
                    Object foo) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcParamWrappedInMdcParams(
            @MdcParams({ @MdcParam(field = "functionName", label = "function-name"),
                    @MdcParam(getter = "getFunctionVersion", label = "function-version") })
                    Object foo) {
        mdc = ThreadContext.getImmutableContext();
    }

    // MdcValue uses

    @Log4j2MdcAware
    public void mdcValueArg(
            @MdcValue(name = "foo")
                    Object foo) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcValueWithNullabilityArg(
            @MdcValue(name = "foo", nullable = true)
                    Object foo) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcValueArgWithAnotherNonAnnotatedArg(
            @MdcValue(name = "foo")
                    Object foo,
            Object bar) {
        mdc = ThreadContext.getImmutableContext();
    }

    public void mdcValueWithoutLog4j2MdcAware(
            @MdcValue(name = "foo")
                    String bar) {
        mdc = ThreadContext.getImmutableContext();
    }

    // MdcHolder uses

    @Log4j2MdcAware
    public void mdcHolderArg(
            @MdcHolder
                    Object context) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcHolderArgWithFilter(
            @MdcHolder(filter = { "foo-two" })
                    HasDiagnosticContext context) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcHolderArgWithFilterAndMdcKeyPrefix(
            @MdcHolder(filter = { "foo-two" }, mdcKeyPrefix = "custom")
                    HasDiagnosticContext context) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcHolderArgsWithAndWithoutMdcKeyPrefix(
            @MdcHolder(mdcKeyPrefix = "custom")
                    HasDiagnosticContext one,
            @MdcHolder
                    HasDiagnosticContext two) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcHolderArgsWithAndWithoutFilter(
            @MdcHolder(filter = { "foo-two" })
                    HasDiagnosticContext one,
            @MdcHolder
                    HasDiagnosticContext two) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcHolderArgWithAnotherNonAnnotatedArg(
            @MdcHolder
                    HasDiagnosticContext one,
            HasDiagnosticContext two) {
        mdc = ThreadContext.getImmutableContext();
    }

    public void mdcHolderWithoutLog4j2MdcAware(
            @MdcHolder
                    HasDiagnosticContext one) {
        mdc = ThreadContext.getImmutableContext();
    }

    // all annotations used

    @Log4j2MdcAware
    public void allMdcAnnotationsInUse(
            @MdcMap
                    Map<?, ?> map,
            @MdcValue(name = "foo")
                    Object foo,
            @MdcHolder
                    HasDiagnosticContext obj,
            @MdcParams(@MdcParam(field = "functionVersion"))
                    DummyMdcParamsObject object) {
        mdc = ThreadContext.getImmutableContext();
    }

    // illegal MDC annotation uses

    @Log4j2MdcAware
    public void mdcHolderAndMdcValueUsedSimultaneously(
            @MdcHolder
            @MdcValue(name = "foo")
                    Object any) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcHolderAndMdcMapUsedOnSameArg(
            @MdcHolder
            @MdcMap
                    Object any) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcHolderAndMdcParamUsedOnSameArg(
            @MdcHolder
            @MdcParam(field = "functionVersion")
                    Object any) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcHolderAndMdcParamsUsedOnSameArg(
            @MdcHolder
            @MdcParams(@MdcParam(field = "functionVersion"))
                    Object any) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcValueAndMdcMapUsedOnSameArg(
            @MdcValue(name = "foo")
            @MdcMap
                    Object any) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcValueAndMdcParamsUsedOnSameArg(
            @MdcValue(name = "foo")
            @MdcParams(@MdcParam(field = "functionName"))
                    Object any) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcValueAndMdcParamUsedOnSameArg(
            @MdcValue(name = "foo")
            @MdcParam(field = "functionName")
                    Object any) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapAndMdcParamUsedOnSameArg(
            @MdcMap
            @MdcParam(field = "functionName")
                    Object any) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcMapAndMdcParamsUsedOnSameArg(
            @MdcMap
            @MdcParams(@MdcParam(field = "functionName"))
                    Object any) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcParamsAndMdcParamUsedOnTheSameArg(
            @MdcParams(@MdcParam(field = "bar"))
            @MdcParam(field = "foo")
                    Object foo) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcParamUseWithoutFieldOrGetter(
            @MdcParam
                    Object foo) {
        mdc = ThreadContext.getImmutableContext();
    }

    @Log4j2MdcAware
    public void mdcParamUseWithFieldAndGetter(
            @MdcParam(field = "functionName", getter = "getFunctionName")
                    Object foo) {
        mdc = ThreadContext.getImmutableContext();
    }
}
