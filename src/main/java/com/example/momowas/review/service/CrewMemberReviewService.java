package com.example.momowas.review.service;

import com.example.momowas.authorization.CrewManager;
import com.example.momowas.crewmember.domain.CrewMember;
import com.example.momowas.crewmember.service.CrewMemberService;
import com.example.momowas.response.BusinessException;
import com.example.momowas.response.ExceptionCode;
import com.example.momowas.review.domain.CrewMemberReview;
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
        //같은 크루 멤버인지 확인
       if(!crewManager.hasCrewPermission(crewId, writerId)|| !crewManager.hasCrewPermission(crewId, targetId)){
           throw new BusinessException(ExceptionCode.NOT_FOUND_CREW_MEMBER);
       }
       //이미 리뷰를 작성함 -> 수정해야함.
        CrewMember target = crewMemberService.findCrewMemberByCrewAndUser(targetId, crewId);
        CrewMember writer = crewMemberService.findCrewMemberByCrewAndUser(writerId, crewId);

        if(crewMemberReviewRepository.findByWriterAndTarget(writer, target).isPresent()){
           throw new BusinessException(ExceptionCode.ALREADY_WRITE_REVIEW);
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
        if(crewMemberReview.getWriter().getUser().getId() != userId){
            throw new BusinessException(ExceptionCode.ACCESS_DENIED);
        }
        crewMemberReview.updateReview(crewMemberReviewReqDto.getComment(), crewMemberReviewReqDto.getRating());
    }

    //target에게 달린 리뷰 조회
    public List<CrewMemberReviewResDto> getCrewMemberReviewByTargetId(Long crewId, Long targetId){
        CrewMember target = crewMemberService.findCrewMemberByCrewAndUser(targetId, crewId);

        return crewMemberReviewRepository.findByTarget(target).stream()
                .map(CrewMemberReviewResDto::fromEntity)
                .collect(Collectors.toList());
    }
}
