package com.nomad.socialspring.image.model;

import com.nomad.socialspring.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_IMAGE")
public class Image extends BaseEntity {

    @Lob
    @Column(name = "DATA", length = 1000)
    private byte[] imageData;

    @Override
    public String getExceptionString() {
        return getId().toString();
    }
}