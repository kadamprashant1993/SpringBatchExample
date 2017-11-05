package com.org;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.org.model.JobBean;

public class Task2 implements Tasklet{

	
	private JobBean job;
	
	

	public void setJob(JobBean job) {
		this.job = job;
	}
	
	
	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		
		JobBean jobtemp=null;
		
		for (int i = 0; i < 10; i++) {
			jobtemp=(JobBean) job.clone();
			if (i==2||i==3||i==5) {
				jobtemp.setId("6");
			}
			
			System.out.println("Step 2::"+jobtemp.getId());
			
		}
		
		
		
		return RepeatStatus.FINISHED;
	}

}
