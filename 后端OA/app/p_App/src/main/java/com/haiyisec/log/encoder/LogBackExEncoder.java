package com.haiyisec.log.encoder;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import com.haiyisec.log.converter.ThreadNumConverter;


public class LogBackExEncoder extends PatternLayoutEncoder {
    static {
        PatternLayout.defaultConverterMap.put("T", ThreadNumConverter.class.getName());
        PatternLayout.defaultConverterMap.put("threadNum", ThreadNumConverter.class.getName());
    }
}
