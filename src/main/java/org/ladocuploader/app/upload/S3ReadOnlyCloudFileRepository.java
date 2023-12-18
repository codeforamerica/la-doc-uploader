package org.ladocuploader.app.upload;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Profile("!test")
@Slf4j
public class S3ReadOnlyCloudFileRepository implements ReadOnlyCloudFileRepository {

  private final String bucketName;
  private final AmazonS3 s3Client;
  private final TransferManager transferManager;

  public S3ReadOnlyCloudFileRepository(@Value("${form-flow.aws.access_key}") String accessKey,
    @Value("${form-flow.aws.secret_key}") String secretKey,
    @Value("${form-flow.aws.s3_bucket_name}") String s3BucketName,
    @Value("${form-flow.aws.region}") String region) {
    AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
    bucketName = s3BucketName;
    s3Client = AmazonS3ClientBuilder
      .standard()
      .withRegion(region)
      .withCredentials(new AWSStaticCredentialsProvider(credentials))
      .build();
    transferManager = TransferManagerBuilder.standard().withS3Client(s3Client).build();
  }

  @Override
  public CloudFile download(String filePath) {
    try {
      File file = new File(filePath);
      Download download = transferManager.download(bucketName, filePath, file);
      ObjectMetadata objectMetadata = download.getObjectMetadata();
      download.waitForCompletion();
      return new CloudFile(objectMetadata.getContentLength(), file);
    } catch (AmazonServiceException e) {
      log.info("AmazonServiceException", e);
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      log.info("Not a AmazonServiceException", e);
      throw new RuntimeException(e);
    }
  }
}
