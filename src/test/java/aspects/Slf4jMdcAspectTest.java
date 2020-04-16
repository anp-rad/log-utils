package aspects;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.MDC;

import com.aspirecsl.log.HasDiagnosticContext;
import com.aspirecsl.log.Slf4jMdcAware;
import com.aspirecsl.log.aspects.MdcAspect;

import util.DummyMdcParamsObject;
import util.Slf4jMdcAwareAnnotationClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test case for {@link MdcAspect} when used with {@link Slf4jMdcAware}
 */
public class Slf4jMdcAspectTest {

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

    private final Slf4jMdcAwareAnnotationClient slf4jMdcAwareAnnotationClient = new Slf4jMdcAwareAnnotationClient();
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void beforeTest() {
        assertThat(MDC.getCopyOfContextMap())
                .as("Slf4j MDC context is empty before method execution")
                .isNullOrEmpty();
    }

    @After
    public void afterTest() {
        assertThat(MDC.getCopyOfContextMap())
                .as("Slf4j MDC context is empty after method execution")
                .isNullOrEmpty();
    }

    // MdcMap tests

    @Test
    public void mdcMapUsageIsNullSafe() {
        slf4jMdcAwareAnnotationClient.mdcMapArg(null);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] is null safe")
                .isEmpty();
    }

    @Test
    public void mdcMapUsageIsEmptyMapSafe() {
        slf4jMdcAwareAnnotationClient.mdcMapArg(Collections.emptyMap());
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] is empty map safe")
                .isEmpty();
    }

    @Test
    public void mdcMapUsage() {
        slf4jMdcAwareAnnotationClient.mdcMapArg(MDC_MAP_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcMapWithKeyPrefixArg(MDC_MAP_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcMapWithFilterArg(MDC_MAP_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcMapWithFilterArgCaseInsensitive(MDC_MAP_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcMapWithFilterAndKeyPrefixArg(MDC_MAP_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcMapWithNestedMapArg(MAP_WITH_NESTED_MDC_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcMapWithNestedMapAndKeyPrefixArg(MAP_WITH_NESTED_MDC_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcMapWithNestedMapAndFilterArg(MAP_WITH_NESTED_MDC_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcMapWithNestedMapFilterAndKeyPrefixArg(MAP_WITH_NESTED_MDC_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcMapArgs(MDC_MAP_ONE, MDC_MAP_TWO);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcMapArgsWithMdcKeyPrefix(MDC_MAP_ONE, MDC_MAP_TWO);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcMapArgsWithAndWithoutMdcKeyPrefix(MDC_MAP_ONE, MDC_MAP_TWO);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient
                .mdcMapWithNestedMapsWithAndWithoutMdcKeyPrefix(MAP_WITH_NESTED_MDC_ONE, MAP_WITH_NESTED_MDC_TWO);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcMapWithAndWithoutFilter(MDC_MAP_ONE, MDC_MAP_TWO);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcMapWithNestedMapsWithAndWithoutFilter(MAP_WITH_NESTED_MDC_ONE, MAP_WITH_NESTED_MDC_TWO);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcMapArgWithAnotherNonAnnotatedArg(MDC_MAP_ONE, MDC_MAP_TWO);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcMapWithUnknownNestedMapKey(MAP_WITH_NESTED_MDC_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] with an unknown nestedMapKey")
                .isEmpty();
    }

    @Test
    public void mdcMapUsageWithNestedMapKeysAndFilterIsSafeEvenWhenNoEntriesSatisfyTheFilter() {
        slf4jMdcAwareAnnotationClient.mdcMapWithNestedMapAndUnSatisfiedFilterArg(MAP_WITH_NESTED_MDC_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] with nestedMapKey and a filter when no entries satisfy the filter")
                .isEmpty();
    }

    @Test
    public void mdcMapUsageWithFilterIsSafeEvenWhenNoEntriesSatisfyTheFilter() {
        slf4jMdcAwareAnnotationClient.mdcMapWithUnSatisfiedFilterArg(MDC_MAP_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] when no entries satisfy the filter")
                .isEmpty();
    }

    @Test
    public void mdcMapUsedWithoutSlf4jMdcAwareDoesNotAddAnythingToTheMdc() {
        slf4jMdcAwareAnnotationClient.mdcMapWithoutSlf4jMdcAware(MDC_MAP_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcMap] without [@Slf4jMdcAware]")
                .isEmpty();
    }

    @Test
    public void mdcMapThrowsExceptionWhenAttachedToANonMapArgument() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("@MdcMap is allowed only on Map<String,String> objects");

        slf4jMdcAwareAnnotationClient.mdcMapArg("not-a-map");
    }

    @Test
    public void mdcMapWithNestedMapKeysThrowsExceptionWhenNestedObjectIsNotAMap() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("@MdcMap(nestedMapKeys = {\"this\"}) is allowed only on Map objects that return a "
                + "Map<String,String> nested map when outerMap.get(\"this\") is called");

        final Map<Object, Object> map = new HashMap<>();
        map.put("nested2", "not-a-map");
        slf4jMdcAwareAnnotationClient.mdcMapWithNestedMapArg(map);
    }

    @Test
    public void mdcMapAdviceDoesNotRemoveUnRelatedEntriesFromLogMdcPostExecution() {
        MDC.put("external", "value");

        assertThat(MDC.getCopyOfContextMap())
                .as("Log context before [@MdcMap]")
                .containsOnlyKeys("external");

        slf4jMdcAwareAnnotationClient.mdcMapArg(MDC_MAP_ONE);

        assertThat(MDC.getCopyOfContextMap())
                .as("Log context after [@MdcMap]")
                .containsOnlyKeys("external");

        MDC.remove("external");
    }

    @Test
    public void mdcMapWrappedInMdcMaps() {
        slf4jMdcAwareAnnotationClient.mdcMapWrappedInMdcMaps(MAP_WITH_NESTED_MDC_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcValueArg(null);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcValue] is null safe")
                .isEmpty();
    }

    @Test
    public void mdcValueUsageWithExplicitNullability() {
        slf4jMdcAwareAnnotationClient.mdcValueWithNullabilityArg(null);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcValueArg("bar");
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcValueArgWithAnotherNonAnnotatedArg("bar", "one");
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcValue] with another non-annotated param")
                .containsOnlyKeys("foo");
        assertThat(mdc)
                .as("[@MdcValue] with another non-annotated param")
                .extractingByKeys("foo")
                .containsOnly("bar");
    }

    @Test
    public void mdcValueUsedWithoutSlf4jMdcAwareDoesNotAddAnythingToTheMdc() {
        slf4jMdcAwareAnnotationClient.mdcValueWithoutSlf4jMdcAware("bar");
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcValue] without [@Slf4jMdcAware]")
                .isEmpty();
    }

    @Test
    public void mdcValueAdviceDoesNotRemoveUnRelatedEntriesFromLogMdcPostExecution() {
        MDC.put("external", "value");

        assertThat(MDC.getCopyOfContextMap())
                .as("Log context before [@MdcValue]")
                .containsOnlyKeys("external");

        slf4jMdcAwareAnnotationClient.mdcValueArg("bar");

        assertThat(MDC.getCopyOfContextMap())
                .as("Log context after [@MdcValue]")
                .containsOnlyKeys("external");

        MDC.remove("external");
    }

    // MdcHolder tests

    @Test
    public void mdcHolderIsNullSafe() {
        slf4jMdcAwareAnnotationClient.mdcHolderArg(null);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcHolder] is null safe")
                .isEmpty();
    }

    @Test
    public void mdcHolderUsage() {
        slf4jMdcAwareAnnotationClient.mdcHolderArg((HasDiagnosticContext) () -> MDC_MAP_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcHolderArgWithFilter(() -> MDC_MAP_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcHolderArgWithFilterAndMdcKeyPrefix(() -> MDC_MAP_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcHolderArgWithAnotherNonAnnotatedArg(() -> MDC_MAP_TWO, () -> MDC_MAP_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcHolder] with another non-annotated param")
                .containsOnlyKeys("this-one", "this-two");
        assertThat(mdc)
                .as("[@MdcHolder] with another non-annotated param")
                .extractingByKeys("this-one", "this-two")
                .containsOnly("that-one", "that-two");
    }

    @Test
    public void mdcHolderUsedWithoutSlf4jMdcAwareDoesNotAddAnythingToTheMdc() {
        slf4jMdcAwareAnnotationClient.mdcHolderWithoutSlf4jMdcAware(() -> MDC_MAP_ONE);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

        assertThat(mdc)
                .as("[@MdcHolder] without [@Slf4jMdcAware]")
                .isEmpty();
    }

    @Test
    public void mdcHolderMultipleArgsWithOnlyOneFilter() {
        slf4jMdcAwareAnnotationClient.mdcHolderArgsWithAndWithoutFilter(() -> MDC_MAP_ONE, () -> MDC_MAP_TWO);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcHolderArgsWithAndWithoutMdcKeyPrefix(() -> MDC_MAP_ONE, () -> MDC_MAP_TWO);
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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

        slf4jMdcAwareAnnotationClient.mdcHolderArg("not-a-map");
    }

    @Test
    public void mdcHolderAdviceDoesNotRemoveUnRelatedEntriesFromLogMdcPostExecution() {
        MDC.put("external", "value");

        assertThat(MDC.getCopyOfContextMap())
                .as("Log context before [@MdcHolder]")
                .containsOnlyKeys("external");

        slf4jMdcAwareAnnotationClient.mdcHolderArg((HasDiagnosticContext) () -> MDC_MAP_ONE);

        assertThat(MDC.getCopyOfContextMap())
                .as("Log context after [@MdcHolder]")
                .containsOnlyKeys("external");

        MDC.remove("external");
    }

    // MdcParam(s) tests

    @Test
    public void mdcParamsUseWithFieldAndLabel() {
        slf4jMdcAwareAnnotationClient.mdcParamUseWithField(new DummyMdcParamsObject("aspirecsl", "V1.2.3"));
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcParamUseWithGetter(new DummyMdcParamsObject("aspirecsl", "V1.2.3"));
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcParamUseWithFieldAndDefaultLabel(new DummyMdcParamsObject("aspirecsl", "V1.2.3"));
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcParamUseWithGetterAndDefaultLabel(new DummyMdcParamsObject("aspirecsl", "V1.2.3"));
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.multiMdcParamUse(new DummyMdcParamsObject("aspirecsl", "V1.2.3"));
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.mdcParamWrappedInMdcParams(new DummyMdcParamsObject("aspirecsl", "V1.2.3"));
        final Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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
        slf4jMdcAwareAnnotationClient.slf4jMdcAwareWithoutParameterAnnotation(MDC_MAP_ONE);
        Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;
        assertThat(mdc)
                .as("[@Slf4jMdcAware] without [@MdcMap]")
                .isEmpty();

        slf4jMdcAwareAnnotationClient.slf4jMdcAwareWithoutParameterAnnotation("foo");
        mdc = slf4jMdcAwareAnnotationClient.mdc;
        assertThat(mdc)
                .as("[@Slf4jMdcAware] without [@MdcValue]")
                .isEmpty();

        slf4jMdcAwareAnnotationClient.slf4jMdcAwareWithoutParameterAnnotation((HasDiagnosticContext) () -> MDC_MAP_TWO);
        mdc = slf4jMdcAwareAnnotationClient.mdc;
        assertThat(mdc)
                .as("[@Slf4jMdcAware] without [@MdcHolder]")
                .isEmpty();

        slf4jMdcAwareAnnotationClient.slf4jMdcAwareWithoutParameterAnnotation(new DummyMdcParamsObject("aspirecsl", "v1.2.3"));
        mdc = slf4jMdcAwareAnnotationClient.mdc;
        assertThat(mdc)
                .as("[@Slf4jMdcAware] without [@MdcParam or @MdcParams]")
                .isEmpty();
    }

    // all MDC parameter annotations used test

    @Test
    public void allMdcAnnotationsUsedTogether() {
        slf4jMdcAwareAnnotationClient
                .allMdcAnnotationsInUse(MDC_MAP_ONE, "bar", () -> MDC_MAP_TWO, new DummyMdcParamsObject("aspirecsl", "v.2468.0"));
        Map<String, String> mdc = slf4jMdcAwareAnnotationClient.mdc;

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

        slf4jMdcAwareAnnotationClient.mdcHolderAndMdcValueUsedSimultaneously("any");
    }

    @Test
    public void mdcHolderCannotBeUsedWithMdcMap() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        slf4jMdcAwareAnnotationClient.mdcHolderAndMdcMapUsedOnSameArg("any");
    }

    @Test
    public void mdcHolderCannotBeUsedWithMdcParam() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        slf4jMdcAwareAnnotationClient.mdcHolderAndMdcParamUsedOnSameArg("any");
    }

    @Test
    public void mdcHolderCannotBeUsedWithMdcParams() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        slf4jMdcAwareAnnotationClient.mdcHolderAndMdcParamsUsedOnSameArg("any");
    }

    @Test
    public void mdcValueCannotBeUsedWithMdcMap() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        slf4jMdcAwareAnnotationClient.mdcValueAndMdcMapUsedOnSameArg("any");
    }

    @Test
    public void mdcValueCannotBeUsedWithMdcParam() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        slf4jMdcAwareAnnotationClient.mdcValueAndMdcParamUsedOnSameArg("any");
    }

    @Test
    public void mdcMapCannotBeUsedWithMdcParam() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        slf4jMdcAwareAnnotationClient.mdcMapAndMdcParamUsedOnSameArg("any");
    }

    @Test
    public void mdcValueCannotBeUsedWithMdcParams() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        slf4jMdcAwareAnnotationClient.mdcValueAndMdcParamsUsedOnSameArg("any");
    }

    @Test
    public void mdcMapCannotBeUsedWithMdcParams() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        slf4jMdcAwareAnnotationClient.mdcMapAndMdcParamsUsedOnSameArg("any");
    }

    @Test
    public void mdcParamCannotBeUsedWithMdcParams() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("MDC parameter annotations are mutually exclusive.");

        slf4jMdcAwareAnnotationClient.mdcParamsAndMdcParamUsedOnTheSameArg("any");
    }

    @Test
    public void mdcParamUseWithoutFieldOrGetter() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Either field() or getter() should be specified.");

        slf4jMdcAwareAnnotationClient.mdcParamUseWithoutFieldOrGetter("any");
    }

    @Test
    public void mdcParamUseWithFieldAndGetter() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Exactly one of field() or getter() should be specified.");

        slf4jMdcAwareAnnotationClient.mdcParamUseWithFieldAndGetter("any");
    }
}