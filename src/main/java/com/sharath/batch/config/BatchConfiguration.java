package com.sharath.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.sharath.batch.model.Stock;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Value("${file.input}")
    private String fileInput;

    @Bean
    public FlatFileItemReader<Stock> reader() {
        return new FlatFileItemReaderBuilder<Stock>().name("stockItemReader")
            .resource(new ClassPathResource(fileInput))
            .delimited()
            .names(new String[] { "quarter","stock","date","open","high","low","close","volume","percent_change_price",
            		"percent_change_volume_over_last_wk","previous_weeks_volume","next_weeks_open","next_weeks_close",
            		"percent_change_next_weeks_price","days_to_next_dividend","percent_return_next_dividend" })
            .fieldSetMapper(new BeanWrapperFieldSetMapper<Stock>() {{
                setTargetType(Stock.class);
             }})
            .build();
    }

    @Bean
    public StockItemProcessor processor() {
        return new StockItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Stock> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Stock>().itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("INSERT INTO stock (quarter,stock,date,open,high,low,close,volume,percent_change_price,"
            		+ "percent_change_volume_over_last_wk,previous_weeks_volume,next_weeks_open,"
            		+ "next_weeks_close,percent_change_next_weeks_price,days_to_next_dividend,"
            		+ "percent_return_next_dividend) VALUES (:quarter,:stock,:date,:open,:high,:low,:close,"
            		+ ":volume,:percent_change_price,:percent_change_volume_over_last_wk,"
            		+ ":previous_weeks_volume,:next_weeks_open,:next_weeks_close,:percent_change_next_weeks_price,"
            		+ ":days_to_next_dividend,:percent_return_next_dividend)")
            .dataSource(dataSource)
            .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step1)
            .end()
            .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Stock> writer) {
        return stepBuilderFactory.get("step1")
            .<Stock, Stock> chunk(10)
            .reader(reader())
            .processor(processor())
            .writer(writer)
            .build();
    }

}
