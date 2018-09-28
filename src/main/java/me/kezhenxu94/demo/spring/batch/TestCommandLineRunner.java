package me.kezhenxu94.demo.spring.batch;

import me.kezhenxu94.demo.spring.batch.steps.StepCreateSolrCollection;
import me.kezhenxu94.demo.spring.batch.steps.StepCreateSolrConfigSet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kezhenxu at 2018/9/28 22:31
 *
 * @author kezhenxu (kezhenxu94 at 163 dot com)
 */
@Component
public class TestCommandLineRunner implements CommandLineRunner {
    private final JobLauncher jobLauncher;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Autowired
    public TestCommandLineRunner(final JobLauncher jobLauncher,
                                 final JobBuilderFactory jobBuilderFactory,
                                 final StepBuilderFactory stepBuilderFactory) {
        this.jobLauncher = jobLauncher;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Override
    public void run(String... args) throws Exception {
        final String businessCode = "test";

        final TaskletStep createConfigSetStep = stepBuilderFactory
                .get(String.format("createSolrConfigSetStep#%s", businessCode))
                .tasklet(new StepCreateSolrConfigSet())
                .build();
        final TaskletStep createCollectionStep = stepBuilderFactory
                .get(String.format("createSolrCollectionStep#%s", businessCode))
                .tasklet(new StepCreateSolrCollection())
                .build();

        final Job job = jobBuilderFactory.get(String.format("SyncJob#%s", businessCode))
                .start(createConfigSetStep)
                .next(createCollectionStep)
                .build();

        final JobParameter businessCodeParam = new JobParameter(businessCode);
        final Map<String, JobParameter> parameters = new HashMap<>();
        parameters.put("businessCode", businessCodeParam);

        jobLauncher.run(job, new JobParameters(parameters));
    }
}
