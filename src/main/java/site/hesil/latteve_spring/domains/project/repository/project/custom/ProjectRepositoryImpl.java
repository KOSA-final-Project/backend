package site.hesil.latteve_spring.domains.project.repository.project.custom;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import site.hesil.latteve_spring.domains.job.domain.QJob;
import site.hesil.latteve_spring.domains.member.domain.QMember;
import site.hesil.latteve_spring.domains.memberStack.domain.QMemberStack;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.domain.QProject;
import site.hesil.latteve_spring.domains.project.domain.projectLike.QProjectLike;
import site.hesil.latteve_spring.domains.project.domain.projectMember.QProjectMember;
import site.hesil.latteve_spring.domains.project.domain.recruitment.QRecruitment;
import site.hesil.latteve_spring.domains.project.dto.project.response.PopularProjectResponse;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectCardResponse;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse;
import site.hesil.latteve_spring.domains.project.dto.response.RetrospectiveResponse;
import site.hesil.latteve_spring.domains.projectStack.domain.QProjectStack;
import site.hesil.latteve_spring.domains.retrospective.domain.QRetrospective;
import site.hesil.latteve_spring.domains.techStack.domain.QTechStack;
import site.hesil.latteve_spring.global.error.errorcode.ErrorCode;
import site.hesil.latteve_spring.global.error.exception.CustomBaseException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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

    // 프로젝트 상세 정보 조회
    @Override
    public Optional<ProjectDetailResponse> getProjectDetail_deprecated(Long projectId) {
        QProject project = QProject.project;
        QProjectStack projectStack = QProjectStack.projectStack;
        QTechStack techStack = QTechStack.techStack;
        QProjectMember projectMember = QProjectMember.projectMember;
        QMemberStack memberStack = QMemberStack.memberStack;
        QRecruitment recruitment = QRecruitment.recruitment;
        QJob job = QJob.job;
        QMember member = QMember.member;

        QProjectMember subProjectMember = new QProjectMember("subProjectMember"); // 서브 쿼리용

        // 프로젝트 기본 정보 조회
        Tuple projectInfo = queryFactory
                .select(project.projectId, project.name, project.description, project.imgUrl,
                        project.status, project.createdAt, project.startedAt, project.duration, project.cycle)
                .from(project)
                .where(project.projectId.eq(projectId))
                .fetchOne();

        if (projectInfo == null) throw new CustomBaseException(ErrorCode.NOT_FOUND);

        // 프로젝트 기술 스택 조회
        List<ProjectDetailResponse.TechStack> projectTechStacks = new ArrayList<>();
        List<Tuple> projectTechStackTuples = queryFactory
                .select(new CaseBuilder()
                                .when(projectStack.techStack.techStackId.eq(1L)).then(projectStack.customStack)
                                .otherwise(techStack.name),
                        techStack.imgUrl)
                .from(projectStack)
                .leftJoin(techStack).on(projectStack.techStack.techStackId.eq(techStack.techStackId))
                .where(projectStack.project.projectId.eq(projectId))
                .fetch();
        log.debug("projectTechStackTuples: {}", projectTechStackTuples);

        for (Tuple tuple : projectTechStackTuples) {
            String techStackName = tuple.get(0, String.class);
            String techStackImg = tuple.get(techStack.imgUrl);

            projectTechStacks.add(new ProjectDetailResponse.TechStack(techStackName, techStackImg));
        }
        log.debug("projectTechStacks: {}", projectTechStacks);

        // 리더 정보 조회 //
        // 리더 기본 정보 조회
        Tuple leaderInfo = queryFactory
                .select(projectMember.member.memberId,
                        projectMember.member.nickname,
                        projectMember.member.imgUrl,
                        projectMember.member.github,
                        JPAExpressions.select(subProjectMember.count())
                                .from(subProjectMember)
                                .where(subProjectMember.member.memberId.eq(projectMember.member.memberId)
                                        .and(subProjectMember.project.status.eq(1))
                                        .and(subProjectMember.acceptStatus.eq(1))),
                        JPAExpressions.select(subProjectMember.count())
                                .from(subProjectMember)
                                .where(subProjectMember.member.memberId.eq(projectMember.member.memberId)
                                        .and(subProjectMember.project.status.eq(2))
                                        .and(subProjectMember.acceptStatus.eq(1))))
                .from(projectMember)
                .join(projectMember.member)
                .join(projectMember.project)
                .where(projectMember.isLeader.isTrue()
                        .and(projectMember.project.projectId.eq(projectId)))
                .fetchOne();

        if (leaderInfo == null) throw new CustomBaseException(ErrorCode.NOT_FOUND);

        // 리더 기술 스택 조회
        List<ProjectDetailResponse.TechStack> leaderTechStacks = new ArrayList<>();
        List<Tuple> leaderTechStackTuples = queryFactory
                .select(new CaseBuilder()
                                .when(memberStack.techStack.techStackId.eq(1L)).then(memberStack.customStack)
                                .otherwise(techStack.name),
                        techStack.imgUrl)
                .from(memberStack)
                .leftJoin(techStack).on(memberStack.techStack.techStackId.eq(techStack.techStackId))
                .where(memberStack.member.memberId.eq(leaderInfo.get(projectMember.member.memberId)))
                .fetch();
        log.debug("leaderTechStackTuples: {}", leaderTechStackTuples);

        for (Tuple tuple : leaderTechStackTuples) {
            String techStackName = tuple.get(0, String.class);
            String techStackImg = tuple.get(techStack.imgUrl);

            leaderTechStacks.add(new ProjectDetailResponse.TechStack(techStackName, techStackImg));
        }
        log.debug("leaderTechStacks: {}", leaderTechStacks);

        // 최종 Leader 객체 생성
        ProjectDetailResponse.Leader leader = new ProjectDetailResponse.Leader(
                leaderInfo.get(projectMember.member.memberId),
                leaderInfo.get(projectMember.member.nickname),
                leaderInfo.get(projectMember.member.imgUrl),
                leaderInfo.get(projectMember.member.github),
                Optional.ofNullable(leaderInfo.get(4, Long.class)).orElse(0L).intValue(),
                Optional.ofNullable(leaderInfo.get(5, Long.class)).orElse(0L).intValue(),
                leaderTechStacks
        );
        log.debug("leader: {}", leader);

        // 모집 정보 및 멤버 조회 //
        // 모집 정보 조회
        List<Tuple> recruitmentTuples = queryFactory
                .select(recruitment.job.jobId,
                        job.name,
                        recruitment.count.intValue())   // Integer를 int로 변환
                .from(recruitment)
                .join(job).on(recruitment.job.jobId.eq(job.jobId))
                .where(recruitment.project.projectId.eq(projectId)
                        .and(recruitment.job.jobId.ne(1L))) // jobId가 1이 아닌 경우만 조회
                .fetch();

        log.debug("recruitmentTuples: {}", recruitmentTuples);

        // 멤버 조회
        List<ProjectDetailResponse.Recruitment> recruitments = new ArrayList<>();
        for (Tuple tuple : recruitmentTuples) {
            Long jobId = tuple.get(recruitment.job.jobId);
            String jobName = tuple.get(job.name);
            int jobCount = Optional.ofNullable(tuple.get(2, Integer.class)).orElse(0);  // index로 접근하고 Integer로 가져온 후 자동 언박싱

            // 각 멤버 기본 정보
            List<Tuple> memberTuples = queryFactory
                    .select(projectMember.member.memberId,
                            projectMember.member.nickname,
                            projectMember.member.imgUrl,
                            projectMember.member.github,
                            JPAExpressions.select(subProjectMember.count())
                                    .from(subProjectMember)
                                    .where(subProjectMember.member.memberId.eq(projectMember.member.memberId)
                                            .and(subProjectMember.project.status.eq(1))
                                            .and(subProjectMember.acceptStatus.eq(1))),
                            JPAExpressions.select(subProjectMember.count())
                                    .from(subProjectMember)
                                    .where(subProjectMember.member.memberId.eq(projectMember.member.memberId)
                                            .and(subProjectMember.project.status.eq(2))
                                            .and(subProjectMember.acceptStatus.eq(1))))
                    .from(projectMember)
                    .join(projectMember.member, member)
                    .where(projectMember.project.projectId.eq(projectId)
                            .and(projectMember.job.jobId.eq(jobId))
                            .and(projectMember.acceptStatus.eq(1))) // 프로젝트 참여가 승인된 멤버만 가져옴
                    .fetch();
            log.debug("memberTuples: {}", memberTuples);

            List<ProjectDetailResponse.Member> members = new ArrayList<>();

            // 각 멤버의 기술 스택
            for (Tuple memberTuple : memberTuples) {
                // 모집 직무에 대한 참여 멤버가 없으면 기술 스택 가져오는 과정을 생략하여
                // memberTechStackTuples 구할 때 memberId를 null과 비교하는 문제 회피
                if (memberTuple.get(projectMember.member.memberId) == null) {
                    continue;
                }

                Long memberId = memberTuple.get(projectMember.member.memberId);
                String nickname = memberTuple.get(projectMember.member.nickname);
                String imgUrl = memberTuple.get(projectMember.member.imgUrl);
                String github = memberTuple.get(projectMember.member.github);
                int ongoingProjectCount = Optional.ofNullable(memberTuple.get(4, Long.class)).orElse(0L).intValue();
                int completedProjectCount = Optional.ofNullable(memberTuple.get(5, Long.class)).orElse(0L).intValue();

                List<ProjectDetailResponse.TechStack> memberTechStacks = new ArrayList<>();
                List<Tuple> memberTechStackTuples = queryFactory
                        .select(new CaseBuilder()
                                        .when(memberStack.techStack.techStackId.eq(1L)).then(memberStack.customStack)
                                        .otherwise(techStack.name),
                                techStack.imgUrl)
                        .from(memberStack)
                        .leftJoin(techStack).on(memberStack.techStack.techStackId.eq(techStack.techStackId))
                        .where(memberStack.member.memberId.eq(memberId))
                        .fetch();

                for (Tuple tuple2 : memberTechStackTuples) {
                    String techStackName = tuple2.get(0, String.class);
                    String techStackImg = tuple2.get(techStack.imgUrl);

                    memberTechStacks.add(new ProjectDetailResponse.TechStack(techStackName, techStackImg));
                }

                members.add(new ProjectDetailResponse.Member(memberId, nickname, imgUrl, github, ongoingProjectCount, completedProjectCount, memberTechStacks));
            }
            log.debug("members: {}", members);

            recruitments.add(new ProjectDetailResponse.Recruitment(jobId, jobName, jobCount, members));
        }
        log.debug("recruitments: {}", recruitments);

        return Optional.ofNullable(ProjectDetailResponse.builder()
                .projectId(projectInfo.get(project.projectId))
                .name(projectInfo.get(project.name))
                .description(projectInfo.get(project.description))
                .projectImg(projectInfo.get(project.imgUrl))
                .projectTechStack(projectTechStacks)
                .status(Optional.ofNullable(projectInfo.get(project.status)).orElse(-1))
                .createdAt(Optional.ofNullable(projectInfo.get(project.createdAt))
                        .map(LocalDateTime::toLocalDate)
                        .orElse(null))
                .startedAt(Optional.ofNullable(projectInfo.get(project.startedAt))
                        .map(LocalDateTime::toLocalDate)
                        .orElse(null))
                .duration(Optional.ofNullable(projectInfo.get(project.duration)).orElse(-1))
                .cycle(Optional.ofNullable(projectInfo.get(project.cycle)).orElse(-1))
                .leader(leader)
                .recruitments(recruitments)
                .build());
    }

    @Override
    public Page<Project> findProjectsByMemberIdAndStatus(Long memberId, int status, Pageable pageable) {
        QProject project = QProject.project;
        QProjectMember projectMember = QProjectMember.projectMember;

        List<Project> projects = queryFactory.select(project)
                .from(projectMember)
                .join(projectMember.project, project)
                .where(projectMember.member.memberId.eq(memberId)
                        .and(project.status.eq(status)))
                .offset(pageable.getOffset()) // 페이지의 시작점 설정
                .limit(pageable.getPageSize()) // 페이지 크기 설정
                .fetch();

        // 전체 개수 조회
        long total = queryFactory.select(project.count())
                .from(projectMember)
                .join(projectMember.project, project)
                .where(projectMember.member.memberId.eq(memberId)
                        .and(project.status.eq(status)))
                .fetchOne();

        // Page 객체로 반환
        return new PageImpl<>(projects, pageable, total);
    }

    @Override
    public int countProjectsByMemberIdAndStatus(Long memberId, int status) {
        QProject project = QProject.project;
        QProjectMember projectMember = QProjectMember.projectMember;

        Long count = queryFactory.select(project.count())
                .from(projectMember)
                .join(projectMember.project, project)
                .where(projectMember.member.memberId.eq(memberId)
                        .and(project.status.eq(status)))
                .fetchOne();


        return count != null ? count.intValue() : 0;
    }

    @Override
    public Optional<RetrospectiveResponse> getRetrospective(Long projectId, Long memberId, int week) {
        QRetrospective retrospective = QRetrospective.retrospective;

        Tuple retrospectiveInto = queryFactory.select(
                        retrospective.retId,
                        retrospective.title,
                        retrospective.content,
                        retrospective.createdAt,
                        retrospective.updatedAt)
                .from(retrospective)
                .where(retrospective.project.projectId.eq(projectId)
                        .and(retrospective.member.memberId.eq(memberId))
                        .and(retrospective.week.eq(week)))
                .fetchOne();

        if (retrospectiveInto == null) throw new CustomBaseException(ErrorCode.NOT_FOUND);

        return Optional.ofNullable(RetrospectiveResponse.builder()
                .retId(retrospectiveInto.get(retrospective.retId))
                .title(retrospectiveInto.get(retrospective.title))
                .content(retrospectiveInto.get(retrospective.content))
                .createdAt(retrospectiveInto.get(retrospective.createdAt))
                .updatedAt(retrospectiveInto.get(retrospective.updatedAt))
                .build());
    }


    public List<PopularProjectResponse> findPopularProjects(int limit) {
        QProject project = QProject.project;
        QRecruitment recruitment = QRecruitment.recruitment;
        QProjectMember projectMember = QProjectMember.projectMember;
        QProjectLike projectLike = QProjectLike.projectLike;
        QProjectStack projectStack = QProjectStack.projectStack;
        QTechStack techStack = QTechStack.techStack;
        QJob job = QJob.job;


        // 지원자 비율 계산 (승인된 멤버만 필터링)
        NumberExpression<Double> applicantsRatio =
                Expressions.cases()
                        .when(recruitment.count.sum().gt(0))
                        .then(
                                Expressions.asNumber(
                                        queryFactory
                                                .select(projectMember.count())  // 승인된 멤버 수를 카운트
                                                .from(projectMember)
                                                .where(projectMember.project.projectId.eq(project.projectId)
                                                        .and(projectMember.acceptStatus.eq(1))) // 승인된 멤버만 카운트
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
                        projectMember.countDistinct(),  // 각 프로젝트에 대한 멤버 수
                        recruitment.countDistinct(),  // 각 프로젝트에 대한 모집 팀원 수
                        basePopularityScore
                )
                .from(project)
                .leftJoin(recruitment).on(project.projectId.eq(recruitment.project.projectId))
                .leftJoin(projectMember).on(project.projectId.eq(projectMember.project.projectId))
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
                .select(projectStack.project.projectId, techStack.name, techStack.imgUrl)
                .from(projectStack)
                .join(projectStack.techStack, techStack)
                .where(projectStack.project.projectId.in(projectIds))  // 모든 projectId에 대해 한 번에 조회
                .fetch()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(projectStack.project.projectId),  // Key: projectId
                        tuple -> new ArrayList<>(List.of(new ProjectCardResponse.TechStack(  // Value: TechStack을 리스트로 변환
                                tuple.get(techStack.name),
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
                    Long currentCnt = tuple.get(projectMember.countDistinct());  // 현재 모인 팀원 수
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
