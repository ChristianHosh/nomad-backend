package com.nomad.socialspring.search;

import com.nomad.socialspring.interest.InterestResponse;
import com.nomad.socialspring.location.LocationResponse;
import com.nomad.socialspring.user.UserResponse;
import org.springframework.data.domain.Page;

public record SearchResult(

        Page<UserResponse> users,
        Page<LocationResponse> locations,
        Page<InterestResponse> interests
) {
}
