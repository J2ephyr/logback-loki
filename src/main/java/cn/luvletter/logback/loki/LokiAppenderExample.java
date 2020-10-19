package cn.luvletter.logback.loki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @author Zephyr
 * @date 2020/10/18
 */
public class LokiAppenderExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(LokiAppenderExample.class);

    public static void main(String[] args) {

        MDC.put("MDC_KEY","MDC_VALUE");
        MDC.put("THREAD_ID", String.valueOf(Thread.currentThread().getId()));

        LOGGER.debug("debug log1");
        LOGGER.debug("debug log2");
        LOGGER.debug("debug log3");
        LOGGER.debug("debug log4");
        LOGGER.debug("debug log5");
        LOGGER.debug("debug log6");
        LOGGER.debug("debug log7");
        LOGGER.info("info log");
        LOGGER.warn("warn log");
        LOGGER.warn("warn log");
        LOGGER.error("error log");
        LOGGER.error("error log1");
        LOGGER.error("error log2");
    }

}
