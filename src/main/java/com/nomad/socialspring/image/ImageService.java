package com.nomad.socialspring.image;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {

  private final ImageFacade imageFacade;

  @Cacheable("images")
  public byte[] getImageById(Long id) {
    return ImageMapper.entityToBytes(imageFacade.findById(id));
  }
}
