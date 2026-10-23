package com.springai.faq.service;

import com.springai.faq.entity.Faq;
import com.springai.faq.repository.FaqRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaqService {

    @Autowired
    private FaqRepository faqRepository;

    @Tool(description = "FAQ 데이터베이스에서 키워드로 질문과 답변을 검색합니다. 회원가입, 배송, 환불, 교환, 포인트, 쿠폰 등의 질문에 사용하세요.")  // Spring AI의 함수 호출 어노테이션
    public List<Faq> searchFaq(
            @ToolParam(description = "검색할 키워드 (예: 배송, 환불, 회원가입)") String keyword  // AI가 파라미터를 이해할 수 있도록 설명 제공
    ) {
        return faqRepository.searchByKeyword(keyword).collectList().block();
    }
}
