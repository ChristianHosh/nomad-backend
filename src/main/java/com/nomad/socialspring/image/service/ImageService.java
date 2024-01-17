package com.nomad.socialspring.image.service;

import com.nomad.socialspring.image.model.ImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageFacade imageFacade;
    public byte[] getImageById(Long id){
        return ImageMapper.entityToResponse(imageFacade.findById(id));
    }
}
