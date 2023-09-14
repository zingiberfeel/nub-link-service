package com.zingiberfeel.nub.service;

import com.zingiberfeel.nub.exception.SnippetNotFoundException;
import com.zingiberfeel.nub.model.Snippet;
import com.zingiberfeel.nub.repository.SnippetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SnippetService {

    private final SnippetRepository snippetRepository;

    @Autowired
    public SnippetService(SnippetRepository snippetRepository) {
        this.snippetRepository = snippetRepository;
    }

    public Snippet createSnippet(Snippet snippet) {
        return snippetRepository.save(snippet);
    }

    public Snippet getSnippetByHash(String hash) {
        return snippetRepository.findByHash(hash);
    }

}
