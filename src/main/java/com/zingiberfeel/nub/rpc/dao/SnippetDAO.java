package com.zingiberfeel.nub.rpc.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SnippetDAO {

    private String text;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
    private LocalDateTime lifetime;
    private String extension;

}
