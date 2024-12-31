package com.example.momowas.user.util;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import net.nurigo.sdk.message.model.Message;

import java.util.Random;

@Component
public class SmsUtil {

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.api.number}")
    private String fromNumber;

    DefaultMessageService messageService;

    @PostConstruct
    public void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    public SingleMessageSentResponse sendOne(String toPhoneNumber, String verificationCode) {
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom(fromNumber);
        message.setTo(toPhoneNumber);
        message.setText("[모두의 모임,momo] 아래의 인증번호를 입력해주세요\n" + verificationCode);

        return this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    public String validationCode(String validation, HttpSession ss) {
        if(ss.getAttribute("message_id") == null || ss.getAttribute("validation") == null) return "인증번호 만료, 처음부터 다시 시도해주세요";
        else {
            if(ss.getAttribute("validation").equals(validation)) return "인증 완료";
            else return "인증 실패";
        }
    }

    public String generateRandomNumber() {
        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            numStr.append(rand.nextInt(10));
        }
        return numStr.toString();
    }

}