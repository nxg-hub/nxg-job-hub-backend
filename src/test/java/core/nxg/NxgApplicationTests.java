package core.nxg;

import core.nxg.configs.JwtService;
import core.nxg.entity.JobPosting;
import core.nxg.entity.User;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.*;
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
import org.springframework.mock.web.MockHttpServletRequest;

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
	private Helper <?,?>helper;


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
	private JwtService jwtService;

	@Mock
	private EmployerRepository employerRepository;

	@Mock
	private TechTalentApprovalHistoryRepository techTalentApprovalHistoryRepository;

	@Mock
	private EmployerApprovalHistoryRepository employerApprovalHistoryRepository;



	@Mock
	 private JobPostingRepository jobPostingRepository;

	@Mock
	private MockHttpServletRequest request;
	@Mock
	private JobPosting jobPosting;
	@BeforeEach
	public void setup() {

		MockitoAnnotations.openMocks(this);

		adminService = new AdminServiceImpl(secretService, helper, subscribeRepository, transactionRepository,  jobPostingRepository,modelMapper, techTalentService,employerService, userRepository,employerRepository, jwtService, employerApprovalHistoryRepository, techTalentApprovalHistoryRepository);

		request = new MockHttpServletRequest();
		request.addHeader("nxg-header", "xg...:");

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
		job.setJobID("1");
	when(secretService.decodeKeyFromHeaderAndValidate(request)).thenReturn(true);
	when(jobPostingRepository.findById(job.getJobID())).thenReturn(Optional.of(job));

	adminService.acceptJob(job.getJobID(), request);

		verify(jobPostingRepository).save(job);
	assertEquals(JobStatus.ACCEPTED, job.getJobStatus());
	assertTrue(job.isActive());
	verify(jobPostingRepository, times(1)).save(job);
}



	@Test
	public void rejectJobChangesJobStatusToRejected() {


		JobPosting job = new JobPosting();
		job.setJobStatus(JobStatus.PENDING);
		job.setJobID("1");
		when(secretService.decodeKeyFromHeaderAndValidate(request)).thenReturn(true);

		when(jobPostingRepository.findById(job.getJobID())).thenReturn(Optional.of(job));
		adminService.rejectJob(job.getJobID(), "Incomplete Profile",request);

		verify(jobPostingRepository).save(job);
		assertEquals(JobStatus.REJECTED, job.getJobStatus());
	}

	@Test
	public void suspendJobChangesJobStatusToSuspended() {


		JobPosting job = new JobPosting();
		job.setJobStatus(JobStatus.ACCEPTED);
		job.setJobID("1");
		when(jobPostingRepository.findById( job.getJobID())).thenReturn(Optional.of(job));
		when(secretService.decodeKeyFromHeaderAndValidate(request)).thenReturn(true);
		adminService.suspendJob(job.getJobID(), "Job flagged!",request);

		verify(jobPostingRepository).save(job);
		assertEquals(JobStatus.SUSPENDED, job.getJobStatus());
	}

//	@Test
//	public void suspendUserChangesUserStatusToDisabled() {
//		User user = new User();
//		user.setEnabled(true);
//		user.setId("1");
//		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//		when(secretService.decodeKeyFromHeaderAndValidate(request)).thenReturn(true);
//		adminService.suspendUser(user.getId(), "Violation of Policy",request);
//
//		verify(userRepository).save(user);
//		assertFalse(user.isEnabled());
//	}

	@Test
	public void acceptJobThrowsExceptionWhenJobNotFound() {
		when(secretService.decodeKeyFromHeaderAndValidate(request)).thenReturn(true);

		when(jobPostingRepository.findById(anyString())).thenReturn(Optional.empty());

		assertThrows(NoSuchElementException.class, () -> adminService.acceptJob(jobPosting.getJobID(), request));
	}

//	@Test
//	public void suspendUserThrowsExceptionWhenUserNotFound() {
//		when(secretService.decodeKeyFromHeaderAndValidate(request)).thenReturn(true);
//
//		when(userRepository.findById(anyString())).thenReturn(Optional.empty());
//
//		assertThrows(UserNotFoundException.class, () -> adminService.suspendUser(user.getId(), "Violation of Policy", request));
//	}





}
