package site.hesil.latteve_spring.domains.project.repository.project.custom;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import site.hesil.latteve_spring.domains.alarm.domain.QAlarm;
import site.hesil.latteve_spring.domains.job.domain.QJob;
import site.hesil.latteve_spring.domains.project.domain.QProject;
import site.hesil.latteve_spring.domains.project.domain.projectLike.QProjectLike;
import site.hesil.latteve_spring.domains.project.domain.recruitment.QRecruitment;
import site.hesil.latteve_spring.domains.project.dto.project.response.PopularProjectResponse;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectCardResponse;
import site.hesil.latteve_spring.domains.projectStack.domain.QProjectStack;
import site.hesil.latteve_spring.domains.techStack.domain.QTechStack;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.querydsl.core.types.dsl.Expressions.numberPath;

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
    //신규순으로 프로젝트 조회
//    @Override
//    public Page<ProjectCardResponse> findProjectsOrderByCreatedAtDesc(Pageable pageable){
//        QProject project = QProject.project;
//        QProjectStack projectStack = QProjectStack.projectStack;
//        QTechStack techStack = QTechStack.techStack;
//        QRecruitment recruitment = QRecruitment.recruitment;
//        QProjectLike projectLike = QProjectLike.projectLike;
//        QAlarm alarm = QAlarm.alarm;
//
//
//        // Project와 ProjectLike, Recruitment, Alarm, ProjectStack, TechStack 을 조인하여 데이터 조회
//        List<Tuple> results = queryFactory
//                .select(
//                        project.projectId,
//                        project.name,
//                        project.imgUrl,
//                        project.duration,
//                        // 프로젝트와 관련된 기술 스택 정보를 리스트 형태로 가져옴
//                        queryFactory.select(Projections.list(
//                                        Projections.constructor(
//                                                ProjectCardResponse.TechStack.class,
//                                                techStack.name,
//                                                techStack.imgUrl
//                                        )
//                                ))
//                                .from(projectStack)
//                                .leftJoin(projectStack.techStack, techStack)
//                                .where(projectStack.project.eq(project))
//                                .fetch(),
//                        false,
//                        // 좋아요 수
//                        queryFactory.select(projectLike.count())
//                                .from(projectLike)
//                                .where(projectLike.project.eq(project))
//                                .fetchOne(),
//                        // 현재 승인된 팀원 수 (알림의 타입이 1인 경우)
//                        queryFactory.select(alarm.count())
//                                .from(alarm)
//                                .where(alarm.recruitment.project.eq(project)
//                                        .and(alarm.type.eq(1)))
//                                .fetchOne(),
//                        // 모집 중인 총 인원 수
//                        queryFactory.select(recruitment.count())
//                                .from(recruitment)
//                                .where(recruitment.project.eq(project))
//                                .fetchOne().intValue()
//                ))
//                .from(project)
//                .leftJoin(project, projectLike.project) // Project와 ProjectLike를 조인하여 좋아요 정보 가져오기
//                .leftJoin(project, recruitment.project) // Project와 Recruitment를 조인하여 팀원 정보 가져오기
//                .leftJoin(-, alarm) // Recruitment와 Alarm을 조인하여 모집 정보 가져오기
//                .groupBy(project.projectId) // projectId 기준 그룹화
//                .orderBy(project.createdAt.desc()) // createdAt 기준 내림차순 정렬
//                .offset(pageable.getOffset()) // 페이징 처리
//                .limit(pageable.getPageSize()) // 페이지당 데이터 수
//                .fetch();
//
//        // 총 프로젝트 수 계산
//        long total = queryFactory
//                .select(project.count())
//                .from(project)
//                .fetchOne();
//
//        return new PageImpl<>(projectList, pageable, total);
//    }
//    }



    // 종료된 프로젝트 조회
//    public Page<ProjectCardResponse> findAllCompletedProjects(Pageable pageable){
//
//    }

    //인기순 프로젝트 조회
    public List<PopularProjectResponse> findPopularProjects(int limit) {
        QProject project = QProject.project;
        QRecruitment recruitment = QRecruitment.recruitment;
        QAlarm alarm = QAlarm.alarm;
        QProjectLike projectLike = QProjectLike.projectLike;
        QProjectStack projectStack = QProjectStack.projectStack;
        QTechStack techStack = QTechStack.techStack;
        QJob job = QJob.job;


        // 지원자 비율 계산  (승인된 멤버만 필터링하지 않고 모든 멤버를 포함)
        NumberExpression<Double> applicantsRatio =
                Expressions.cases()
                        .when(recruitment.count.sum().gt(0))
                        .then(
                                Expressions.asNumber(
                                        queryFactory
                                                .select(alarm.count())
                                                .from(alarm)
                                                .join(alarm.recruitment, recruitment)
                                                .where(recruitment.project.projectId.eq(project.projectId))
                                ).doubleValue().divide(recruitment.count.sum())
                        )
                        .otherwise(0.0);


        // 기본적인 좋아요 수 + 지원자 비율 계산
        NumberExpression<Double> basePopularityScore = projectLike.count().doubleValue().add(applicantsRatio);



        // 최종 쿼리
        List<Tuple> projectsWithPopularity = queryFactory
                .select(
                        project.projectId,
                        project.name,
                        project.imgUrl,
                        project.description,
                        project.duration,
                        project.createdAt,
                        projectLike.countDistinct(),  // 각 프로젝트에 대한 좋아요 수
                        Expressions.asNumber(
                                queryFactory
                                        .select(alarm.alarmId.countDistinct())  // Alarm의 distinct count를 계산
                                        .from(alarm)
                                        .join(alarm.recruitment, recruitment)  // Alarm을 Recruitment와 조인
                                        .join(recruitment.project, project)    // Recruitment를 Project와 조인
                                        .where(
                                                project.projectId.eq(project.projectId)  // 특정 projectId에 해당하는 조건
                                                        .and(alarm.type.eq(1))  // Alarm의 type이 1인 것만 필터링
                                        )
                        ).longValue(), // 각 프로젝트에 대한 참여 멤버 수를 가져옴 (type이 1인 Alarm 기반)
                        recruitment.countDistinct(),  // 각 프로젝트에 대한 모집 팀원 수
                        basePopularityScore
                )
                .from(project)
                .leftJoin(recruitment).on(project.projectId.eq(recruitment.project.projectId))
                .leftJoin(alarm).on(recruitment.recruitmentId.eq(alarm.recruitment.recruitmentId)
                        .and(alarm.type.eq(1))) // 알람이 해당 recruitment와 연결되고 type이 1인 것만 조회
                .leftJoin(projectLike).on(project.projectId.eq(projectLike.project.projectId))
                .groupBy(project.projectId)
                .orderBy(basePopularityScore.desc())
                .fetch();

        // projectIds 리스트 추출 (모든 프로젝트 ID 추출)
        List<Long> projectIds = projectsWithPopularity.stream()
                .map(tuple -> tuple.get(project.projectId))
                .toList();

        log.debug("projectIds: {}", projectIds);

        // 모든 프로젝트의 기술 스택 리스트 조회
        Map<Long, List<ProjectCardResponse.TechStack>> techStacksByProjectId = queryFactory
                .select(projectStack.project.projectId, new CaseBuilder()
                                .when(techStack.techStackId.eq(1L)).then(projectStack.customStack)
                                .otherwise(techStack.name),
                        techStack.imgUrl)
                .from(projectStack)
                .join(projectStack.techStack, techStack)
                .where(projectStack.project.projectId.in(projectIds))  // 모든 projectId에 대해 한 번에 조회
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(projectStack.project.projectId),  // Key: projectId
                        tuple -> new ArrayList<>(List.of(new ProjectCardResponse.TechStack(  // Value: TechStack을 리스트로 변환
                                tuple.get(1, String.class), // techStack.name or projectStack.customSTack
                                tuple.get(techStack.imgUrl)
                        ))),
                        (existing, replacement) -> {  // 기존 값에 새 값을 병합하는 로직
                            existing.addAll(replacement);  // 기존 리스트에 새 값을 추가
                            return existing;  // 병합된 리스트 반환
                        }
                ));
        log.debug(" techStacksByProjectId: {}", techStacksByProjectId);


        List<Tuple> results = queryFactory
                .select(recruitment.project.projectId, job.name)
                .from(recruitment)
                .join(recruitment.job, job)
                .where(recruitment.project.projectId.in(projectIds))
                .fetch();

        log.debug("Fetched recruitment data: {}", results);


        log.debug("Fetching recruitment names by project IDs: {}", projectIds);

        // 모든 프로젝트의 모집 포지션 리스트 조회
        Map<Long, List<String>> recruitmentNamesByProjectId = results.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(recruitment.project.projectId),  // Key: projectId
                        tuple -> Optional.ofNullable(tuple.get(job.name))  // Value: Optional로 job.name을 가져옴
                                .map(List::of)  // 값이 있을 때 리스트로 변환
                                .orElseGet(Collections::emptyList),  // 값이 없으면 빈 리스트 반환
                        (existing, replacement) -> {  // 기존 값에 새 값을 병합하는 로직
                            if (existing instanceof ArrayList) {
                                existing.addAll(replacement);  // 기존 리스트에 새 값을 추가
                            } else {
                                List<String> newList = new ArrayList<>(existing);
                                newList.addAll(replacement);
                                return newList;  // 변경 가능한 리스트로 반환
                            }
                            return existing;  // 병합된 리스트 반환
                        }
                ));


        log.debug(" recruitmentNamesByProjectId: {}", recruitmentNamesByProjectId);

        // 조회된 데이터를 필요한 필드만 매핑
        return projectsWithPopularity.stream()
                .map(tuple -> {
                    Long projectId = tuple.get(project.projectId);
                    String name = tuple.get(project.name);
                    String imgUrl = tuple.get(project.imgUrl);
                    String description = tuple.get(project.description);
                    int duration = tuple.get(project.duration);
                    LocalDateTime createdAt = tuple.get(project.createdAt);
                    Long cntLike = tuple.get(projectLike.countDistinct());  // 좋아요 수
                    Long currentCnt =   tuple.get(7, Long.class);   // 현재 모인 팀원 수
                    Long teamCnt = tuple.get(recruitment.countDistinct());  // 팀원 총 모집 수
                    double baseScore = tuple.get(basePopularityScore);

                    log.info("projectID " + projectId);

                    List<ProjectCardResponse.TechStack> techStackList = techStacksByProjectId.getOrDefault(projectId, null);
                    if (techStackList == null) {
                        log.warn("No tech stack found for project ID: {}", projectId);
                    }

                    List<String> recruitmentNames = recruitmentNamesByProjectId.getOrDefault(projectId, null);
                    if (recruitmentNames == null) {
                        log.warn("No recruitment names found for project ID: {}", projectId);
                    }


                    // 시간 가중치 계산
                    double timeWeight = calculateTimeWeight(createdAt.toLocalDate());
                    double finalPopularityScore = baseScore * timeWeight;

                    return new PopularProjectResponse(
                            projectId,
                            name,
                            imgUrl,
                            techStackList,
                            description,
                            recruitmentNames,
                            duration,
                            createdAt,
                            cntLike,           // 좋아요 수
                            Math.toIntExact(currentCnt),        // 현재 모인 팀원 수
                            Math.toIntExact(teamCnt),           // 팀원 총 모집 수
                            finalPopularityScore // 최종 인기도 점수
                    );
                })
                .toList();
    }

    // 시간 가중치 계산 (최근 5일은 1로 설정하고 점점 감소)
    private double calculateTimeWeight(LocalDate createdDate) {
        long daysSinceCreation = ChronoUnit.DAYS.between(createdDate, LocalDate.now());
        if (daysSinceCreation <= 5) {
            return 1.0; // 최근 5일 이내면 가중치 1
        }
        return Math.max(0.1, 1 - (double) daysSinceCreation / 30); // 30일 이후로 서서히 가중치 감소
    }



}
