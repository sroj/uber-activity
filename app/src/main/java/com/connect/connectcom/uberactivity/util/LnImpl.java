package com.connect.connectcom.uberactivity.util;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import java.util.Locale;

import javax.inject.Inject;

/**
 * Copyright 2009-2014 roboguice committers
 * <p/>
 * Modified by sven 2015 - Strings.toString(s1) calls were throwing an error at compile time - switched to using
 * s1.toString()
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
public class LnImpl implements LnInterface {

    protected int minimumLogLevel = Log.VERBOSE;
    protected String packageName = "";
    protected String tag = "";

    public LnImpl() {
        // do nothing, used by Ln before injection is set up
    }

    @Inject
    public LnImpl(Application context) {
        try {
            packageName = context.getPackageName();
            final int flags = context.getPackageManager().getApplicationInfo(packageName, 0).flags;
            minimumLogLevel = (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0 ? Log.VERBOSE : Log.INFO;
            tag = packageName.toUpperCase(Locale.US);

            Ln.d("Configuring Logging, minimum log level is %s", Ln.logLevelToString(minimumLogLevel));

        } catch (Exception e) {
            try {
                Log.e(packageName, "Error configuring logger", e);
            } catch (RuntimeException f) { // NOPMD - Legal empty catch block
                // HACK ignore Stub! errors in mock objects during testing
            }
        }
    }

    @Override
    public int v(Throwable t) {
        return getLoggingLevel() <= Log.VERBOSE ? println(Log.VERBOSE, Log.getStackTraceString(t)) : 0;
    }

    @Override
    public int v(Object s1, Object... args) {
        if (getLoggingLevel() > Log.VERBOSE) {
            return 0;
        }

        final String s = s1.toString();
        final String message = formatArgs(s, args);
        return println(Log.VERBOSE, message);
    }

    @Override
    public int v(Throwable throwable, Object s1, Object[] args) {
        if (getLoggingLevel() > Log.VERBOSE) {
            return 0;
        }

        final String s = s1.toString();
        final String message = formatArgs(s, args) + '\n' + Log.getStackTraceString(throwable);
        return println(Log.VERBOSE, message);
    }

    @Override
    public int d(Throwable t) {
        return getLoggingLevel() <= Log.DEBUG ? println(Log.DEBUG, Log.getStackTraceString(t)) : 0;
    }

    @Override
    public int d(Object s1, Object... args) {
        if (getLoggingLevel() > Log.DEBUG) {
            return 0;
        }

        final String s = s1.toString();
        final String message = formatArgs(s, args);
        return println(Log.DEBUG, message);
    }

    @Override
    public int d(Throwable throwable, Object s1, Object... args) {
        if (getLoggingLevel() > Log.DEBUG) {
            return 0;
        }

        final String s = s1.toString();
        final String message = formatArgs(s, args) + '\n' + Log.getStackTraceString(throwable);
        return println(Log.DEBUG, message);
    }

    @Override
    public int i(Throwable t) {
        return getLoggingLevel() <= Log.INFO ? println(Log.INFO, Log.getStackTraceString(t)) : 0;
    }

    @Override
    public int i(Throwable throwable, Object s1, Object... args) {
        if (getLoggingLevel() > Log.INFO) {
            return 0;
        }

        final String s = s1.toString();
        final String message = formatArgs(s, args) + '\n' + Log.getStackTraceString(throwable);
        return println(Log.INFO, message);
    }

    @Override
    public int i(Object s1, Object... args) {
        if (getLoggingLevel() > Log.INFO) {
            return 0;
        }

        final String s = s1.toString();
        final String message = formatArgs(s, args);
        return println(Log.INFO, message);
    }

    @Override
    public int w(Throwable t) {
        return getLoggingLevel() <= Log.WARN ? println(Log.WARN, Log.getStackTraceString(t)) : 0;
    }

    @Override
    public int w(Throwable throwable, Object s1, Object... args) {
        if (getLoggingLevel() > Log.WARN) {
            return 0;
        }

        final String s = s1.toString();
        final String message = formatArgs(s, args) + '\n' + Log.getStackTraceString(throwable);
        return println(Log.WARN, message);
    }

    @Override
    public int w(Object s1, Object... args) {
        if (getLoggingLevel() > Log.WARN) {
            return 0;
        }

        final String s = s1.toString();
        final String message = formatArgs(s, args);
        return println(Log.WARN, message);
    }

    @Override
    public int e(Throwable t) {
        return getLoggingLevel() <= Log.ERROR ? println(Log.ERROR, Log.getStackTraceString(t)) : 0;
    }

    @Override
    public int e(Throwable throwable, Object s1, Object... args) {
        if (getLoggingLevel() > Log.ERROR) {
            return 0;
        }

        final String s = s1.toString();
        final String message = formatArgs(s, args) + '\n' + Log.getStackTraceString(throwable);
        return println(Log.ERROR, message);
    }

    @Override
    public int e(Object s1, Object... args) {
        if (getLoggingLevel() > Log.ERROR) {
            return 0;
        }

        final String s = s1.toString();
        final String message = formatArgs(s, args);
        return println(Log.ERROR, message);
    }

    @Override
    public boolean isDebugEnabled() {
        return getLoggingLevel() <= Log.DEBUG;
    }

    @Override
    public boolean isVerboseEnabled() {
        return getLoggingLevel() <= Log.VERBOSE;
    }

    @Override
    public String logLevelToString(int loglevel) {
        switch (loglevel) {
            case Log.VERBOSE:
                return "VERBOSE";
            case Log.DEBUG:
                return "DEBUG";
            case Log.INFO:
                return "INFO";
            case Log.WARN:
                return "WARN";
            case Log.ERROR:
                return "ERROR";
            case Log.ASSERT:
                return "ASSERT";

            default:
                return "UNKNOWN";
        }
    }

    @Override
    public int getLoggingLevel() {
        return minimumLogLevel;
    }

    @Override
    public void setLoggingLevel(int level) {
        minimumLogLevel = level;
    }

    public int println(int priority, String msg) {
        return Log.println(priority, getTag(), processMessage(msg));
    }

    protected String processMessage(String msg) {
        if (getLoggingLevel() <= Log.DEBUG) {
            msg = String.format("%s %s", Thread.currentThread().getName(), msg);
        }
        return msg;
    }

    protected String getTag() {
        final int skipDepth = 6; // skip 6 stackframes to find the location where this was called
        if (getLoggingLevel() <= Log.DEBUG) {
            final StackTraceElement trace = Thread.currentThread().getStackTrace()[skipDepth];
            return tag + "/" + trace.getFileName() + ":" + trace.getLineNumber();
        }

        return tag;
    }

    //protected for testing.
    protected String formatArgs(final String s, Object... args) {
        //this is a bit tricky : if args is null, it is passed to formatting
        //(and yes this can still break depending on conversion of the formatter, see String.format)
        //else if there is no args, we return the message as-is, otherwise we pass args to formatting normally.
        if (args != null && args.length == 0) {
            return s;
        } else {
            return String.format(s, args);
        }
    }

}
