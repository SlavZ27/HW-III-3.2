package pro.sky.hwiii32.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InfoService {

    final Logger logger = LoggerFactory.getLogger(InfoService.class);

    @Value("${server.port}")
    private Integer port;

    public Integer getPort() {
        logger.info("Was invoked method getPort for find port = {}", port);
        return port;

    }
}
