package riskgame.amazons3;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import riskgame.commands.CommandManager;

import java.io.*;

public class AmazonS3 {

    public CommandManager cm;

    public AmazonS3(CommandManager commandManager){
        this.cm = commandManager;
    }

    public void saveCommandManagerState() {
        try {
            FileOutputStream f = new FileOutputStream(new File("text.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(cm);

            o.close();
            f.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error initializing stream while saving");
        }

    }
    public CommandManager readCommandManagerState() {
        CommandManager commandManager = null;
        try {

            FileInputStream fi = new FileInputStream(new File("text.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects

            commandManager = (CommandManager) oi.readObject();
            oi.close();
            fi.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream while reading");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return commandManager;

    }

    public  String saveFileToAmazon() throws Exception {


        BasicAWSCredentials awsCreds = new BasicAWSCredentials(Credentials.access_key_id, Credentials.secret_access_key);

        AmazonS3Client s3Client = new AmazonS3Client(awsCreds);

        BucketUtils.deleteAllBuckets(s3Client);


        String newBucketName = "testbucket42" + System.currentTimeMillis();
        s3Client.createBucket(newBucketName);
        s3Client.putObject(newBucketName, "text.txt",  new File("text.txt"));

        return newBucketName;

    }
    public static void main(String[] args) throws Exception {


    }

}
