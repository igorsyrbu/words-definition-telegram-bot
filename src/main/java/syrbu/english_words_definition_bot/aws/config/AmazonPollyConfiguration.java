package syrbu.english_words_definition_bot.aws.config;

import com.amazonaws.auth.*;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import com.amazonaws.services.polly.model.AmazonPollyException;
import com.amazonaws.services.polly.model.DescribeVoicesRequest;
import com.amazonaws.services.polly.model.Voice;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import syrbu.english_words_definition_bot.property.AwsPollyProperties;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class AmazonPollyConfiguration {

    private final AwsPollyProperties awsPollyProperties;

    @Bean
    public AmazonPolly amazonPolly() {
        boolean isIam = awsPollyProperties.isIam();
        AWSCredentials awsCredentials = new BasicAWSCredentials(awsPollyProperties.getAccessKey(), awsPollyProperties.getSecretKey());
        AWSCredentialsProvider credentialsProvider = isIam ?
                new InstanceProfileCredentialsProvider(false) :
                new AWSStaticCredentialsProvider(awsCredentials);

        log.info("Configuring AWS client with IAM role: {} and Credential provider: {}",
                isIam, credentialsProvider.getClass().getSimpleName());
        return AmazonPollyClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(awsPollyProperties.getRegion())
                .build();
    }

    @Bean
    public Voice voice() {
        String voiceName = awsPollyProperties.getVoiceName();
        DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();
        return amazonPolly()
                .describeVoices(describeVoicesRequest)
                .getVoices().stream()
                .filter(voice -> voice.getName().equalsIgnoreCase(voiceName))
                .findFirst()
                .orElseThrow(() -> new AmazonPollyException(voiceName + " voice hasn't been found"));
    }

}
