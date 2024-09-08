package site.hesil.latteve_spring.domains.alarm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import site.hesil.latteve_spring.domains.alarm.dto.AlarmExampleResponse;

import java.util.List;
import java.util.stream.IntStream;

/**
 * packageName    : site.hesil.latteve_spring.domains.alarm.service
 * fileName       : AlarmService
 * author         : Yeong-Huns
 * date           : 2024-09-09
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-09        Yeong-Huns       최초 생성
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class AlarmService {
    //private final AlarmRepository alarmRepository;

    public List<AlarmExampleResponse> getNotifications() {
        return IntStream.range(0, 10)
                .mapToObj(i -> new AlarmExampleResponse(i,i + " 번 프로젝트에 참가 승인 되었습니다. 더 긴 메세지 테스트입니다.", Math.random() < 0.5))
                .toList();
    }
}
