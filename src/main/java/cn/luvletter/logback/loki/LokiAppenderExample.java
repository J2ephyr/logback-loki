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

        LOGGER.trace("trace log");
        LOGGER.debug("debug log");
        LOGGER.info("info log");
        LOGGER.warn("warn log");
        LOGGER.error("error log");
        LOGGER.error("error log");
        LOGGER.error("error log");
        LOGGER.error("error log");
    }

}
