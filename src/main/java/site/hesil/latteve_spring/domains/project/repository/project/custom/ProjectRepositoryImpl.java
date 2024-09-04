package site.hesil.latteve_spring.domains.project.repository.project.custom;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import site.hesil.latteve_spring.domains.job.domain.QJob;
import site.hesil.latteve_spring.domains.memberStack.domain.QMemberStack;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.domain.QProject;
import site.hesil.latteve_spring.domains.project.domain.projectMember.QProjectMember;
import site.hesil.latteve_spring.domains.project.domain.recruitment.QRecruitment;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse;
import site.hesil.latteve_spring.domains.projectStack.domain.QProjectStack;
import site.hesil.latteve_spring.domains.techStack.domain.QTechStack;
import site.hesil.latteve_spring.global.error.errorcode.ErrorCode;
import site.hesil.latteve_spring.global.error.exception.CustomBaseException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.repository.custom
 * fileName       : ProjectRepositoryImpl
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
    public Optional<ProjectDetailResponse> getProjectDetail(Long projectId) {
        QProject project = QProject.project;
        QProjectStack projectStack = QProjectStack.projectStack;
        QTechStack techStack = QTechStack.techStack;
        QProjectMember projectMember = QProjectMember.projectMember;
        QMemberStack memberStack = QMemberStack.memberStack;
        QRecruitment recruitment = QRecruitment.recruitment;
        QJob job = QJob.job;

        // 프로젝트 기본 정보 조회
        Tuple projectInfo = queryFactory
                .select(project.projectId, project.name, project.description, project.imgUrl,
                        project.status, project.createdAt, project.startedAt, project.duration, project.cycle)
                .from(project)
                .where(project.projectId.eq(projectId))
                .fetchOne();

        if(projectInfo == null) throw new CustomBaseException(ErrorCode.NOT_FOUND);

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
                        projectMember.project.status.when(1).then(1).otherwise(0).sum().as("ongoingProjectCount"),
                        projectMember.project.status.when(2).then(1).otherwise(0).sum().as("completedProjectCount"))
                .from(projectMember)
                .join(projectMember.member)
                .join(projectMember.project)
                .where(projectMember.isLeader.isTrue()
                        .and(projectMember.project.projectId.eq(projectId)))
                .groupBy(projectMember.member.memberId,
                        projectMember.member.nickname,
                        projectMember.member.imgUrl,
                        projectMember.member.github)
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
                Optional.ofNullable(leaderInfo.get(4, Integer.class)).orElse(0),
                Optional.ofNullable(leaderInfo.get(5, Integer.class)).orElse(0),
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
                            projectMember.project.status.when(1).then(1).otherwise(0).sum(),
                            projectMember.project.status.when(2).then(1).otherwise(0).sum())
                    .from(projectMember)
                    .join(projectMember.member)
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
                int ongoingProjectCont = Optional.ofNullable(memberTuple.get(4, Integer.class)).orElse(0);
                int completedProjectCount = Optional.ofNullable(memberTuple.get(5, Integer.class)).orElse(0);

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

                members.add(new ProjectDetailResponse.Member(memberId, nickname, imgUrl, github, ongoingProjectCont, completedProjectCount, memberTechStacks));
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
}
