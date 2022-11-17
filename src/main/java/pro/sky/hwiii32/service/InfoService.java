package pro.sky.hwiii32.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class InfoService {

    private final static Logger logger = LoggerFactory.getLogger(InfoService.class);

    @Value("${server.port}")
    private Integer port;

    public Integer getPort() {
        logger.info("Was invoked method getPort for find port = {}", port);
        return port;

    }

    public Integer getIntValue() {
        logger.info("Was invoked method getIntValue for get integer value");
        long time = System.currentTimeMillis();
        logger.info("Method getIntValue was started");
        int sum = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, Integer::sum);

        time = System.currentTimeMillis() - time;

        logger.info("Method getIntValue was completed and returned the value = {} in {} millis", sum, time);
        return sum;
    }
}
