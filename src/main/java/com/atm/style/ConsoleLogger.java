package com.atm.style;
import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ConsoleLogger {
    private ConsoleLogger(){}
    private static final int CONSOLE_WIDTH = 120;
    private static final String SEPARATOR = StringUtils.repeat("-", CONSOLE_WIDTH);

    public static void print(String message, String color,boolean firstSeparator, boolean secondSeparator ) {
        int messageLength = message.length();
        int paddingLength = (CONSOLE_WIDTH - messageLength) / 2;
        String paddedMessage = StringUtils.repeat(" ", paddingLength) + message + StringUtils.repeat(" ", paddingLength);
        if(firstSeparator){
            log.info(SEPARATOR);
        }
        log.info(color + paddedMessage + ConsoleColor.ANSI_RESET);
        if(secondSeparator){
            log.info(SEPARATOR);
        }
    }
    public static void printPassword(String message, String color,boolean firstSeparator, boolean secondSeparator ) {
        int messageLength = message.length();
        String paddedMessage = StringUtils.repeat("*",messageLength);
        if(firstSeparator){
            log.info(SEPARATOR);
        }
        log.info(color + paddedMessage + ConsoleColor.ANSI_RESET);
        if(secondSeparator){
            log.info(SEPARATOR);
        }
    }
}
