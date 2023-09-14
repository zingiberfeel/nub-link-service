package com.zingiberfeel.nub.model;

import com.zingiberfeel.nub.util.SecureHashing;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Document("snippets")
public class Snippet {

    @Field
    private String hash;
    @Field
    private String text;
    @Field
    private String extension;

    @Field
    @Indexed(expireAfterSeconds = 0)
    private LocalDateTime lifetime;

    public Snippet(String text, String extension, LocalDateTime lifetime) {
        this.hash = SecureHashing.sha256Hash();
        this.text = text;
        this.extension = extension;
        this.lifetime = lifetime;
    }
}
