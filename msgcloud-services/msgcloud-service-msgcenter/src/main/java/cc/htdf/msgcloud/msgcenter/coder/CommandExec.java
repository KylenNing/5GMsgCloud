package cc.htdf.msgcloud.msgcenter.coder;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import java.io.IOException;

/**
 * Created by JT on 2018/5/9
 */
public class CommandExec {


    public static void run(Command command) {

        CommandLine cmd = CommandLine.parse(command.getCommand());
        DefaultExecutor executor = new DefaultExecutor();
        try {
            executor.execute(cmd);
        } catch (IOException e) {
            throw new RuntimeException("Running CommandLine Exception! CommandLine:" + command.getCommand());
        }
    }
}
