package hudson.plugins.sectioned_view;

import java.io.*;
import org.apache.commons.io.FileUtils;
import java.nio.charset.*;

public class bash_exec {

    public static String run(String script, String scriptPath, String lang) throws InterruptedException, IOException {

        // A Runtime object has methods for dealing with the OS
        Runtime r = Runtime.getRuntime();
        Process p;     // Process tracks one external native process
        BufferedReader is;  // reader for output of process
        BufferedReader errors;  // reader for errors of process
        String line;
        String result = "";
        File tmpFile = File.createTempFile("date", ".dump");

        if ( scriptPath.matches(" *") ) {
            FileUtils.writeStringToFile(tmpFile, script, StandardCharsets.UTF_8);
            scriptPath = tmpFile.getAbsolutePath();
        }

        String[] cmd = new String[]{"/usr/bin/env", lang, scriptPath};

        p = r.exec(cmd);

        // getInputStream gives an Input stream connected to
        // the process p's standard output. Just use it to make
        // a BufferedReader to readLine() what the program writes out.
        is = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
        errors = new BufferedReader(new InputStreamReader(p.getErrorStream(), "UTF-8"));

        StringBuffer bufout = new StringBuffer();
        try {
            while ((line = errors.readLine()) != null) {
                System.err.println(line);
            }
            while ((line = is.readLine()) != null) {
                bufout.append(line + System.lineSeparator());
            }
            result = bufout.toString();
        } finally {
            is.close();
            errors.close();
        }

        try {
            p.waitFor();  // wait for process to complete
        } catch (InterruptedException e) {
            System.err.println(e);  // "Can'tHappen"
        }
        System.err.println("TableSectionView plugin: Process done, exit status was " + p.exitValue());

        tmpFile.deleteOnExit();

        if ( p.exitValue() > 0 ) {
            return "Error script syntax";
        } else {
            return result;
        }
    }
}