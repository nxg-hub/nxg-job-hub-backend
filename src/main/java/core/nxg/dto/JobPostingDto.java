package core.nxg.dto;

import jakarta.persistence.Temporal;
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
    private String job_title;
    private String job_description;
    private String company_bio;
    private String salary;
    private String job_type;
    private LocalDate deadline;
    private LocalDate created_at;
    private String requirements;
    private String job_location;
    private List<String> tags;

}
