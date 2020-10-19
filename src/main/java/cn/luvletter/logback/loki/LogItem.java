package cn.luvletter.logback.loki;

/**
 * @author Zephyr
 * @date 2020/10/16
 */
public class LogItem {

    private Long nanoseconds;

    private String logLine;

    public LogItem(Long nanoseconds, String logLine) {
        this.nanoseconds = nanoseconds;
        this.logLine = logLine;
    }

    public Long getNanoseconds() {
        return nanoseconds;
    }

    public void setNanoseconds(Long nanoseconds) {
        this.nanoseconds = nanoseconds;
    }

    public String getLogLine() {
        return logLine;
    }

    public void setLogLine(String logLine) {
        this.logLine = logLine;
    }

}
