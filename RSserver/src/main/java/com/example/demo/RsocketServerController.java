package com.example.demo;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Flux;

@Controller
public class RsocketServerController {
 
    Logger logger = LoggerFactory.getLogger(RsocketServerController.class);
 
    @MessageMapping("fileRoute")
    public Flux<String> requestResponse(String msg) {
        logger.info("Received a message: " + msg);
        Path ipPath = Paths.get("C:\\temp\\temp.txt");
        return Flux.using(
                () -> Files.lines(ipPath),
                Flux::fromStream,
                Stream::close
        );
    }
}