package com.org;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.org.model.JobBean;

public class App {
	public static void main(String[] args) {

		String[] springConfig  = 
			{	"spring/batch/config/database.xml", 
				"spring/batch/config/context.xml",
				"spring/batch/jobs/job-report.xml" 
			};
		
		ClassPathXmlApplicationContext context = 
				new ClassPathXmlApplicationContext(springConfig);
		
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		JobBean bean = (JobBean) context.getBean("jobbean");
		bean.setId("2");
		System.out.println("JOB::"+bean.getId());
		Job job = (Job) context.getBean("reportJob");

		try {

			JobExecution execution = jobLauncher.run(job, new JobParameters());
			//sexecution.getExecutionContext().get("jobbean");
			System.out.println(execution.getExecutionContext().get("jobbean"));
			System.out.println("Exit Status : " + execution.getStatus());

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			context.close();
		}
		
		System.out.println("Done");

	}
}
