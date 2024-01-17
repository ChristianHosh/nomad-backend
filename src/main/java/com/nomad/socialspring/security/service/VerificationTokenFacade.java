package com.nomad.socialspring.security.service;

import com.nomad.socialspring.error.exceptions.BxException;
import com.nomad.socialspring.security.model.VerificationTokenMapper;
import com.nomad.socialspring.security.model.VerificationToken;
import com.nomad.socialspring.security.repo.VerificationTokenRepository;
import com.nomad.socialspring.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationTokenFacade {

    private final VerificationTokenRepository repository;

    public VerificationToken findByToken(String token) {
        return repository.findByToken(token)
                .orElseThrow(BxException.xNotFound(VerificationToken.class, token));
    }

    public VerificationToken findById(Long id) {
        return repository.findById(id)
                .orElseThrow(BxException.xNotFound(VerificationToken.class, id));
    }

    public VerificationToken findByUser(User user) {
        return repository.findByUser(user)
                .orElseThrow(BxException.xNotFound(VerificationToken.class, user));
    }

    public VerificationToken save(VerificationToken verificationToken) {
        return repository.save(verificationToken);
    }

    public VerificationToken accountSave(User user) {
        if (user.getIsVerified())
            throw BxException.badRequest(User.class, "already verified");

        return repository.save(VerificationTokenMapper.accountToken(user));
    }

    public void delete(VerificationToken verificationToken) {
        repository.delete(verificationToken);
    }
}
