package com.springai.faq.repository;

import com.springai.faq.entity.Faq;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FaqRepository extends CrudRepository<Faq, Long> {

    @Query(value = """
            SELECT * FROM faq
            WHERE question &@~ :keyword OR answer &@~ :keyword
            """, nativeQuery = true)
    List<Faq> searchByKeyword(@Param("keyword") String keyword);
}
