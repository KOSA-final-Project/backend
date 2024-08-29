package site.hesil.latteve_spring.domains.project.repository.custom;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import site.hesil.latteve_spring.domains.job.domain.QJob;
import site.hesil.latteve_spring.domains.member.domain.QMember;
import site.hesil.latteve_spring.domains.memberStack.domain.QMemberStack;
import site.hesil.latteve_spring.domains.project.domain.QProject;
import site.hesil.latteve_spring.domains.project.domain.projectMember.QProjectMember;
import site.hesil.latteve_spring.domains.project.domain.recruitment.QRecruitment;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse;
import site.hesil.latteve_spring.domains.projectStack.domain.QProjectStack;
import site.hesil.latteve_spring.domains.techStack.domain.QTechStack;

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

@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 프로젝트 상세 정보 조회
    @Override
    public ProjectDetailResponse getProjectDetail(Long projectId) {
        QProject project = QProject.project;
        QProjectStack projectStack = QProjectStack.projectStack;
        QTechStack techStack = QTechStack.techStack;
        QProjectMember projectMember = QProjectMember.projectMember;
        QMember member = QMember.member;
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

        // 프로젝트 기술 스택 조회
        List<String> projectTechStack = queryFactory
                .select(new CaseBuilder()
                        .when(projectStack.techStack.techStackId.eq(1L)).then(projectStack.customStack)
                        .otherwise(techStack.imgUrl))
                .from(projectStack)
                .leftJoin(techStack).on(projectStack.techStack.techStackId.eq(techStack.techStackId))
                .where(projectStack.project.projectId.eq(projectId))
                .fetch();

        // 리더 정보 조회 //
        // 리더 기본 정보 조회
        Tuple leaderInfo = queryFactory
                .select(projectMember.member.memberId,
                        member.nickname,
                        member.imgUrl,
                        member.github)
                .from(projectMember)
                .join(member).on(projectMember.member.memberId.eq(member.memberId))
                .where(projectMember.project.projectId.eq(projectId).and(projectMember.isLeader.eq(true)))
                .fetchOne();

        // 리더 기술 스택 조회
        List<String> leaderTechStack = queryFactory
                .select(new CaseBuilder()
                        .when(memberStack.techStack.techStackId.eq(1L)).then(memberStack.customStack)
                        .otherwise(techStack.imgUrl))
                .from(memberStack)
                .leftJoin(techStack).on(memberStack.techStack.techStackId.eq(techStack.techStackId))
                .where(memberStack.member.memberId.eq(leaderInfo.get(projectMember.member.memberId)))
                .fetch();

        // 최종 Leader 객체 생성
        ProjectDetailResponse.Leader leader = new ProjectDetailResponse.Leader(
                leaderInfo.get(projectMember.member.memberId),
                leaderInfo.get(member.nickname),
                leaderInfo.get(member.imgUrl),
                leaderInfo.get(member.github),
                leaderTechStack
        );

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

        // 멤버 조회
        List<ProjectDetailResponse.Recruitment> recruitments = new ArrayList<>();
        for (Tuple tuple : recruitmentTuples) {
            Long jobId = tuple.get(recruitment.job.jobId);
            String jobName = tuple.get(job.name);
            int jobCount = Optional.ofNullable(tuple.get(2, Integer.class)).orElse(0);  // index로 접근하고 Integer로 가져온 후 자동 언박싱

            // 각 멤버 기본 정보
            List<Tuple> memberTuples = queryFactory
                    .select(member.memberId,
                            member.nickname,
                            member.imgUrl,
                            member.github)
                    .from(projectMember)
                    .join(member).on(projectMember.member.memberId.eq(member.memberId))
                    .where(projectMember.project.projectId.eq(projectId)
                            .and(projectMember.job.jobId.eq(jobId))
                            .and(projectMember.acceptStatus.eq(1))) // 프로젝트 참여가 승인된 멤버만 가져옴
                    .fetch();

            List<ProjectDetailResponse.Member> members = new ArrayList<>();

            // 각 멤버의 기술 스택
            for (Tuple memberTuple : memberTuples) {
                Long memberId = memberTuple.get(member.memberId);
                String nickname = memberTuple.get(member.nickname);
                String imgUrl = memberTuple.get(member.imgUrl);
                String github = memberTuple.get(member.github);

                List<String> memberTechStack = queryFactory
                        .select(new CaseBuilder()
                                .when(memberStack.techStack.techStackId.eq(1L)).then(memberStack.customStack)
                                .otherwise(techStack.imgUrl))
                        .from(memberStack)
                        .leftJoin(techStack).on(memberStack.techStack.techStackId.eq(techStack.techStackId))
                        .where(memberStack.member.memberId.eq(memberId))
                        .fetch();

                members.add(new ProjectDetailResponse.Member(memberId, nickname, imgUrl, github, memberTechStack));
            }

            recruitments.add(new ProjectDetailResponse.Recruitment(jobId, jobName, jobCount, members));
        }

        return ProjectDetailResponse.builder()
                .projectId(projectInfo.get(project.projectId))
                .name(projectInfo.get(project.name))
                .description(projectInfo.get(project.description))
                .projectImg(projectInfo.get(project.imgUrl))
                .projectTechStack(projectTechStack)
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
                .build();
    }
}
