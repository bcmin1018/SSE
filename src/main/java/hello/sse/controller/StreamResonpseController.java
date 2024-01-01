package hello.sse.controller;

import hello.sse.service.StreamResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequiredArgsConstructor
public class StreamResonpseController {

    private final StreamResponseService streamResponseService;
    @GetMapping(path="/subscribe/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long id) {
        return streamResponseService.subscribe(id);
    }

    @GetMapping(path="/sendMessage/{id}/{data}")
    public void sendMessage(@PathVariable Long id, @PathVariable String data) {
        streamResponseService.sendMessage(id, data);
    }
}
