package com.nomad.socialspring.image.model;

import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.post.model.Post;
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

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "POST_ID")
    private Post post;

}