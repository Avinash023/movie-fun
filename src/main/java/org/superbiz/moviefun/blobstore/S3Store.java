package org.superbiz.moviefun.blobstore;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.tika.Tika;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

import static java.nio.file.Files.readAllBytes;

public class S3Store implements BlobStore {

    private final String photoStorageBucket;
    private final AmazonS3Client s3Client;

    public S3Store( AmazonS3Client s3Client, String photoStorageBucket ) {
        this.s3Client = s3Client;
        this.photoStorageBucket = photoStorageBucket;
    }

    @Override
    public void put( Blob blob ) throws IOException {
        ObjectMetadata o = new ObjectMetadata();
        o.setContentType( blob.contentType );
        s3Client.putObject( photoStorageBucket, blob.name ,blob.inputStream, o );
    }

    @Override
    public Optional<Blob> get( String name ) throws IOException {
        S3Object s3Object = s3Client.getObject( photoStorageBucket, name );
        return Optional.of( new Blob(s3Object.getKey(),s3Object.getObjectContent(), s3Object.getObjectMetadata().getContentType()));
        //return Optional.empty();
    }

    @Override
    public void deleteAll() {

    }
}
