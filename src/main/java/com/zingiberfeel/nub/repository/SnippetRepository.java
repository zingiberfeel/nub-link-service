package com.zingiberfeel.nub.repository;

import com.zingiberfeel.nub.model.Snippet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface SnippetRepository extends MongoRepository <Snippet, String> {

    Snippet findByHash(String hash);

}
