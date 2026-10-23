package com.springai.ai.service;

import com.springai.faq.service.FaqService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private FaqService faqService;

    private static final String SYSTEM_PROMPT = """
            당신은 친절한 고객 서비스 챗봇입니다.
            사용자의 질문에 대해 FAQ 데이터베이스를 검색하여 정확한 답변을 제공하세요.
            검색 결과가 없으면 "해당 내용에 대한 FAQ가 없습니다. 고객센터(1234-5678)로 문의해주세요."라고 안내하세요.
            """;

    // AI가 사용자 질문을 분석하여 필요시 자동으로 searchFaq() 메서드를 호출
    public Flux<String> streamChatResponse(String userMessage){
        return chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .messages(UserMessage.builder().text(userMessage).build())
                .tools(faqService)  // FaqService의 @Tool 어노테이션이 붙은 메서드를 AI가 호출 가능하도록 등록
                .stream()
                .content();
    }
}
