package com.example.momowas.review.service;

import com.example.momowas.authorization.CrewManager;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.review.domain.CrewMemberReview;
import com.example.momowas.review.domain.CrewReview;
import com.example.momowas.review.dto.CrewMemberReviewReqDto;
import com.example.momowas.review.dto.CrewMemberReviewResDto;
import com.example.momowas.review.repository.CrewMemberReviewRepository;
import com.example.momowas.schedule.dto.ScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrewMemberReviewService {

    private final CrewMemberReviewRepository crewMemberReviewRepository;
    private final CrewManager crewManager;
    private final CrewMemberService crewMemberService;

    public Long createCrewMemberReview(Long crewId, Long writerId, Long targetId, CrewMemberReviewReqDto crewMemberReviewReqDto){

        CrewMember target = crewMemberService.findCrewMemberById(targetId);
        CrewMember writer = crewMemberService.findCrewMemberByCrewAndUser(writerId, crewId);

        //같은 크루 멤버인지 확인
        if(!crewManager.hasCrewPermission(crewId, target.getUser().getId()) || !crewManager.hasCrewPermission(crewId, writerId)){
            throw new BusinessException(ExceptionCode.NOT_FOUND_CREW_MEMBER);
        }

       CrewMemberReview crewMemberReview = CrewMemberReview.builder()
               .comment(crewMemberReviewReqDto.getComment())
               .rating(crewMemberReviewReqDto.getRating())
               .target(target)
               .writer(writer)
               .createdAt(LocalDateTime.now())
               .build();
       crewMemberReviewRepository.save(crewMemberReview);

       return crewMemberReview.getId();

    }

    @Transactional
    public void updateCrewReview(Long reviewId, Long userId, CrewMemberReviewReqDto crewMemberReviewReqDto){
        CrewMemberReview crewMemberReview = crewMemberReviewRepository.findById(reviewId).orElseThrow(()->new BusinessException(ExceptionCode.NOT_FOUND_REVIEW));
        validateWriter(crewMemberReview.getWriter().getUser().getId(), userId);
        crewMemberReview.updateReview(crewMemberReviewReqDto.getComment(), crewMemberReviewReqDto.getRating());
    }

    @Transactional
    public void deleteCrewMemberReview(Long reviewId, Long userId) {
        CrewMemberReview crewMemberReview = crewMemberReviewRepository.findById(reviewId).orElseThrow(()->new BusinessException(ExceptionCode.NOT_FOUND_REVIEW));
        validateWriter(crewMemberReview.getWriter().getUser().getId(), userId);
        crewMemberReviewRepository.deleteById(crewMemberReview.getId());
    }

    //target에게 달린 리뷰 조회
    public List<CrewMemberReviewResDto> getCrewMemberReviewByTargetId(Long crewId, Long targetId){
        CrewMember target = crewMemberService.findCrewMemberById(targetId);

        return crewMemberReviewRepository.findByTarget(target).stream()
                .map(CrewMemberReviewResDto::fromEntity)
                .collect(Collectors.toList());
    }

    //리뷰 달았는지 여부 조회
    public boolean isExistCrewMemberReview(Long crewId, Long userId, Long targetId){
        CrewMember target = crewMemberService.findCrewMemberById(targetId);
        CrewMember writer = crewMemberService.findCrewMemberByCrewAndUser(userId, crewId);

        return !crewMemberReviewRepository.findByWriterAndTarget(writer, target).isEmpty();
    }

    //타겟에게 내가 적은 리뷰 내역 조회
    public List<CrewMemberReviewResDto> getCrewMemberReviewsByTargetIdFromMe(Long crewId, Long writerId, Long targetId){
        CrewMember target = crewMemberService.findCrewMemberById(targetId);
        CrewMember writer = crewMemberService.findCrewMemberByCrewAndUser(writerId, crewId);

        return crewMemberReviewRepository.findByWriterAndTarget(writer, target).stream()
                .map(CrewMemberReviewResDto::fromEntity)
                .collect(Collectors.toList());
    }

    private void validateWriter(Long writerId, Long userId){
        if(writerId != userId){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }
    }

}
