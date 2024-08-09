package site.lawmate.lawyer.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GroupGrantee;
import com.amazonaws.services.s3.model.Permission;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

        private final String endPoint = "https://kr.object.ncloudstorage.com";
        private final String regionName = "kr-standard";  //region
        private final String accessKey = "0F0E336681D1F4A7AA41"; // 버켓의 접근키
        private final String secretKey = "A7789619DCA9F11E31FDFCD6EF05D75B8ADAC03F"; // NCP의 240703 시크릿키
        private final String bucketName = "bucket-lawmate-lawyer";  //버켓 이름

        @Bean
        public AmazonS3 s3Client() {
                return AmazonS3ClientBuilder.standard()
                        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                        .build();
        }
        @Bean
        public String bucketName() {
                return bucketName;
        }
}
