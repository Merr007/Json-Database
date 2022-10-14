package client;

import com.beust.jcommander.*;

public class Args {
    @Parameter(names = {"-t"}, description = "Request type for server execution")
    private String type;

    @Parameter(names = {"-in"}, description = "File name with the request provided")
    private String fileName;

    @Parameter(names = {"-k"}, description = "Key")
    private String key;

    @Parameter(names = {"-v"}, description = "Value to save in database")
    private String value;

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return "Args{" +
                "type='" + type + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
