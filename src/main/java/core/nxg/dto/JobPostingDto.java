package core.nxg.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class JobPostingDto {
    private Long employerID;
    private String job_title;
    private String job_description;
    private String company_bio;
    private String salary;
    private String jobType;
    private LocalDate deadline;
    private String requirements;
    private String job_location;
    private String tags;

}
