package com.nomad.socialspring.image.service;

import com.nomad.socialspring.error.exceptions.BxException;
import com.nomad.socialspring.image.model.Image;
import com.nomad.socialspring.image.model.ImageMapper;
import com.nomad.socialspring.image.repo.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageFacade {

    private final ImageRepository repository;

    public Image save(@NotNull MultipartFile imageFile) {
        try {
            return save(ImageMapper.requestToEntity(imageFile));
        } catch (IOException e) {
            throw BxException.unexpected(e);
        }
    }

    public Image save(Image image) {
        return repository.save(image);
    }

    public Image findById(Long id){
        return repository.findById(id)
                .orElseThrow(BxException.xNotFound(Image.class, id));
    }
}
