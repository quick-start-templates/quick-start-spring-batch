package me.kezhenxu94.demo.spring.batch.steps;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Map;

/**
 * Created by kezhenxu at 2018/9/28 22:54
 *
 * @author kezhenxu (kezhenxu94 at 163 dot com)
 */
public class StepCreateSolrCollection implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        final Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
        final Object businessCode = jobParameters.get("businessCode");
        System.out.println("creating solr collection " + businessCode);
        return RepeatStatus.FINISHED;
    }
}
