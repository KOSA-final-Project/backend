package site.hesil.latteve_spring.domains.techStack.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.hesil.latteve_spring.domains.techStack.domain.TechStack;
import site.hesil.latteve_spring.domains.techStack.dto.response.ResponseTechStack;
import site.hesil.latteve_spring.domains.techStack.repository.TechStackRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : site.hesil.latteve_spring.domains.techStack.service
 * fileName       : TechStackService
 * author         : yunbin
 * date           : 2024-09-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-01           yunbin           최초 생성
 */
@Service
@RequiredArgsConstructor
public class TechStackService {
    private final TechStackRepository techStackRepository;

    public List<ResponseTechStack> getAllTechStacks() {
        List<TechStack> techStacks = techStackRepository.findAll();

        return techStacks.stream()
                .map(ResponseTechStack::of)
                .collect(Collectors.toList());
    }
}
