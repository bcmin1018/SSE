package hello.sse.service.impl;

//import hello.sse.respository.StreamResponseRepository;
import hello.sse.respository.EmitterRepository;
import hello.sse.service.StreamResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SseServiceImpl implements StreamResponseService {

    private final EmitterRepository emitterRepository;

    private static final Long DEFAULT_TIMEOUT = 60 * 5 * 1000L;
    private static final Long RECONNECTION_TIMEOUT = 6 * 1000L;

    @Override
    public SseEmitter subscribe(Long id) {
        SseEmitter emitter = createEmitter(id);
        sendMessage(id, "EventStream Created, [UserId=" + id + "]");
        return emitter;
    }

    @Override
    public void sendMessage(Long id, Object data) {
        SseEmitter emitter = emitterRepository.get(id);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("firstSSE")
                        .data(data)
                        .reconnectTime(RECONNECTION_TIMEOUT)
                );
            } catch (IOException e) {
                // 보완
                emitter.complete();
            }
        }
    }

    private SseEmitter createEmitter(Long id) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(id, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        return emitter;
    }

}
