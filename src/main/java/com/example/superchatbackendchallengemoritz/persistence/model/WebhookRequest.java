package com.example.superchatbackendchallengemoritz.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@ToString(includeFieldNames = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "webhook_requests")
public class WebhookRequest extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "recipient_id")
    private WebhookRequestContact recipient;

    @OneToOne
    @JoinColumn(name = "sender_id")
    private WebhookRequestContact sender;

    @Column private String message;
}
