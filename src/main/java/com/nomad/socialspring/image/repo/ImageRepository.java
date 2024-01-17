package com.nomad.socialspring.image.repo;

import com.nomad.socialspring.image.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}