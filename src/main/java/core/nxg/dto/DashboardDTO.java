package core.nxg.dto;

import core.nxg.entity.SavedJobs;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

@Setter
@Getter
@RequiredArgsConstructor
public class DashboardDTO extends RepresentationModel<DashboardDTO> {

    private UserResponseDto privacy;
    private List<ApplicationDTO> my_applications;
    private List<SavedJobs> saved_jobs;
    
}
