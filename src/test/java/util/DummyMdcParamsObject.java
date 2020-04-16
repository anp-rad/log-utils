package util;

import com.aspirecsl.log.MdcParam;
import com.aspirecsl.log.MdcParams;
import com.aspirecsl.log.aspects.MdcAspect;

/**
 * A dummy class which can be the target of {@link MdcParams} and {@link MdcParam} annotations.
 * <p>This class has no real-world use and only exists to facilitate the unit testing of {@link MdcAspect}.
 *
 * @author anoopr
 * @version 1c
 * @since 1c
 */
public final class DummyMdcParamsObject {

    private final String functionName;
    private final String functionVersion;

    public DummyMdcParamsObject(String functionName, String functionVersion) {
        this.functionName = functionName;
        this.functionVersion = functionVersion;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getFunctionVersion() {
        return functionVersion;
    }
}
