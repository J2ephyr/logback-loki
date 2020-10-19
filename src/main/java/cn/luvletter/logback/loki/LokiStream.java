package cn.luvletter.logback.loki;

import java.util.*;

/**
 * {
 * "streams": [
 * {
 * "stream": {
 * "label": "value"
 * },
 * "values": [
 * [ "<unix epoch in nanoseconds>", "<log line>" ],
 * [ "<unix epoch in nanoseconds>", "<log line>" ]
 * ]
 * }
 * ]
 * }
 *
 * @author Zephyr
 * @date 2020/10/16
 */
public class LokiStream {

    private List<Stream> streams;

    private LokiStream() {
        streams = new ArrayList<>();
    }

    public LokiStream add(Stream stream) {
        this.streams.add(stream);
        return this;
    }

    public static LokiStream build() {
        return new LokiStream();
    }

    public static Stream buildStream(String label, String value) {
        return new Stream(label, value);
    }

    public List<Stream> getStreams() {
        return streams;
    }

    public void setStreams(List<Stream> streams) {
        this.streams = streams;
    }

    public static class Stream {

        private Map<String, String> stream = new HashMap<>(1);

        private List<List<String>> values;

        private long lastNanoseconds;

        public Stream(String label, String value) {
            stream.put(label, value);
            values = new ArrayList<>();
        }

        public void appendLine(LogItem item) {
            appendLine(item.getNanoseconds(), item.getLogLine());
        }

        public void appendLine(long nanoseconds, String logLine) {
            if (lastNanoseconds > nanoseconds) {
                throw new RuntimeException("nanoseconds should not be greater than lastNanoseconds!");
            }
            values.add(Arrays.asList(nanoseconds + "", logLine));
        }

        public Map<String, String> getStream() {
            return stream;
        }

        public void setStream(Map<String, String> stream) {
            this.stream = stream;
        }

        public List<List<String>> getValues() {
            return values;
        }

        public void setValues(List<List<String>> values) {
            this.values = values;
        }

    }

}
