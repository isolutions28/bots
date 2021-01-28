package ua.isolutions.bot.telegram.clients.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FileDetails {

    private boolean ok;
    private Result result;

    @Data
    public static class Result {
        @JsonProperty("file_id")
        private String fileId;
        @JsonProperty("file_size")
        private int fileSize;
        @JsonProperty("file_path")
        private String filePath;

    }
}

