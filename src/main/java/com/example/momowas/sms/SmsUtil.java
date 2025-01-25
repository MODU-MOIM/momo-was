package com.example.momowas.sms;

import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.schedule.domain.Schedule;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SmsUtil {

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.api.number}")
    private String fromNumber;

    private DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    public SingleMessageSentResponse sendOne(String toPhoneNumber, HttpSession session) {
        String verificationCode = generateRandomNumber();
        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(toPhoneNumber);
        message.setText("[모두의 모임, momo] 인증번호: " + verificationCode);

        // 세션에 인증 코드 저장
        session.setAttribute("validation", verificationCode);
        session.setAttribute("phoneNumber", toPhoneNumber);
        session.setMaxInactiveInterval(180);

        return this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    public boolean validateCode(String inputCode, HttpSession session) {
        String storedCode = (String) session.getAttribute("validation");
        String phoneNumber = (String) session.getAttribute("phoneNumber");

        if (storedCode == null || phoneNumber == null) {
            throw new BusinessException(ExceptionCode.EXPIRED_VERIFICATION_CODE);
        }

        if (storedCode.equals(inputCode)) {
            // 인증 성공
            session.setAttribute("isAuthenticated", true);
            return true;
        }

        return false;
    }

    public boolean isAuthenticated(HttpSession session) {
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
        return isAuthenticated != null && isAuthenticated;
    }

    public String generateRandomNumber() {
        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            numStr.append(rand.nextInt(10));
        }
        return numStr.toString();
    }

    //일정 알림 form
    public SingleMessageSentResponse sendScheduleNotification(String toPhoneNumber, String title) {
        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(toPhoneNumber);
        message.setText("[모두의 모임, momo]\n" + title +" 일정이 하루 남았습니다!");

        return this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }

}
