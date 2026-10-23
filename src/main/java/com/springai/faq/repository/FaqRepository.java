package com.springai.faq.repository;

import com.springai.faq.entity.Faq;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface FaqRepository extends R2dbcRepository<Faq, Long> {

    @Query("""
            SELECT * FROM faq
            WHERE pgroonga_match_text(question, :keyword)
               OR pgroonga_match_text(answer, :keyword)
            """)
    Flux<Faq> searchByKeyword(String keyword);
}
