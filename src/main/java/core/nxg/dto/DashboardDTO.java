package core.nxg.dto;

import core.nxg.entity.Application;
import core.nxg.entity.SavedJobs;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.RepresentationModel;

@Setter
@Getter
@RequiredArgsConstructor
public class DashboardDTO extends RepresentationModel<DashboardDTO> {


    


    private UserResponseDto profile;
    private TechTalentDTO other_profile;
    private List<Application> my_applications;
    private List<SavedJobs> saved_jobs;


    
}
