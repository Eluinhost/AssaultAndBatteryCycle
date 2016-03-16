/*
 The MIT License (MIT)
 Copyright (c) 2016 Paul Schwandes (paulschwandes@web.de)

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

package de.web.paulschwandes.assaultandbatterycycle;

import com.google.common.collect.ImmutableMap;

import java.text.NumberFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeUtil {

    private static final Map<Character, TimeUnit> UNIT_MAP = ImmutableMap.<Character, TimeUnit>builder()
            .put('d', TimeUnit.DAYS)
            .put('h', TimeUnit.HOURS)
            .put('m', TimeUnit.MINUTES)
            .put('s', TimeUnit.SECONDS)
            .build();

    private static final String UNIT_NAME = "(?<unit>[a-z]+)";
    private static final String UNIT_SIZE = "(?<size>[-\\d]+)";
    private static final String OPTIONAL_WHITESPACE = "\\s*";

    private static final NumberFormat FORMATTER = NumberFormat.getInstance();

    static {
        FORMATTER.setParseIntegerOnly(true);
        FORMATTER.setMinimumIntegerDigits(2);
    }

    private static final Pattern PART_PATTERN = Pattern.compile("(?:" + UNIT_SIZE + OPTIONAL_WHITESPACE + UNIT_NAME + ")", Pattern.CASE_INSENSITIVE);

    public static Map<TimeUnit, Long> getUnits(String timeString) {
        ImmutableMap.Builder<TimeUnit, Long> map = ImmutableMap.builder();

        Matcher matcher = PART_PATTERN.matcher(timeString);

        while (matcher.find()) {
            String unit = matcher.group("unit").toLowerCase();
            String size = matcher.group("size");

            TimeUnit type = UNIT_MAP.get(unit.charAt(0));

            if (type == null) continue;

            map.put(type, Long.parseLong(size));
        }

        return map.build();
    }

    public static long getSeconds(String timeString) {
        Map<TimeUnit, Long> units = getUnits(timeString);

        long seconds = 0;

        for (Map.Entry<TimeUnit, Long> unit : units.entrySet()) {
            seconds += unit.getKey().toSeconds(unit.getValue());
        }

        return seconds;

    }
}
