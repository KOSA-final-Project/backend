package site.hesil.latteve_spring.domains.project.repository.project.custom;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import site.hesil.latteve_spring.domains.alarm.domain.QAlarm;
import site.hesil.latteve_spring.domains.project.domain.QProject;
import site.hesil.latteve_spring.domains.project.domain.projectLike.ProjectLike;
import site.hesil.latteve_spring.domains.project.domain.projectLike.QProjectLike;
import site.hesil.latteve_spring.domains.project.domain.recruitment.QRecruitment;
import site.hesil.latteve_spring.domains.project.dto.project.response.PopularProjectResponse;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectCardResponse;
import site.hesil.latteve_spring.domains.projectStack.domain.QProjectStack;
import site.hesil.latteve_spring.domains.techStack.domain.QTechStack;

import java.util.*;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.repository.custom
 * fileName       :
 * author         : JooYoon
 * date           : 2024-08-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-27        JooYoon       최초 생성
 */
@Log4j2
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /** 홈페이지 */
    // 4개씩 조회

    //프로젝트 신규순 조회
    @Override
    public Page<ProjectCardResponse> getNewProjects(Pageable pageable) {
        QProject project = QProject.project;
        QProjectStack projectStack = QProjectStack.projectStack;
        QTechStack techStack = QTechStack.techStack;
        QRecruitment recruitment = QRecruitment.recruitment;
        QProjectLike projectLike = QProjectLike.projectLike;
        QAlarm alarm = QAlarm.alarm;

        // 메인 쿼리: ProjectCardResponse 생성
        List<ProjectCardResponse> projectList = queryFactory
                .select(Projections.constructor(
                        ProjectCardResponse.class,
                        project.projectId,
                        project.name,
                        project.imgUrl,
                        project.duration,
                        Projections.list(
                                Projections.constructor(
                                        ProjectCardResponse.TechStack.class,
                                        techStack.name,
                                        techStack.imgUrl
                                )
                        ), // TechStack 리스트 매핑
                        Expressions.constant(false), // 좋아요 여부 (임시값)
                        projectLike.countDistinct().as("cntLike"), // 좋아요 수
                        alarm.countDistinct().intValue().as("currentCnt"), // 현재 모인 팀원 수
                        recruitment.countDistinct().intValue().as("teamCnt") // 모집 인원 수
                ))
                .from(project)
                // 조인 수행
                .leftJoin(projectStack).on(projectStack.project.eq(project))
                .leftJoin(projectStack.techStack, techStack)
                .leftJoin(projectLike).on(projectLike.project.eq(project))
                .leftJoin(recruitment).on(recruitment.project.eq(project))
                .leftJoin(alarm).on(alarm.recruitment.project.eq(project).and(alarm.type.eq(1)))
                .groupBy(project.projectId)
                .orderBy(project.createdAt.desc()) // 최신순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회
        long total = queryFactory
                .select(project.projectId.countDistinct()) // 중복 제거 후 개수 계산
                .from(project)
                .leftJoin(projectStack).on(projectStack.project.eq(project))
                .leftJoin(projectStack.techStack, techStack)
                .leftJoin(projectLike).on(projectLike.project.eq(project))
                .leftJoin(recruitment).on(recruitment.project.eq(project))
                .leftJoin(alarm).on(alarm.recruitment.project.eq(project).and(alarm.type.eq(1)))
                .groupBy(project.projectId)
                .fetch().size(); // 그룹화된 결과의 개수 반환


        return new PageImpl<>(projectList, pageable, total);
    }

    @Override
    public Page<ProjectCardResponse> getProjectsSortedByRecentlyConcluded(Pageable pageable) {
        return null;
    }

    @Override
    public Page<PopularProjectResponse> getPopularProjects(Pageable pageable) {
        return null;
    }
}
