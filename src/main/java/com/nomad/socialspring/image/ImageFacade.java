package com.nomad.socialspring.image;

import com.nomad.socialspring.error.BxException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageFacade {

  private final ImageRepository repository;

  public Set<Image> saveAll(List<MultipartFile> imageFiles) {
    if (imageFiles == null || imageFiles.isEmpty())
      return new HashSet<>();
    return imageFiles.stream()
            .map(file -> {
              try {
                return save(ImageMapper.requestToEntity(file));
              } catch (IOException e) {
                throw BxException.unexpected(e);
              }
            }).collect(Collectors.toSet());
  }

  public Image save(@NotNull MultipartFile file) {
    try {
      return save(ImageMapper.requestToEntity(file));
    } catch (IOException e) {
      throw BxException.unexpected(e);
    }
  }

  public Image save(Image image) {
    return repository.save(image);
  }

  public Image findById(Long id) {
    return repository.findById(id)
            .orElseThrow(BxException.xNotFound(Image.class, id));
  }
}
