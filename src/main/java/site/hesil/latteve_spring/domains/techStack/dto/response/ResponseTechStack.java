package site.hesil.latteve_spring.domains.techStack.dto.response;

import site.hesil.latteve_spring.domains.techStack.domain.TechStack;

/**
 * packageName    : site.hesil.latteve_spring.domains.techStack.dto.response
 * fileName       : ResponseTechStack
 * author         : yunbin
 * date           : 2024-09-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-01           yunbin           최초 생성
 */
public record ResponseTechStack (long techStackId,
        String name,
        String imgUrl){

    public static ResponseTechStack of(TechStack techStack) {
        return new ResponseTechStack(
                techStack.getTechStackId(),
                techStack.getName(),
                techStack.getImgUrl()
        );
    }
}
