package com.nomad.socialspring.security.model;

import com.nomad.socialspring.common.BaseEntity;
import com.nomad.socialspring.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_VERIFICATION_TOKEN")
public class VerificationToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TOKEN", nullable = false)
    private String token;

    @Column(name = "EXPIRATION_DATE", nullable = false)
    private Date expirationDate;

    @Enumerated
    @Column(name = "VERIFICATION_TYPE", nullable = false)
    private VerificationType verificationType;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "USER_ID")
    private User user;


}