package cc.htdf.msgcloud.msgcenter.coder;

/**
 * author: JT
 * date: 2020/10/7
 * title:
 */
public class WavCoderCommand implements Command {

    private String command;

    public WavCoderCommand(String utilpath, String inputPath, String outPath) {
        StringBuilder sb = new StringBuilder(utilpath);
        sb.append(' ')
                .append(" -i ").append(inputPath).append(' ')
                .append(" -ar 16000 -ac 1 -acodec pcm_s16le ")
                .append(" -f ").append(" wav ")
                .append(outPath).append(' ')
                .append(" -y ");
        this.command = sb.toString();
    }

    @Override
    public String getCommand() {
        return this.command;
    }
}
