package com.connect.connectcom.uberactivity;

/**
 * Copyright 2009-2014 roboguice committers
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
public interface LnInterface {
    int v(Throwable t);

    int v(
            Object s1,
            Object... args
    );

    int v(
            Throwable throwable,
            Object s1,
            Object[] args
    );

    int d(Throwable t);

    int d(
            Object s1,
            Object... args
    );

    int d(
            Throwable throwable,
            Object s1,
            Object... args
    );

    int i(Throwable t);

    int i(
            Throwable throwable,
            Object s1,
            Object... args
    );

    int i(
            Object s1,
            Object... args
    );

    int w(Throwable t);

    int w(
            Throwable throwable,
            Object s1,
            Object... args
    );

    int w(
            Object s1,
            Object... args
    );

    int e(Throwable t);

    int e(
            Throwable throwable,
            Object s1,
            Object... args
    );

    int e(
            Object s1,
            Object... args
    );

    boolean isDebugEnabled();

    boolean isVerboseEnabled();

    int getLoggingLevel();

    void setLoggingLevel(int level);

    String logLevelToString(int loglevel);
}