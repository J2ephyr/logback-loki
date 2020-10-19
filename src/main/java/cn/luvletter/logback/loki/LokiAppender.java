package cn.luvletter.logback.loki;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Zephyr
 * @date 2020/10/16
 */
public class LokiAppender<E> extends UnsynchronizedAppenderBase<E> {

    private String lokiHost;

    private String label;

    private String value;

    protected Encoder<E> encoder;

    private static final String PUSH_PATH = "/loki/api/v1/push";

    private static final long DELAY = TimeUnit.SECONDS.toMillis(1);

    private static final long MAX_CACHE = 5;

    private ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);

    private HttpRequest httpRequest;

    private List<LogItem> logCache;

    private ScheduledFuture<?> senderFuture;

    private final Sender sender = new Sender();

    @Override
    public void start() {
        try {
            subStart();
        } catch (Exception e) {
            addError("Failed to start LokiAppender.", e);
        }
        super.start();
    }

    private void subStart() {
        httpRequest = HttpUtil.createPost(lokiHost + PUSH_PATH).header(Header.CONTENT_TYPE, "application/json");
        logCache = new LinkedList<>();
    }

    @Override
    protected void append(E e) {
        if (!(e instanceof LoggingEvent)) {
            return;
        }
        LoggingEvent event = (LoggingEvent) e;
        if (encoder != null) {
            LogItem logItem = new LogItem(TimeUnit.MILLISECONDS.toNanos(event.getTimeStamp()), new String(encoder.encode(e)));
            logCache.add(logItem);
            this.send();
        }
    }

    private void send() {
        int cacheSize = logCache.size();
        if (cacheSize == 0) {
            return;
        }
        if (senderFuture != null && !senderFuture.isCancelled()) {
            senderFuture.cancel(true);
        }
        LogItem last = logCache.get(cacheSize - 1);
        if (System.currentTimeMillis() - last.getNanoseconds() > DELAY || cacheSize > MAX_CACHE) {
            this.doSend();
        } else {
            if (senderFuture == null || senderFuture.isCancelled() || senderFuture.isDone()) {
                senderFuture = executor.schedule(new Sender(), 5L, TimeUnit.SECONDS);
            }
        }
        System.out.println("senderFuture is cancelled:" + senderFuture.isCancelled());
    }

    protected void doSend() {
        LokiStream.Stream stream = LokiStream.buildStream(label, value);
        logCache.forEach(stream::appendLine);
        LokiStream lokiStream = LokiStream.build().add(stream);
        HttpResponse response = httpRequest.body(JSONUtil.toJsonStr(lokiStream)).execute();
        if (!response.isOk()) {
            addError(StrUtil.format("Failed request loki. Response: {}", response));
        } else {
            logCache.clear();
        }
    }

    private class Sender implements Runnable {

        @Override
        public void run() {
            System.out.println("执行任务了");
            doSend();
        }

    }

    public String getLokiHost() {
        return lokiHost;
    }

    public void setLokiHost(String lokiHost) {
        this.lokiHost = lokiHost;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Encoder<E> getEncoder() {
        return encoder;
    }

    public void setEncoder(Encoder<E> encoder) {
        this.encoder = encoder;
    }

}
