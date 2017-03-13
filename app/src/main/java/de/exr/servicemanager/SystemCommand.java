package de.exr.servicemanager;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by felix on 29.06.15.
 */
public class SystemCommand {

    protected final static boolean IS_SYSTEM_APP = true;

    protected static String exec(String cmd) {
        try {
            Process process;
            if (IS_SYSTEM_APP) {
                process = Runtime.getRuntime().exec(cmd);
            } else {
                process = Runtime.getRuntime().exec("su");
                DataOutputStream os = new DataOutputStream(
                        process.getOutputStream());
                os.writeBytes(cmd);
                os.writeBytes("\n");
                os.writeBytes("exit\n");
                os.flush();
                os.close();
            }
            String out = "";
            out += readStream(process.getErrorStream());
            out += readStream(process.getInputStream());
            process.waitFor();
            return out;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String readStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = "";
        String out = "";
        while ( (line=br.readLine()) != null) {
            out += line;
        }
        return out;
    }
}
