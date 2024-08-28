package site.hesil.latteve_spring.domains.search.service;

import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.springframework.stereotype.Service;
import site.hesil.latteve_spring.domains.member.repository.MemberRepository;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.repository.ProjectRepository;

import java.io.IOException;
import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.search.service
 * fileName       : SearchProjectService
 * author         : Heeseon
 * date           : 2024-08-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-28        Heeseon       최초 생성
 */
@Service
@RequiredArgsConstructor
public class SearchService {
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final OpenSearchClient openSearchClient;

    public void indexProjectsToOpenSearch() throws IOException {
        List<Project> projects = projectRepository.findAll();

        //Project -> Index
        for(Project project : projects) {
            IndexRequest indexRequest = new IndexRequest.Builder()
                    .index("projects")
                    .id(project.getProjectId().toString()) // Elsticsearch의 Id로 사용
                    .source("name", project.getName(),
                            "description", project.getDescription(),
                            "imgUrl", project.getImgUrl(),
                            "status", project.getStatus(),
                            "duration" );
            openSearchClient.index(indexRequest);
        }



    }
}
