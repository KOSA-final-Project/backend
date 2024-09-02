package site.hesil.latteve_spring.global.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * packageName    : site.hesil.latteve_spring.global.redis.service
 * fileName       : ResdisService
 * author         : Yeong-Huns
 * date           : 2024-09-02
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-02        Yeong-Huns       최초 생성
 */
@RequiredArgsConstructor
@Service
public class RedisService {
    private final RedisTemplate<String, Object> jobTemplate;


}
