package site.hesil.latteve_spring.global.amazon.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : site.hesil.latteve_spring.global.amazon.config
 * fileName       : S3Config
 * author         : Yeong-Huns
 * date           : 2024-08-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-24        Yeong-Huns       최초 생성
 */
@Log4j2
@Configuration
public class S3Config {
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;


    @Bean
    public AmazonS3 amazonS3Client() {
        log.info("🐱😺😸😹😻😼😽🙀😿😾🐱 amazonS3Client()");

        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        return  AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .enablePathStyleAccess()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
