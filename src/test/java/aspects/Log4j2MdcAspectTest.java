package aspects;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.ThreadContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.aspirecsl.log.HasDiagnosticContext;
import com.aspirecsl.log.Log4j2MdcAware;
import com.aspirecsl.log.aspects.MdcAspect;

import util.DummyMdcParamsObject;
import util.Log4j2MdcAwareAnnotationClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test case for {@link MdcAspect} when used with {@link Log4j2MdcAware}
 */
public class Log4j2MdcAspectTest {

    /**
     * A static map that supplies entries to the logging framework's MDC.
     */
    public static final Map<String, String> MDC_MAP_ONE;

    /**
     * A static map with nested maps that supply entries to the logging framework's MDC.
     */
    public static final Map<String, Object> MAP_WITH_NESTED_MDC_ONE;

    /**
     * A static map that supplies entries to the logging framework's MDC.
     */
    public static final Map<String, String> MDC_MAP_TWO;

    /**
     * A static map with nested maps that supply entries to the logging framework's MDC.
     */
    public static final Map<String, Object> MAP_WITH_NESTED_MDC_TWO;

    static {
        MDC_MAP_ONE = new HashMap<>();
        MDC_MAP_ONE.put("foo-one", "bar-one");
        MDC_MAP_ONE.put("foo-two", "bar-two");

        MDC_MAP_TWO = new HashMap<>();
        MDC_MAP_TWO.put("this-one", "that-one");
        MDC_MAP_TWO.put("this-two", "that-two");

        MAP_WITH_NESTED_MDC_ONE = new HashMap<>();
        final Map<String, String> nestedOne = new HashMap<>();
        nestedOne.put("nested1-foo-one", "nested1-bar-one");
        nestedOne.put("nested1-foo-two", "nested1-bar-two");

        final Map<String, String> nestedTwo = new HashMap<>();
        nestedTwo.put("nested2-foo-one", "nested2-bar-one");
        nestedTwo.put("nested2-foo-two", "nested2-bar-two");

        MAP_WITH_NESTED_MDC_ONE.put("foo", "bar");
        MAP_WITH_NESTED_MDC_ONE.put("this", "that");
        MAP_WITH_NESTED_MDC_ONE.put("nested1", nestedOne);
        MAP_WITH_NESTED_MDC_ONE.put("nested2", nestedTwo);

        MAP_WITH_NESTED_MDC_TWO = new HashMap<>();
        final Map<String, String> nestedThree = new HashMap<>();
        nestedThree.put("nested3-this-one", "nested3-that-one");
        nestedThree.put("nested3-this-two", "nested3-that-two");

        final Map<String, String> nestedFour = new HashMap<>();
        nestedFour.put("nested4-this-one", "nested4-that-one");
        nestedFour.put("nested4-this-two", "nested4-that-two");

        MAP_WITH_NESTED_MDC_TWO.put("foo", "bar");
        MAP_WITH_NESTED_MDC_TWO.put("this", "that");
        MAP_WITH_NESTED_MDC_TWO.put("nested3", nestedThree);
        MAP_WITH_NESTED_MDC_TWO.put("nested4", nestedFour);
    }

    private final Log4j2MdcAwareAnnotationClient log4j2MdcAwareAnnotationClient = new Log4j2MdcAwareAnnotationClient();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void beforeTest() {
        assertThat(ThreadContext.getImmutableContext())
                .as("Log4J ThreadContext is empty before method execution")
                .isEmpty();
    }

    @After
    public void afterTest() {
        assertThat(ThreadContext.getImmutableContext())
                .as("Log4J ThreadContext is empty after method execution")
                .isEmpty();
    }

    // MdcMap tests

    @Test
    public void mdcMapUsageIsNullSafe() {
        log4j2MdcAwareAnnotationClient.mdcMapArg(null);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] is null safe")
                .isEmpty();
    }

    @Test
    public void mdcMapUsageIsEmptyMapSafe() {
        log4j2MdcAwareAnnotationClient.mdcMapArg(Collections.emptyMap());
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] is empty map safe")
                .isEmpty();
    }

    @Test
    public void mdcMapUsage() {
        log4j2MdcAwareAnnotationClient.mdcMapArg(MDC_MAP_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap]")
                .containsOnlyKeys("foo-one", "foo-two");
        assertThat(mdc)
                .as("[@MdcMap]")
                .extractingByKeys("foo-one", "foo-two")
                .containsExactly("bar-one", "bar-two");
    }

    @Test
    public void mdcMapUsageWithMdcKeyPrefix() {
        log4j2MdcAwareAnnotationClient.mdcMapWithKeyPrefixArg(MDC_MAP_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] with mdcKeyPrefix")
                .containsOnlyKeys("custom-foo-one", "custom-foo-two");
        assertThat(mdc)
                .as("[@MdcMap] with mdcKeyPrefix")
                .extractingByKeys("custom-foo-one", "custom-foo-two")
                .containsExactly("bar-one", "bar-two");
    }

    @Test
    public void mdcMapUsageWithFilter() {
        log4j2MdcAwareAnnotationClient.mdcMapWithFilterArg(MDC_MAP_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] with filter")
                .containsOnlyKeys("foo-one");
        assertThat(mdc)
                .as("[@MdcMap] with filter")
                .extractingByKeys("foo-one")
                .containsOnly("bar-one");
    }

    @Test
    public void mdcMapUsageWithFilterCaseInsensitive() {
        log4j2MdcAwareAnnotationClient.mdcMapWithFilterArgCaseInsensitive(MDC_MAP_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] with filter")
                .containsOnlyKeys("foo-one");
        assertThat(mdc)
                .as("[@MdcMap] with filter")
                .extractingByKeys("foo-one")
                .containsOnly("bar-one");
    }

    @Test
    public void mdcMapUsageWithFilterAndMdcKeyPrefix() {
        log4j2MdcAwareAnnotationClient.mdcMapWithFilterAndKeyPrefixArg(MDC_MAP_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] with a filter and mdcKeyPrefix")
                .containsOnlyKeys("custom-foo-one");
        assertThat(mdc)
                .as("[@MdcMap] with a filter and mdcKeyPrefix")
                .extractingByKeys("custom-foo-one")
                .containsOnly("bar-one");
    }

    @Test
    public void mdcMapWithNestedMapKeys() {
        log4j2MdcAwareAnnotationClient.mdcMapWithNestedMapArg(MAP_WITH_NESTED_MDC_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] with nestedMapKeys")
                .containsOnlyKeys("nested2-foo-one", "nested2-foo-two");
        assertThat(mdc)
                .as("[@MdcMap] with nestedMapKeys")
                .extractingByKeys("nested2-foo-one", "nested2-foo-two")
                .containsExactly("nested2-bar-one", "nested2-bar-two");
    }

    @Test
    public void mdcMapWithNestedMapKeysAndMdcKeyPrefix() {
        log4j2MdcAwareAnnotationClient.mdcMapWithNestedMapAndKeyPrefixArg(MAP_WITH_NESTED_MDC_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] with nestedMapKeys and mdcKeyPrefix")
                .containsOnlyKeys("custom-nested2-foo-one", "custom-nested2-foo-two");
        assertThat(mdc)
                .as("[@MdcMap] with nestedMapKeys and mdcKeyPrefix")
                .extractingByKeys("custom-nested2-foo-one", "custom-nested2-foo-two")
                .containsExactly("nested2-bar-one", "nested2-bar-two");
    }

    @Test
    public void mdcMapWithNestedKeysAndFilter() {
        log4j2MdcAwareAnnotationClient.mdcMapWithNestedMapAndFilterArg(MAP_WITH_NESTED_MDC_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] with nestedMapKeys and filter")
                .containsOnlyKeys("nested1-foo-two");
        assertThat(mdc)
                .as("[@MdcMap] with nestedMapKeys and filter")
                .extractingByKeys("nested1-foo-two")
                .containsOnly("nested1-bar-two");
    }

    @Test
    public void mdcMapWithNestedKeysFilterAndMdcKeyPrefix() {
        log4j2MdcAwareAnnotationClient.mdcMapWithNestedMapFilterAndKeyPrefixArg(MAP_WITH_NESTED_MDC_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] with nestedMapKeys, filter and mdcKeyPrefix")
                .containsOnlyKeys("custom-nested1-foo-two");
        assertThat(mdc)
                .as("[@MdcMap] with nestedMapKeys, filter and mdcKeyPrefix")
                .extractingByKeys("custom-nested1-foo-two")
                .containsOnly("nested1-bar-two");
    }

    @Test
    public void mdcMapMultipleArgsUsage() {
        log4j2MdcAwareAnnotationClient.mdcMapArgs(MDC_MAP_ONE, MDC_MAP_TWO);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] multiple args")
                .containsOnlyKeys("foo-one", "this-one", "foo-two", "this-two");
        assertThat(mdc)
                .as("[@MdcMap] multiple args")
                .extractingByKeys("foo-one", "this-one", "foo-two", "this-two")
                .containsExactly("bar-one", "that-one", "bar-two", "that-two");
    }

    @Test
    public void mdcMapMultipleArgsWithDifferentMdcKeyPrefixes() {
        log4j2MdcAwareAnnotationClient.mdcMapArgsWithMdcKeyPrefix(MDC_MAP_ONE, MDC_MAP_TWO);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] multiple args")
                .containsOnlyKeys("one-foo-two", "two-this-two", "one-foo-one", "two-this-one");
        assertThat(mdc)
                .as("[@MdcMap] multiple args")
                .extractingByKeys("one-foo-two", "two-this-two", "one-foo-one", "two-this-one")
                .containsExactly("bar-two", "that-two", "bar-one", "that-one");
    }

    @Test
    public void mdcMapMultipleArgsWithOnlyOneMdcKeyPrefix() {
        log4j2MdcAwareAnnotationClient.mdcMapArgsWithAndWithoutMdcKeyPrefix(MDC_MAP_ONE, MDC_MAP_TWO);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] multiple args with one mdcKeyPrefix")
                .containsOnlyKeys("this-one", "custom-foo-two", "this-two", "custom-foo-one");
        assertThat(mdc)
                .as("[@MdcMap] multiple args with one mdcKeyPrefix")
                .extractingByKeys("this-one", "custom-foo-two", "this-two", "custom-foo-one")
                .containsExactly("that-one", "bar-two", "that-two", "bar-one");
    }

    @Test
    public void mdcMapMultiArgsWithNestedMapKeysAndOnlyOneMdcKeyPrefix() {
        log4j2MdcAwareAnnotationClient
                .mdcMapWithNestedMapsWithAndWithoutMdcKeyPrefix(MAP_WITH_NESTED_MDC_ONE, MAP_WITH_NESTED_MDC_TWO);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] with nestedMapKeys with one mdcKeyPrefix")
                .containsOnlyKeys("custom-nested1-foo-one", "nested3-this-one", "custom-nested1-foo-two", "nested3-this-two");
        assertThat(mdc)
                .as("[@MdcMap] with nestedMapKeys with one mdcKeyPrefix")
                .extractingByKeys("custom-nested1-foo-one", "nested3-this-one", "custom-nested1-foo-two", "nested3-this-two")
                .containsExactly("nested1-bar-one", "nested3-that-one", "nested1-bar-two", "nested3-that-two");
    }

    @Test
    public void mdcMapMultipleArgsWithOnlyOneFilter() {
        log4j2MdcAwareAnnotationClient.mdcMapWithAndWithoutFilter(MDC_MAP_ONE, MDC_MAP_TWO);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] multiple args with one filter")
                .containsOnlyKeys("this-one", "this-two", "foo-one");
        assertThat(mdc)
                .as("[@MdcMap] multiple args with one filter")
                .extractingByKeys("this-one", "this-two", "foo-one")
                .containsExactly("that-one", "that-two", "bar-one");
    }

    @Test
    public void mdcMapArgsWithNestedKeysAndOneFilter() {
        log4j2MdcAwareAnnotationClient.mdcMapWithNestedMapsWithAndWithoutFilter(MAP_WITH_NESTED_MDC_ONE, MAP_WITH_NESTED_MDC_TWO);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] two args both nestedMapKeys but only one filter")
                .containsOnlyKeys("nested1-foo-two", "nested4-this-two", "nested4-this-one");
        assertThat(mdc)
                .as("[@MdcMap] two args both nestedMapKeys but only one filter")
                .extractingByKeys("nested1-foo-two", "nested4-this-two", "nested4-this-one")
                .containsOnly("nested1-bar-two", "nested4-that-two", "nested4-that-one");
    }

    @Test
    public void mdcMapUsageWithAnotherNonAnnotatedParam() {
        log4j2MdcAwareAnnotationClient.mdcMapArgWithAnotherNonAnnotatedArg(MDC_MAP_ONE, MDC_MAP_TWO);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] with another non-annotated param")
                .containsOnlyKeys("foo-one", "foo-two");
        assertThat(mdc)
                .as("[@MdcMap] with another non-annotated param")
                .extractingByKeys("foo-one", "foo-two")
                .containsExactly("bar-one", "bar-two");
    }

    @Test
    public void mdcMapUsageWithNestedMapKeysIsSafeEvenWhenNoNestedMapFound() {
        log4j2MdcAwareAnnotationClient.mdcMapWithUnknownNestedMapKey(MAP_WITH_NESTED_MDC_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] with an unknown nestedMapKey")
                .isEmpty();
    }

    @Test
    public void mdcMapUsageWithNestedMapKeysAndFilterIsSafeEvenWhenNoEntriesSatisfyTheFilter() {
        log4j2MdcAwareAnnotationClient.mdcMapWithNestedMapAndUnSatisfiedFilterArg(MAP_WITH_NESTED_MDC_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] with nestedMapKey and a filter when no entries satisfy the filter")
                .isEmpty();
    }

    @Test
    public void mdcMapUsageWithFilterIsSafeEvenWhenNoEntriesSatisfyTheFilter() {
        log4j2MdcAwareAnnotationClient.mdcMapWithUnSatisfiedFilterArg(MDC_MAP_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] when no entries satisfy the filter")
                .isEmpty();
    }

    @Test
    public void mdcMapUsedWithoutLog4j2MdcAwareDoesNotAddAnythingToTheMdc() {
        log4j2MdcAwareAnnotationClient.mdcMapWithoutLog4j2MdcAware(MDC_MAP_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] without [@Log4j2MdcAware]")
                .isEmpty();
    }

    @Test
    public void mdcMapThrowsExceptionWhenAttachedToANonMapArgument() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("@MdcMap is allowed only on Map<String,String> objects");

        log4j2MdcAwareAnnotationClient.mdcMapArg("not-a-map");
    }

    @Test
    public void mdcMapWithNestedMapKeysThrowsExceptionWhenNestedObjectIsNotAMap() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("@MdcMap(nestedMapKeys = {\"this\"}) is allowed only on Map objects that return a "
                + "Map<String,String> nested map when outerMap.get(\"this\") is called");

        final Map<Object, Object> map = new HashMap<>();
        map.put("nested2", "not-a-map");
        log4j2MdcAwareAnnotationClient.mdcMapWithNestedMapArg(map);
    }

    @Test
    public void mdcMapAdviceDoesNotRemoveUnRelatedEntriesFromLogMdcPostExecution() {
        ThreadContext.put("external", "value");

        assertThat(ThreadContext.getImmutableContext())
                .as("Log context before [@MdcMap]")
                .containsOnlyKeys("external");

        log4j2MdcAwareAnnotationClient.mdcMapArg(MDC_MAP_ONE);

        assertThat(ThreadContext.getImmutableContext())
                .as("Log context after [@MdcMap]")
                .containsOnlyKeys("external");

        ThreadContext.remove("external");
    }

    @Test
    public void mdcMapWrappedInMdcMaps() {
        log4j2MdcAwareAnnotationClient.mdcMapWrappedInMdcMaps(MAP_WITH_NESTED_MDC_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMaps] use")
                .containsOnlyKeys("this", "nested1-foo-two");
        assertThat(mdc)
                .as("[@MdcMaps] use")
                .extractingByKeys("this", "nested1-foo-two")
                .containsExactly("that", "nested1-bar-two");
    }

    // MdcValue tests

    @Test
    public void mdcValueUsageIsNullSafe() {
        log4j2MdcAwareAnnotationClient.mdcValueArg(null);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcValue] is null safe")
                .isEmpty();
    }

    @Test
    public void mdcValueUsageWithExplicitNullability() {
        log4j2MdcAwareAnnotationClient.mdcValueWithNullabilityArg(null);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcValue] with explicit nullability")
                .containsOnlyKeys("foo");
        assertThat(mdc)
                .as("[@MdcValue] with explicit nullability")
                .extractingByKeys("foo")
                .containsOnly("null");
    }

    @Test
    public void mdcValueUsage() {
        log4j2MdcAwareAnnotationClient.mdcValueArg("bar");
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcValue]")
                .containsOnlyKeys("foo");
        assertThat(mdc)
                .as("[@MdcValue]")
                .extractingByKeys("foo")
                .containsOnly("bar");
    }

    @Test
    public void mdcValueUsageWithNonAnnotatedParam() {
        log4j2MdcAwareAnnotationClient.mdcValueArgWithAnotherNonAnnotatedArg("bar", "one");
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcValue] with another non-annotated param")
                .containsOnlyKeys("foo");
        assertThat(mdc)
                .as("[@MdcValue] with another non-annotated param")
                .extractingByKeys("foo")
                .containsOnly("bar");
    }

    @Test
    public void mdcValueUsedWithoutLog4j2MdcAwareDoesNotAddAnythingToTheMdc() {
        log4j2MdcAwareAnnotationClient.mdcValueWithoutLog4j2MdcAware("bar");
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcValue] without [@Log4j2MdcAware]")
                .isEmpty();
    }

    @Test
    public void mdcValueAdviceDoesNotRemoveUnRelatedEntriesFromLogMdcPostExecution() {
        ThreadContext.put("external", "value");

        assertThat(ThreadContext.getImmutableContext())
                .as("Log context before [@MdcValue]")
                .containsOnlyKeys("external");

        log4j2MdcAwareAnnotationClient.mdcValueArg("bar");

        assertThat(ThreadContext.getImmutableContext())
                .as("Log context after [@MdcValue]")
                .containsOnlyKeys("external");

        ThreadContext.remove("external");
    }

    // MdcHolder tests

    @Test
    public void mdcHolderIsNullSafe() {
        log4j2MdcAwareAnnotationClient.mdcHolderArg(null);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcHolder] is null safe")
                .isEmpty();
    }

    @Test
    public void mdcHolderUsage() {
        log4j2MdcAwareAnnotationClient.mdcHolderArg((HasDiagnosticContext) () -> MDC_MAP_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcHolder]")
                .containsOnlyKeys("foo-one", "foo-two");
        assertThat(mdc)
                .as("[@MdcHolder]")
                .extractingByKeys("foo-one", "foo-two")
                .containsExactly("bar-one", "bar-two");
    }

    @Test
    public void mdcHolderWithFilter() {
        log4j2MdcAwareAnnotationClient.mdcHolderArgWithFilter(() -> MDC_MAP_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcHolder] with filter")
                .containsOnlyKeys("foo-two");
        assertThat(mdc)
                .as("[@MdcHolder] with filter")
                .extractingByKeys("foo-two")
                .containsOnly("bar-two");
    }

    @Test
    public void mdcHolderWithFilterAndMdcKeyPrefix() {
        log4j2MdcAwareAnnotationClient.mdcHolderArgWithFilterAndMdcKeyPrefix(() -> MDC_MAP_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcHolder] with a filter and mdcKeyPrefix")
                .containsOnlyKeys("custom-foo-two");
        assertThat(mdc)
                .as("[@MdcHolder] with a filter and mdcKeyPrefix")
                .extractingByKeys("custom-foo-two")
                .containsOnly("bar-two");
    }

    @Test
    public void mdcHolderUsageWithNonAnnotatedParam() {
        log4j2MdcAwareAnnotationClient.mdcHolderArgWithAnotherNonAnnotatedArg(() -> MDC_MAP_TWO, () -> MDC_MAP_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcHolder] with another non-annotated param")
                .containsOnlyKeys("this-one", "this-two");
        assertThat(mdc)
                .as("[@MdcHolder] with another non-annotated param")
                .extractingByKeys("this-one", "this-two")
                .containsOnly("that-one", "that-two");
    }

    @Test
    public void mdcHolderUsedWithoutLog4j2MdcAwareDoesNotAddAnythingToTheMdc() {
        log4j2MdcAwareAnnotationClient.mdcHolderWithoutLog4j2MdcAware(() -> MDC_MAP_ONE);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcHolder] without [@Log4j2MdcAware]")
                .isEmpty();
    }

    @Test
    public void mdcHolderMultipleArgsWithOnlyOneFilter() {
        log4j2MdcAwareAnnotationClient.mdcHolderArgsWithAndWithoutFilter(() -> MDC_MAP_ONE, () -> MDC_MAP_TWO);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcHolder] multiple args with one filter")
                .containsOnlyKeys("this-one", "this-two", "foo-two");
        assertThat(mdc)
                .as("[@MdcHolder] multiple args with one filter")
                .extractingByKeys("this-one", "this-two", "foo-two")
                .containsExactly("that-one", "that-two", "bar-two");
    }

    @Test
    public void mdcHolderMultipleArgsWithOnlyOneMdcKeyPrefix() {
        log4j2MdcAwareAnnotationClient.mdcHolderArgsWithAndWithoutMdcKeyPrefix(() -> MDC_MAP_ONE, () -> MDC_MAP_TWO);
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcHolder] multiple args with one mdcKeyPrefix")
                .containsOnlyKeys("this-one", "custom-foo-two", "this-two", "custom-foo-one");
        assertThat(mdc)
                .as("[@MdcHolder] multiple args with one mdcKeyPrefix")
                .extractingByKeys("this-one", "custom-foo-two", "this-two", "custom-foo-one")
                .containsExactly("that-one", "bar-two", "that-two", "bar-one");
    }

    @Test
    public void mdcHolderThrowsExceptionWhenAttachedToANonMapArgument() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("@MdcHolder is allowed only on com.aspirecsl.log.utils.HasDiagnosticContext objects");

        log4j2MdcAwareAnnotationClient.mdcHolderArg("not-a-map");
    }

    @Test
    public void mdcHolderAdviceDoesNotRemoveUnRelatedEntriesFromLogMdcPostExecution() {
        ThreadContext.put("external", "value");

        assertThat(ThreadContext.getImmutableContext())
                .as("Log context before [@MdcHolder]")
                .containsOnlyKeys("external");

        log4j2MdcAwareAnnotationClient.mdcHolderArg((HasDiagnosticContext) () -> MDC_MAP_ONE);

        assertThat(ThreadContext.getImmutableContext())
                .as("Log context after [@MdcHolder]")
                .containsOnlyKeys("external");

        ThreadContext.remove("external");
    }

    // MdcParam(s) tests

    @Test
    public void mdcParamsUseWithFieldAndLabel() {
        log4j2MdcAwareAnnotationClient.mdcParamUseWithField(new DummyMdcParamsObject("aspirecsl", "V1.2.3"));
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcParam] specifying field and label")
                .containsOnlyKeys("function-name");
        assertThat(mdc)
                .as("[@MdcParam] specifying field and label")
                .extractingByKeys("function-name")
                .containsExactly("aspirecsl");
    }

    @Test
    public void mdcParamsUseWithGetterAndLabel() {
        log4j2MdcAwareAnnotationClient.mdcParamUseWithGetter(new DummyMdcParamsObject("aspirecsl", "V1.2.3"));
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcParam] specifying getter and label")
                .containsOnlyKeys("function-version");
        assertThat(mdc)
                .as("[@MdcParam] specifying getter and label")
                .extractingByKeys("function-version")
                .containsExactly("V1.2.3");
    }

    @Test
    public void mdcParamsUseWithFieldAndDefaultLabel() {
        log4j2MdcAwareAnnotationClient.mdcParamUseWithFieldAndDefaultLabel(new DummyMdcParamsObject("aspirecsl", "V1.2.3"));
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcParam] specifying field but not label")
                .containsOnlyKeys("functionName");
        assertThat(mdc)
                .as("[@MdcParam] specifying field but not label")
                .extractingByKeys("functionName")
                .containsExactly("aspirecsl");
    }

    @Test
    public void mdcParamsUseWithGetterAndDefaultLabel() {
        log4j2MdcAwareAnnotationClient.mdcParamUseWithGetterAndDefaultLabel(new DummyMdcParamsObject("aspirecsl", "V1.2.3"));
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcParam] specifying getter but not label")
                .containsOnlyKeys("getFunctionVersion");
        assertThat(mdc)
                .as("[@MdcParam] specifying getter but not label")
                .extractingByKeys("getFunctionVersion")
                .containsExactly("V1.2.3");
    }

    @Test
    public void multiMdcParamsUse() {
        log4j2MdcAwareAnnotationClient.multiMdcParamUse(new DummyMdcParamsObject("aspirecsl", "V1.2.3"));
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcParam] specifying getter but not label")
                .containsOnlyKeys("function-name", "function-version");
        assertThat(mdc)
                .as("[@MdcParam] specifying getter but not label")
                .extractingByKeys("function-name", "function-version")
                .containsExactly("aspirecsl", "V1.2.3");
    }

    @Test
    public void mdcParamWrappedInMdcParams() {
        log4j2MdcAwareAnnotationClient.mdcParamWrappedInMdcParams(new DummyMdcParamsObject("aspirecsl", "V1.2.3"));
        final Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcParams] use")
                .containsOnlyKeys("function-name", "function-version");
        assertThat(mdc)
                .as("[@MdcParams] use")
                .extractingByKeys("function-name", "function-version")
                .containsExactly("aspirecsl", "V1.2.3");
    }
    // no MDC parameter annotation used test

    @Test
    public void nothingAddedToMdcWhenNoAnnotationsUsedOnTheMethodParams() {
        log4j2MdcAwareAnnotationClient.log4j2MdcAwareWithoutParameterAnnotation(MDC_MAP_ONE);
        Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;
        assertThat(mdc)
                .as("[@Log4j2MdcAware] without [@MdcMap]")
                .isEmpty();

        log4j2MdcAwareAnnotationClient.log4j2MdcAwareWithoutParameterAnnotation("foo");
        mdc = log4j2MdcAwareAnnotationClient.mdc;
        assertThat(mdc)
                .as("[@Log4j2MdcAware] without [@MdcValue]")
                .isEmpty();

        log4j2MdcAwareAnnotationClient.log4j2MdcAwareWithoutParameterAnnotation((HasDiagnosticContext) () -> MDC_MAP_TWO);
        mdc = log4j2MdcAwareAnnotationClient.mdc;
        assertThat(mdc)
                .as("[@Log4j2MdcAware] without [@MdcHolder]")
                .isEmpty();

        log4j2MdcAwareAnnotationClient.log4j2MdcAwareWithoutParameterAnnotation(new DummyMdcParamsObject("aspirecsl", "v1.2.3"));
        mdc = log4j2MdcAwareAnnotationClient.mdc;
        assertThat(mdc)
                .as("[@Log4j2MdcAware] without [@MdcParam or @MdcParams]")
                .isEmpty();
    }

    // all MDC parameter annotations used test

    @Test
    public void allMdcAnnotationsUsedTogether() {
        log4j2MdcAwareAnnotationClient
                .allMdcAnnotationsInUse(MDC_MAP_ONE, "bar", () -> MDC_MAP_TWO, new DummyMdcParamsObject("aspirecsl", "v.2468.0"));
        Map<String, String> mdc = log4j2MdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("All MDC parameter annotations in use")
                .containsOnlyKeys("functionVersion", "this-one", "foo-two", "this-two", "foo",
                        "foo-one");
        assertThat(mdc)
                .as("All MDC parameter annotations in use")
                .extractingByKeys("functionVersion", "this-one", "foo-two", "this-two", "foo",
                        "foo-one")
                .containsExactly("v.2468.0", "that-one", "bar-two", "that-two", "bar", "bar-one");
    }

    // MDC annotations not repeatable tests

    @Test
    public void mdcHolderCannotBeUsedWithMdcValue() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        log4j2MdcAwareAnnotationClient.mdcHolderAndMdcValueUsedSimultaneously("any");
    }

    @Test
    public void mdcHolderCannotBeUsedWithMdcMap() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        log4j2MdcAwareAnnotationClient.mdcHolderAndMdcMapUsedOnSameArg("any");
    }

    @Test
    public void mdcHolderCannotBeUsedWithMdcParam() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        log4j2MdcAwareAnnotationClient.mdcHolderAndMdcParamUsedOnSameArg("any");
    }

    @Test
    public void mdcHolderCannotBeUsedWithMdcParams() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        log4j2MdcAwareAnnotationClient.mdcHolderAndMdcParamsUsedOnSameArg("any");
    }

    @Test
    public void mdcValueCannotBeUsedWithMdcMap() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        log4j2MdcAwareAnnotationClient.mdcValueAndMdcMapUsedOnSameArg("any");
    }

    @Test
    public void mdcValueCannotBeUsedWithMdcParam() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        log4j2MdcAwareAnnotationClient.mdcValueAndMdcParamUsedOnSameArg("any");
    }

    @Test
    public void mdcMapCannotBeUsedWithMdcParam() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        log4j2MdcAwareAnnotationClient.mdcMapAndMdcParamUsedOnSameArg("any");
    }

    @Test
    public void mdcValueCannotBeUsedWithMdcParams() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        log4j2MdcAwareAnnotationClient.mdcValueAndMdcParamsUsedOnSameArg("any");
    }

    @Test
    public void mdcMapCannotBeUsedWithMdcParams() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        log4j2MdcAwareAnnotationClient.mdcMapAndMdcParamsUsedOnSameArg("any");
    }

    @Test
    public void mdcParamCannotBeUsedWithMdcParams() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        log4j2MdcAwareAnnotationClient.mdcParamsAndMdcParamUsedOnTheSameArg("any");
    }

    @Test
    public void mdcParamUseWithoutFieldOrGetter() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Either field() or getter() should be specified.");

        log4j2MdcAwareAnnotationClient.mdcParamUseWithoutFieldOrGetter("any");
    }

    @Test
    public void mdcParamUseWithFieldAndGetter() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Exactly one of field() or getter() should be specified.");

        log4j2MdcAwareAnnotationClient.mdcParamUseWithFieldAndGetter("any");
    }
}