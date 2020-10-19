# logback-loki
> A logback Appender, Send logs to loki

## Usage example

```xml
<configuration>
    <appender name="INFO_LOKI" class="cn.luvletter.logback.loki.LokiAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
        <lokiHost>${lokiHost}</lokiHost>
        <label>EXAMPLE</label>
        <value>INFO</value>
    </appender>

    <root level="INFO">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="INFO_LOKI"/>
    </root>
</configuration>
```
