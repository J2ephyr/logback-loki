package cn.luvletter.logback.loki;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.luvletter.logback.loki.proto.Logproto;
import com.google.protobuf.Timestamp;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Zephyr
 * @date 2020/10/19
 */
public class LokiProtoAppender<E> extends LokiAppender<E> {

    @Override
    protected synchronized void doSend() {
        List<Logproto.EntryAdapter> entryAdapterList = logCache.stream().map(c -> Logproto.EntryAdapter
                .newBuilder()
                .setTimestamp(Timestamp.newBuilder().setSeconds(c.getNanoseconds()).build())
                .setLine(c.getLogLine())
                .build()
        ).collect(Collectors.toList());
        Logproto.PushRequest pushRequest = Logproto.PushRequest.newBuilder()
                .addStreams(Logproto.StreamAdapter.newBuilder()
                        .addAllEntries(entryAdapterList)
                        .setLabels("{\"example\":\"debug\"}")
                        .build()
                )
                .build();
        HttpResponse response = httpRequest.body(pushRequest.toByteArray()).contentType("application/x-protobuf").execute();
        if (!response.isOk()) {
            addError(StrUtil.format("Failed request loki. Response: \n {}", response));
        } else {
            addInfo(StrUtil.format("日志发送成功，数量：{}", logCache.size()));
            logCache.clear();
        }
    }

}
