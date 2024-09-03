package site.hesil.latteve_spring.domains.techStack.dto.response;

import lombok.Builder;
import site.hesil.latteve_spring.domains.techStack.domain.TechStack;

import java.util.Optional;

/**
 * packageName    : site.hesil.latteve_spring.domains.projectStack.dto.response
 * fileName       : getAllProjectStackResponse
 * author         : Yeong-Huns
 * date           : 2024-09-01
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-01        Yeong-Huns       최초 생성
 */
@Builder
public record GetAllTechStackResponse(
        long techStackId,
        String name,
        String ImgUrl
) {
    public static GetAllTechStackResponse from(TechStack techStack) {
        String ImgUrl = Optional.ofNullable(techStack.getImgUrl()).orElse("");
        return GetAllTechStackResponse.builder()
                .techStackId(techStack.getTechStackId())
                .name(techStack.getName())
                .ImgUrl(ImgUrl)
                .build();
    }
}
