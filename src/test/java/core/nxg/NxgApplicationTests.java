package core.nxg;

import core.nxg.dto.JobPostingDto;
import core.nxg.entity.JobPosting;
import core.nxg.repository.JobPostingRepository;
import core.nxg.serviceImpl.JobPostingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class NxgApplicationTests {


	@Mock
	 JobPostingRepository jobPostingRepository;

	@Mock
	private JobPosting jobPosting;
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}	@Test
	void contextLoads() {
	}


	@Test
	public void findByEmployerIDReturnsJobPostings() {
		List<JobPosting> jobPostings = List.of(jobPosting);
		when(jobPostingRepository.findByEmployerID("employer1")).thenReturn(Optional.of(jobPostings));
		Optional<List<JobPosting>> result = jobPostingRepository.findByEmployerID("employer1");
		assertEquals(Optional.of(jobPostings), result);
	}

}
