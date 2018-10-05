package riskgame.amazons3;
import com.amazonaws.auth.BasicAWSCredentials;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;


    public class Example {

        public static void main(String[] args)throws Exception {


            BasicAWSCredentials awsCreds = new BasicAWSCredentials(Credentials.access_key_id, Credentials.secret_access_key);

            AmazonS3Client s3Client = new AmazonS3Client(awsCreds);

            BucketUtils.deleteAllBuckets(s3Client);


            String newBucketName = "testbucket42" + System.currentTimeMillis();
            s3Client.createBucket(newBucketName);
            s3Client.putObject(newBucketName, "text.txt",  new File("text.txt"));

            S3Object testObject = null;
            testObject = s3Client.getObject(new GetObjectRequest(newBucketName, "text.txt"));
            System.out.println("Content: ");
            displayTextInputStream(testObject.getObjectContent());

        }


       

        private static void displayTextInputStream(InputStream input) throws IOException {
            // Read the text input stream one line at a time and display each line.
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println();
        }




}
