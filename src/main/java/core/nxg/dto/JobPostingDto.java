package core.nxg.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor

public class JobPostingDto {
    private String employerID;
    private String jobID;
    private String job_title;
    private String job_description;
    private String company_bio;
    private String salary;
    private String job_type;
    private LocalDate deadline;
    private LocalDateTime createdAt;
    private String requirements;
    private String employer_name;
    private String employer_profile_pic;
    private String job_location;
    private List<String> tags;

}
