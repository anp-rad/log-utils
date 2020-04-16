package util;

import java.util.Collections;
import java.util.Map;

import org.slf4j.MDC;

import com.aspirecsl.log.HasDiagnosticContext;
import com.aspirecsl.log.MdcHolder;
import com.aspirecsl.log.MdcMap;
import com.aspirecsl.log.MdcMaps;
import com.aspirecsl.log.MdcParam;
import com.aspirecsl.log.MdcParams;
import com.aspirecsl.log.MdcValue;
import com.aspirecsl.log.Slf4jMdcAware;

/**
 * Uses {@link Slf4jMdcAware} annotation.
 * <p>This class has no <em>real-world</em> use and only exists to facilitate the unit testing of <tt>Slf4jMdcAware</tt>,
 * <tt>MdcParams</tt>, <tt>MdcHolder</tt>, <tt>MdcMap</tt>, and <tt>MdcValue</tt> annotations and <tt>MdcAspect</tt> class.
 *
 * @author anoopr
 * @version 1c
 * @since 1c
 */
@SuppressWarnings("unused")
public class Slf4jMdcAwareAnnotationClient {

    /**
     * Holds the MDC from the logging framework to be verified in tests.
     */
    public Map<String, String> mdc = Collections.emptyMap();

    @Slf4jMdcAware
    public void slf4jMdcAwareWithoutParameterAnnotation(Object input) {
        mdc = MDC.getCopyOfContextMap();
    }

    // MdcMap uses

    @Slf4jMdcAware
    public void mdcMapArg(
            @MdcMap
                    Object input) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapWithKeyPrefixArg(
            @MdcMap(mdcKeyPrefix = "custom")
                    Map<?, ?> input) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapWithFilterArg(
            @MdcMap(filter = { "foo-one" })
                    Map<?, ?> one) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapWithFilterArgCaseInsensitive(
            @MdcMap(filter = { "FoO-oNe" })
                    Map<?, ?> one) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapWithNestedMapArg(
            @MdcMap(nestedMapKeys = { "nested2" })
                    Map<?, ?> one) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapWithFilterAndKeyPrefixArg(
            @MdcMap(filter = { "foo-one" }, mdcKeyPrefix = "custom")
                    Map<?, ?> one) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapWithNestedMapAndKeyPrefixArg(
            @MdcMap(nestedMapKeys = { "nested2" }, mdcKeyPrefix = "custom")
                    Map<?, ?> one) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapWithNestedMapAndFilterArg(
            @MdcMap(nestedMapKeys = { "nested1" }, filter = { "nested1-foo-two" })
                    Map<?, ?> one) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapWithNestedMapFilterAndKeyPrefixArg(
            @MdcMap(nestedMapKeys = { "nested1" }, filter = { "nested1-foo-two" }, mdcKeyPrefix = "custom")
                    Map<?, ?> one) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapArgs(
            @MdcMap
                    Map<?, ?> one,
            @MdcMap
                    Map<?, ?> two) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapArgsWithMdcKeyPrefix(
            @MdcMap(mdcKeyPrefix = "one")
                    Map<?, ?> one,
            @MdcMap(mdcKeyPrefix = "two")
                    Map<?, ?> two) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapArgsWithAndWithoutMdcKeyPrefix(
            @MdcMap(mdcKeyPrefix = "custom")
                    Map<?, ?> one,
            @MdcMap
                    Map<?, ?> two) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapWithNestedMapsWithAndWithoutMdcKeyPrefix(
            @MdcMap(nestedMapKeys = { "nested1" }, mdcKeyPrefix = "custom")
                    Map<?, ?> one,
            @MdcMap(nestedMapKeys = { "nested3" })
                    Map<?, ?> two) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapWithAndWithoutFilter(
            @MdcMap(filter = { "foo-one" })
                    Map<?, ?> one,
            @MdcMap
                    Map<?, ?> two) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapWithNestedMapsWithAndWithoutFilter(
            @MdcMap(nestedMapKeys = { "nested1" }, filter = { "nested1-foo-two" })
                    Map<?, ?> one,
            @MdcMap(nestedMapKeys = { "nested4" })
                    Map<?, ?> two) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapArgWithAnotherNonAnnotatedArg(
            @MdcMap
                    Map<?, ?> one,
            Map<?, ?> two) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapWithUnknownNestedMapKey(
            @MdcMap(nestedMapKeys = { "unknown-key" })
                    Map<?, ?> one) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapWithNestedMapAndUnSatisfiedFilterArg(
            @MdcMap(nestedMapKeys = { "nested2" }, filter = { "absent-key" })
                    Map<?, ?> one) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapWithUnSatisfiedFilterArg(
            @MdcMap(filter = { "absent-key" })
                    Map<?, ?> one) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapWrappedInMdcMaps(
            @MdcMaps({ @MdcMap(filter = "this"),
                    @MdcMap(nestedMapKeys = { "nested1" }, filter = { "nested1-foo-two" }) })
                    Map<?, ?> map) {
        mdc = MDC.getCopyOfContextMap();
    }

    public void mdcMapWithoutSlf4jMdcAware(
            @MdcMap
                    Map<?, ?> map) {
        mdc = MDC.getCopyOfContextMap();
    }

    // MdcParam uses

    @Slf4jMdcAware
    public void mdcParamUseWithField(
            @MdcParam(field = "functionName", label = "function-name")
                    Object foo) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcParamUseWithGetter(
            @MdcParam(getter = "getFunctionVersion", label = "function-version")
                    Object foo) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcParamUseWithFieldAndDefaultLabel(
            @MdcParam(field = "functionName")
                    Object foo) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcParamUseWithGetterAndDefaultLabel(
            @MdcParam(getter = "getFunctionVersion")
                    Object foo) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void multiMdcParamUse(
            @MdcParam(field = "functionName", label = "function-name")
            @MdcParam(getter = "getFunctionVersion", label = "function-version")
                    Object foo) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcParamWrappedInMdcParams(
            @MdcParams({ @MdcParam(field = "functionName", label = "function-name"),
                    @MdcParam(getter = "getFunctionVersion", label = "function-version") })
                    Object foo) {
        mdc = MDC.getCopyOfContextMap();
    }

    // MdcValue uses

    @Slf4jMdcAware
    public void mdcValueArg(
            @MdcValue(name = "foo")
                    Object foo) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcValueWithNullabilityArg(
            @MdcValue(name = "foo", nullable = true)
                    Object foo) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcValueArgWithAnotherNonAnnotatedArg(
            @MdcValue(name = "foo")
                    Object foo,
            Object bar) {
        mdc = MDC.getCopyOfContextMap();
    }

    public void mdcValueWithoutSlf4jMdcAware(
            @MdcValue(name = "foo")
                    String bar) {
        mdc = MDC.getCopyOfContextMap();
    }

    // MdcHolder uses

    @Slf4jMdcAware
    public void mdcHolderArg(
            @MdcHolder
                    Object context) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcHolderArgWithFilter(
            @MdcHolder(filter = { "foo-two" })
                    HasDiagnosticContext context) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcHolderArgWithFilterAndMdcKeyPrefix(
            @MdcHolder(filter = { "foo-two" }, mdcKeyPrefix = "custom")
                    HasDiagnosticContext context) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcHolderArgsWithAndWithoutMdcKeyPrefix(
            @MdcHolder(mdcKeyPrefix = "custom")
                    HasDiagnosticContext one,
            @MdcHolder
                    HasDiagnosticContext two) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcHolderArgsWithAndWithoutFilter(
            @MdcHolder(filter = { "foo-two" })
                    HasDiagnosticContext one,
            @MdcHolder
                    HasDiagnosticContext two) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcHolderArgWithAnotherNonAnnotatedArg(
            @MdcHolder
                    HasDiagnosticContext one,
            HasDiagnosticContext two) {
        mdc = MDC.getCopyOfContextMap();
    }

    public void mdcHolderWithoutSlf4jMdcAware(
            @MdcHolder
                    HasDiagnosticContext one) {
        mdc = MDC.getCopyOfContextMap();
    }

    // all annotations used

    @Slf4jMdcAware
    public void allMdcAnnotationsInUse(
            @MdcMap
                    Map<?, ?> map,
            @MdcValue(name = "foo")
                    Object foo,
            @MdcHolder
                    HasDiagnosticContext obj,
            @MdcParams(@MdcParam(field = "functionVersion"))
                    DummyMdcParamsObject object) {
        mdc = MDC.getCopyOfContextMap();
    }

    // illegal MDC annotation uses

    @Slf4jMdcAware
    public void mdcHolderAndMdcValueUsedSimultaneously(
            @MdcHolder
            @MdcValue(name = "foo")
                    Object any) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcHolderAndMdcMapUsedOnSameArg(
            @MdcHolder
            @MdcMap
                    Object any) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcHolderAndMdcParamUsedOnSameArg(
            @MdcHolder
            @MdcParam(field = "functionVersion")
                    Object any) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcHolderAndMdcParamsUsedOnSameArg(
            @MdcHolder
            @MdcParams(@MdcParam(field = "functionVersion"))
                    Object any) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcValueAndMdcMapUsedOnSameArg(
            @MdcValue(name = "foo")
            @MdcMap
                    Object any) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcValueAndMdcParamsUsedOnSameArg(
            @MdcValue(name = "foo")
            @MdcParams(@MdcParam(field = "functionName"))
                    Object any) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcValueAndMdcParamUsedOnSameArg(
            @MdcValue(name = "foo")
            @MdcParam(field = "functionName")
                    Object any) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapAndMdcParamUsedOnSameArg(
            @MdcMap
            @MdcParam(field = "functionName")
                    Object any) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcMapAndMdcParamsUsedOnSameArg(
            @MdcMap
            @MdcParams(@MdcParam(field = "functionName"))
                    Object any) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcParamsAndMdcParamUsedOnTheSameArg(
            @MdcParams(@MdcParam(field = "bar"))
            @MdcParam(field = "foo")
                    Object foo) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcParamUseWithoutFieldOrGetter(
            @MdcParam
                    Object foo) {
        mdc = MDC.getCopyOfContextMap();
    }

    @Slf4jMdcAware
    public void mdcParamUseWithFieldAndGetter(
            @MdcParam(field = "functionName", getter = "getFunctionName")
                    Object foo) {
        mdc = MDC.getCopyOfContextMap();
    }
}
