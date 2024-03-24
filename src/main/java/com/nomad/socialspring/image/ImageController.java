package com.nomad.socialspring.image;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

  private final ImageService imageService;


  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Object> getImageById(@PathVariable Long id) {
    return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.IMAGE_PNG)
            .body(imageService.getImageById(id));
  }

}
