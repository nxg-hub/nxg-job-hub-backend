package core.nxg;

import core.nxg.entity.JobPosting;
import core.nxg.entity.User;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.JobPostingRepository;
import core.nxg.repository.UserRepository;
import core.nxg.service.AdminService;
import core.nxg.serviceImpl.AdminServiceImpl;
import core.nxg.serviceImpl.EmployerServiceImpl;
import core.nxg.serviceImpl.TechTalentServiceImpl;
import core.nxg.subscription.enums.JobStatus;
import core.nxg.subscription.repository.SubscribeRepository;
import core.nxg.subscription.repository.TransactionRepository;
import core.nxg.utils.Helper;
import core.nxg.utils.SecretService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;


class NxgApplicationTests {


	@InjectMocks
	private User user;

	@Mock
	private SecretService secretService;


	@Mock
	private Helper helper;


	@Mock
	private AdminService adminService;


	@Mock
	private TransactionRepository transactionRepository;


	@Mock
	private UserRepository userRepository;

	@Mock
	private  ModelMapper modelMapper;

	@Mock
	private SubscribeRepository subscribeRepository;
	@Mock
	private TechTalentServiceImpl<?> techTalentService;

	@Mock
	private EmployerServiceImpl employerService;



	@Mock
	 private JobPostingRepository jobPostingRepository;


	private HttpServletRequest request;
	@Mock
	private JobPosting jobPosting;
	@BeforeEach
	public void setup() {

		MockitoAnnotations.openMocks(this);

		adminService = new AdminServiceImpl(secretService, helper, subscribeRepository, transactionRepository,  jobPostingRepository,modelMapper, techTalentService,employerService, userRepository);



	}
	@Test
	void contextLoads() {
	}


	@Test
	public void findByEmployerIDReturnsJobPostings() {
		List<JobPosting> jobPostings = List.of(jobPosting);
		when(jobPostingRepository.findByEmployerID("employer1")).thenReturn(Optional.of(jobPostings));
		Optional<List<JobPosting>> result = jobPostingRepository.findByEmployerID("employer1");
		assertEquals(Optional.of(jobPostings), result);
	}



	@Test
	public void test_accept_valid_job_id() {


	JobPosting job = new JobPosting();
        job.setJobStatus(JobStatus.PENDING);
        job.setActive(false);
	when(jobPostingRepository.findById(job.getJobID())).thenReturn(Optional.of(job));

	// Act
        adminService.acceptJob(Long.valueOf(job.getJobID()), request);

	// Assert
	assertEquals(JobStatus.ACCEPTED, job.getJobStatus());
	assertTrue(job.isActive());
	verify(jobPostingRepository, times(1)).save(job);
}



	@Test
	public void rejectJobChangesJobStatusToRejected() {


		JobPosting job = new JobPosting();
		job.setJobStatus(JobStatus.PENDING);
		when(jobPostingRepository.findById(job.getJobID())).thenReturn(Optional.of(job));

		adminService.rejectJob(Long.valueOf(job.getJobID()), request);

		verify(jobPostingRepository).save(job);
		assertEquals(JobStatus.REJECTED, job.getJobStatus());
	}

	@Test
	public void suspendJobChangesJobStatusToSuspended() {


		JobPosting job = new JobPosting();
		job.setJobStatus(JobStatus.ACCEPTED);
		when(jobPostingRepository.findById( job.getJobID())).thenReturn(Optional.of(job));

		adminService.suspendJob(Long.valueOf(job.getJobID()), request);

		verify(jobPostingRepository).save(job);
		assertEquals(JobStatus.SUSPENDED, job.getJobStatus());
	}

	@Test
	public void suspendUserChangesUserStatusToDisabled() {
		User user = new User();
		user.setEnabled(true);
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		adminService.suspendUser(Long.valueOf(user.getId()), request);

		verify(userRepository).save(user);
		assertFalse(user.isEnabled());
	}

	@Test
	public void acceptJobThrowsExceptionWhenJobNotFound() {
		when(jobPostingRepository.findById(anyString())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> adminService.acceptJob(1L, request));
	}

	@Test
	public void suspendUserThrowsExceptionWhenUserNotFound() {
		when(userRepository.findById(anyString())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> adminService.suspendUser(1L, request));
	}





}
