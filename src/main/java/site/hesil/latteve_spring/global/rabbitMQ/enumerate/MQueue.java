package site.hesil.latteve_spring.global.rabbitMQ.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * packageName    : site.hesil.latteve_spring.global.rabbitMQ.enumerate
 * fileName       : Queue
 * author         : Yeong-Huns
 * date           : 2024-09-03
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-03        Yeong-Huns       최초 생성
 */
@Getter
@RequiredArgsConstructor
public enum MQueue {
    MEMBER_CREATE("member.create.queue"),
    DEAD_LETTER("deadLetter.queue"),
    MEMBER_DELETE("member.delete.queue"),
    MEMBER_UPDATE("member.update.queue"),

//    project
    PROJECT_CREATE("project.create.queue"),
    PROJECT_DELETE("project.delete.queue"),
    PROJECT_UPDATE("project.update.queue"),


//    projectLike
    PROJECT_LIKE_CREATE("project.like.create.queue"),
    PROJECT_LIKE_DELETE("project.like.delete.queue"),
    PROJECT_LIKE_UPDATE("project.like.update.queue"),

//    projectMember
    PROJECT_MEMBER_CREATE("project.member.create.queue"),
    PROJECT_MEMBER_DELETE("project.member.delete.queue"),
    PROJECT_MEMBER_UPDATE("project.member.update.queue"),
    ;

    private final String queue;
}
