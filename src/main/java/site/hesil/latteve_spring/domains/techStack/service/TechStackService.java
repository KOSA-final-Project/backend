package site.hesil.latteve_spring.domains.techStack.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.hesil.latteve_spring.domains.techStack.dto.response.GetAllTechStackResponse;
import site.hesil.latteve_spring.domains.techStack.repository.TechStackRepository;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.techStack.service
 * fileName       : TechStackService
 * author         : Yeong-Huns
 * date           : 2024-09-01
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-01        Yeong-Huns       최초 생성
 */
@RequiredArgsConstructor
@Service
public class TechStackService {
    private final TechStackRepository techStackRepository;

    public List<GetAllTechStackResponse> getAllTechStack(){
        return techStackRepository.findAll()
                .stream()
                .map(GetAllTechStackResponse::from)
                .toList();
    }
}
