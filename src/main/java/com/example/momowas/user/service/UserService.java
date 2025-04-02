package com.example.momowas.user.service;

import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.review.domain.CrewMemberReview;
import com.example.momowas.s3.service.S3Service;
import com.example.momowas.user.domain.User;
import com.example.momowas.user.dto.UserDto;
import com.example.momowas.user.dto.UserInfoUpdateReqDto;
import com.example.momowas.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CrewMemberService crewMemberService;
    private final S3Service s3Service;

    public void create(User user){
        userRepository.save(user);
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    public boolean isNicknameExists(String nickName){
        return userRepository.existsByNickname(nickName);
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
    }

    public User findUserByProviderId(String providerId){
        return userRepository.findByProviderId(providerId);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
    }
    public User findUserByCp(String cp){
        return userRepository.findByCp(cp).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
    }

    //프로필 업로드
    @Transactional
    public String updateProfileImage(Long userId, MultipartFile file) throws IOException {
        User user = findUserById(userId);
        String fileUrl = s3Service.uploadImage(file, "profile");

        user.updateProfileImage(fileUrl);
        return fileUrl;
    }

    public UserDto getMyInfo(Long userId){
        User user = findUserById(userId);
        double mannerRating = calculateMannersRatingForUser(user);
        return UserDto.fromEntity(user, mannerRating);
    }

    @Transactional
    public UserDto updateMyInfo(Long userId, UserInfoUpdateReqDto userInfoUpdateReqDto){
        User user = findUserById(userId);
        //닉네임 중복 확인
        if(isNicknameExists(userInfoUpdateReqDto.getNickname())){
            throw new BusinessException(ExceptionCode.ALREADY_EXISTS);
        }
        user.updateUserInfo(
                userInfoUpdateReqDto.getNickname(),
                userInfoUpdateReqDto.getCp(),
                userInfoUpdateReqDto.getGender(),
                userInfoUpdateReqDto.getAge());

        double mannerRating = calculateMannersRatingForUser(user);
        return UserDto.fromEntity(user, mannerRating);

    }


    public double calculateMannersRatingForUser(User user) {
        List<CrewMember> members = crewMemberService.findCrewMemberByUser(user);
        if(members.isEmpty()){
            return 36.5;
        }
        double result = 0.0;

        for (CrewMember cm : members) {
            result += cm.countMannersRating();
        }
        return result / members.size();
    }

}
