package com.example.trend.ai.service;


import com.example.trend.ai.dto.CurrentItem;
import com.example.trend.ai.dto.MessageInput;
import com.example.trend.ai.dto.PreviousConversation;
import com.example.trend.ai.dto.ResponseDto;
import com.example.trend.ai.mapper.AIMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import okhttp3.MediaType;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService{

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Autowired
    private final AIMapper aiMapper;

    private final ObjectMapper objectMapper;

    @Override
    public int createNewRoom(String userId) {
        int result = aiMapper.insertNewRoom(userId);
        return result;
    }

    @Transactional
    @Override
    public List<ResponseDto> getChatResponse(MessageInput messageInput) {
        String query = messageInput.getQuery();
        String previousConversation = getPreviousConversation(messageInput.getRoomId());
        String currentItems = getCurrentItems();

        /*
        이전 대화 내용이 존재하면 대화 내용을 포함
        one-shot learning을 이용해 유사한 예시를 전달
         */
        String prompt = String.format(
                """
                이전 대화 내용:
                %s
                
                물품 목록:
                %s
                
                반환 양식:
                [
                    {
                        itemId,
                        itemName,
                        reason
                    },
                    {
                        itemId,
                        itemName,
                        reason
                    }
                ]
                
                예시:
                  물품 목록:
                  물품 ID: 1, 물품 이름: 블루투스 스피커, 물품 카테고리: 전자기기 > 휴대용 > 스피커, 물품 설명: 휴대 가능한 블루투스 스피커입니다.
                  물품 ID: 2, 물품 이름: 서핑 보드, 물품 카테고리: 액티비티 > 물놀이 용품 > 보드, 물품 설명: 실사용 5회 이상의 서핑보드입니다. 튼튼합니다.
                  물품 ID: 3, 물품 이름: 4인용 텐트, 물품 카테고리: 액티비티 > 캠핑 용품 > 텐트, 물품 설명: 4명이서 넓게 사용할 수 있습니다.
                
                  입력 예시: 바닷가에 놀러가려는데, 어떤 물품들을 빌리면 좋을까?
                
                  반환 예시:
                  [
                      {
                          itemId: 2,
                          itemName: "서핑 보드",
                          reason: "바닷가에 놀러 간다면, 서핑 보드는 필수죠."
                      },
                      {
                          itemId: 1,
                          itemName: "블루투스 스피커",
                          reason: "바닷가에선 블루투스 스피커를 이용해 더욱 활기찬 여행을 즐길 수 있습니다."
                      }
                  ]
                
                가이드라인:
                - 이전 대화 내용을 가장 중요한 참고 자료로 사용하세요. 질문과 관련된 맥락을 파악하기 위해 반드시 이전 대화 내용을 철저히 분석하고 활용하세요.
                - 이전 대화 내용이 존재하지 않는 경우에만 해당 내용을 제외하고 답변을 생성하세요.
                - 이전 대화 내용을 중심으로 질문의 맥락을 이해하고, 질문에서 요구하는 내용을 충족하기 위해 필요한 정보를 도출하세요.
                - 주어진 예시를 참고하여 답변을 생성하되, 이전 대화 내용과 질문의 요구사항이 충돌하지 않도록 하세요.
                - 반환 양식에 맞추어 답변을 작성하세요. 반환 시 "반환:" 및 "[반환]"과 같은 표시는 모두 제외하고, 오로지 지정된 JSON 형식에 맞춰 반환하세요.
                - 물품 목록에서 현재 질문과 가장 관련성이 높은 물품을 최소 2개 이상 선택하세요. 선택 이유를 작성할 때, 이전 대화 내용, 물품의 설명, 질문의 요구사항을 종합적으로 고려하세요.
                - 물품의 카테고리와 설명을 기반으로 선택 이유를 구체적이고 상세히 작성하세요. 이전 대화 내용에서 사용자가 선호하는 물품이나 상황과 관련된 힌트를 활용하세요.
                - 답변을 작성할 때, 이전 대화 내용을 가장 먼저 고려하여 질문의 맥락과 물품 목록 간의 연결성을 명확히 보여주세요.
                
                현재 질문:
                %s
                """, previousConversation, currentItems, query);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", "gpt-4");
        requestBody.put("temperature", 0.7);

        ObjectNode userMessage = objectMapper.createObjectNode();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);

        requestBody.set("messages", objectMapper.createArrayNode().add(userMessage));

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        int roomId = messageInput.getRoomId();
        aiMapper.insertQuery(roomId, query);

        System.out.println(prompt);

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            ObjectNode responseJson = (ObjectNode) objectMapper.readTree(responseBody);
            String generatedText = responseJson
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            // 답변이 json 형식으로 넘어오지만, String 타입이므로 이를 json parsing
            JsonNode responseRootNode = objectMapper.readTree(generatedText);
            List<ResponseDto> responseDto = objectMapper.convertValue(responseRootNode, new TypeReference<>() {});

            // db에는 JSON 형식으로 저장하는 것보다, 다시 String으로 변환해서 정형화 후 저장
            String convertedResponse = convertJsonToString(responseDto);

            System.out.println(generatedText);
            System.out.println(convertedResponse);

            // DB에 입력 쿼리 및 답변 삽입
            aiMapper.insertResponse(roomId, convertedResponse);

            return responseDto;
            
        } catch (IOException e) {
            throw new RuntimeException("답변 생성 중 오류 발생: " + e.getMessage(), e);
        }
    }

    @Override
    public int clearRoom(int roomId) {
        return aiMapper.deleteMessages(roomId);
    }

    private String getPreviousConversation(int roomId) {
        List<PreviousConversation> previousConversations = aiMapper.selectPreviousConversation(roomId);
        // 현재는 가장 마지막에 보낸 채팅이 0번에 위치하므로, reverse 필요
        Collections.reverse(previousConversations);
        StringBuilder conversation = new StringBuilder();

        for(PreviousConversation previousConversation: previousConversations) {
            conversation.append("[").append(previousConversation.getSender()).append("]").append("\n");
            conversation.append(previousConversation.getContent()).append("\n");
        }

        return conversation.toString();
    }

    private String getCurrentItems() {
        List<CurrentItem> currentItems = aiMapper.selectCurrentItems();
        StringBuilder items = new StringBuilder();

        for(CurrentItem currentItem: currentItems) {
            items.append("물품 ID: ").append(currentItem.getItemId()).append(", ");
            items.append("물품 이름: ").append(currentItem.getItemName()).append(", ");
            items.append("물품 카테고리: ").append(currentItem.getCategory()).append(", ");
            items.append("물품 설명: ").append(currentItem.getItemContent()).append("\n");
        }

        return items.toString();
    }

    private String convertJsonToString(List<ResponseDto> response) {
        StringBuilder result = new StringBuilder();

        // 데이터를 "itemName, reason" 형식으로 변환
        for (ResponseDto item : response) {
            result.append(item.getItemName())
                    .append(", ")
                    .append(item.getReason())
                    .append("\n"); // 줄바꿈 추가
        }

        return result.toString();
    }
}
