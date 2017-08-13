import org.apache.log4j.Logger;

import java.io.*;

/**
 * Created by kernel32 on 25.07.2017.
 */
public class Public2WcamFolder {
    private static InputStream inStream = null;
    private static OutputStream outStream = null;
    private static String from = "/var/www/kernel32/data/";
    private static String to = "/var/lib/tomcat7/webapps/wcam/resources/images/";

    private static Logger log = Logger.getLogger("Public2WcamFolder");

    public static void main(String[] args) {
        moveFiles();
    }

    private static void moveFiles() {
        while (true) {
            File folder = new File(from);
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                File file = listOfFiles[i];
                if (file.isFile() && file.getName().endsWith(".jpg")) {
                    long fileSizeInit = file.length();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        ex.printStackTrace();
                        String msgError = "Public2WcamFolder: get screen size error";
                        log.error(msgError, ex);
                        Phone.sendMessage(msgError);
                    }
                    long fileSizeFinal = file.length();
                    if (fileSizeInit == fileSizeFinal) {
                        moveFile(file);
                    }
                }
            }
        }
    }

    private static void moveFile(File sourceFile) {
        inStream = null;
        outStream = null;
        try {
            File targetFile = new File(to + sourceFile.getName());
            inStream = new FileInputStream(sourceFile);
            outStream = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            int length;

            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            inStream.close();
            outStream.close();

            //delete the original file
            sourceFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
            String msgError = "Public2WcamFolder: move screen error";
            log.error(msgError, e);
            Phone.sendMessage(msgError);
        }
    }
}
