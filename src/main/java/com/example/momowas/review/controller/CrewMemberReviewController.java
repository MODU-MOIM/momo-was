package com.example.momowas.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crews/{crewId}/members")
@RequiredArgsConstructor
public class CrewMemberReviewController {
}
