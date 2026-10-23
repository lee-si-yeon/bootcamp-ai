package com.springai;

import com.springai.ai.service.ChatService;
import com.springai.faq.entity.Faq;
import com.springai.faq.repository.FaqRepository;
import com.springai.faq.service.FaqService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class SpringaiApplicationTests {

	@Autowired
	private FaqRepository faqRepository;

	@Autowired
	private FaqService faqService;

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private ChatService chatService;


	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("FAQ 전문 검색 테스트")
	void t1(){
		// given
		String keyword = "배송";

		// when
		List<Faq> results = faqService.searchFaq(keyword);

		// then
		assertFalse(results.isEmpty());
		assertTrue(results.stream().anyMatch(f -> f.getQuestion().contains("배송") || f.getAnswer().contains("배송")));
		System.out.println("FTS 검색 결과:");
		results.forEach(System.out::println);
	}

	@Test
	@DisplayName("FAQ 기반 AI 챗봇 테스트")
	void t2(){
		// given
		String userMessage = "배송은 얼마나 걸려요?";

		// when & then
		String response = chatService.streamChatResponse(userMessage)
				.collectList()
				.map(list -> String.join("", list))
				.block();

		assertNotNull(response);
		assertFalse(response.isEmpty());
		System.out.println("AI 응답: " + response);
	}

	@Test
	@DisplayName("SSE 스트리밍 API 테스트")
	void t3(){
		webTestClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/api/v1/ai/chat")
						.queryParam("message", "환불 방법을 알려주세요")
						.build())
				.accept(MediaType.TEXT_EVENT_STREAM)
				.exchange()
				.expectStatus().isOk()
				.returnResult(String.class)
				.getResponseBody()
				.collectList()
				.map(list -> String.join("", list))
				.doOnNext(response -> {
					assertNotNull(response);
					assertFalse(response.isEmpty());
					System.out.println("SSE 응답: " + response);
				})
				.block();
	}
}
