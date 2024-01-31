package com.nomad.socialspring.interest.controller;

import com.nomad.socialspring.common.annotations.ResponseOk;
import com.nomad.socialspring.interest.dto.InterestResponse;
import com.nomad.socialspring.interest.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    @GetMapping("")
    @ResponseOk
    public Page<InterestResponse> getInterests(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "50") int size,
            @RequestParam(name = "name", required = false) String name
    ) {
        return interestService.getInterests(page, size, name);
    }
}
