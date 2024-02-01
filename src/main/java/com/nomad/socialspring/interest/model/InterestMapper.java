package com.nomad.socialspring.interest.model;

import com.nomad.socialspring.interest.dto.InterestResponse;

public class InterestMapper {

    public static InterestResponse entityToResponse(Interest interest) {
        if (interest == null)
            return null;

        return new InterestResponse(interest);
    }
}
