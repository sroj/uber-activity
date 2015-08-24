package com.connect.connectcom.uberactivity.util;

import javax.inject.Inject;

/**
 * Copyright 2009-2014 roboguice committers
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * A more natural android logging facility.
 * <p/>
 * WARNING: CHECK OUT COMMON PITFALLS BELOW
 * <p/>
 * Unlike {@link android.util.Log}, Log provides sensible defaults. Debug and Verbose logging is enabled for
 * applications that have "android:debuggable=true" in their AndroidManifest.xml. For apps built using SDK Tools r8 or
 * later, this means any debug build.  Release builds built with r8 or later will have verbose and debug log messages
 * turned off.
 * <p/>
 * The default tag is automatically set to your app's packagename, and the current context (eg. activity, service,
 * application, etc) is appended as well.  You can add an additional parameter to the tag using {@link #Log(String)}.
 * <p/>
 * Log-levels can be programatically overridden for specific instances using {@link #Log(String, boolean, boolean)}.
 * <p/>
 * Log messages may optionally use {@link String#format(String, Object...)} formatting, which will not be evaluated
 * unless the log statement is output. Additional parameters to the logging statement are treated as varrgs parameters
 * to {@link String#format(String, Object...)}
 * <p/>
 * Also, the current file and line is automatically appended to the tag (this is only done if debug is enabled for
 * performance reasons).
 * <p/>
 * COMMON PITFALLS: * Make sure you put the exception FIRST in the call.  A common mistake is to place it last as is the
 * android.util.Log convention, but then it will get treated as varargs parameter. * vararg parameters are not appended
 * to the log message!  You must insert them into the log message using %s or another similar format parameter
 * <p/>
 * Usage Examples:
 * <p/>
 * Ln.v("hello there"); Ln.d("%s %s", "hello", "there"); Ln.e( exception, "Error during some operation"); Ln.w(
 * exception, "Error during %s operation", "some other");
 */
@SuppressWarnings({"ImplicitArrayToString"})
public final class Ln {

    /**
     * lnImpl is initially set to LnImpl() with sensible defaults, then replaced by whatever binding you choose during
     * guice static injection pass.
     */
    @SuppressWarnings(value = "MS_SHOULD_BE_FINAL")
    @Inject
    protected static LnInterface lnImpl = new LnImpl();

    private Ln() {
    }

    public static int v(Throwable t) {
        return lnImpl.v(t);
    }

    public static int v(Object s1, Object... args) {
        return lnImpl.v(s1, args);
    }

    public static int v(Throwable throwable, Object s1, Object... args) {
        return lnImpl.v(throwable, s1, args);
    }

    public static int d(Throwable t) {
        return lnImpl.d(t);
    }

    public static int d(Object s1, Object... args) {
        return lnImpl.d(s1, args);
    }

    public static int d(Throwable throwable, Object s1, Object... args) {
        return lnImpl.d(throwable, s1, args);
    }

    public static int i(Throwable t) {
        return lnImpl.i(t);
    }

    public static int i(Object s1, Object... args) {
        return lnImpl.i(s1, args);
    }

    public static int i(Throwable throwable, Object s1, Object... args) {
        return lnImpl.i(throwable, s1, args);
    }

    public static int w(Throwable t) {
        return lnImpl.w(t);
    }

    public static int w(Object s1, Object... args) {
        return lnImpl.w(s1, args);
    }

    public static int w(Throwable throwable, Object s1, Object... args) {
        return lnImpl.w(throwable, s1, args);
    }

    public static int e(Throwable t) {
        return lnImpl.e(t);
    }

    public static int e(Object s1, Object... args) {
        return lnImpl.e(s1, args);
    }

    public static int e(Throwable throwable, Object s1, Object... args) {
        return lnImpl.e(throwable, s1, args);
    }

    public static boolean isDebugEnabled() {
        return lnImpl.isDebugEnabled();
    }

    public static boolean isVerboseEnabled() {
        return lnImpl.isVerboseEnabled();
    }

    public static int getLoggingLevel() {
        return lnImpl.getLoggingLevel();
    }

    public static void setLoggingLevel(int level) {
        lnImpl.setLoggingLevel(level);
    }

    public static String logLevelToString(int loglevel) {
        return lnImpl.logLevelToString(loglevel);
    }

}
