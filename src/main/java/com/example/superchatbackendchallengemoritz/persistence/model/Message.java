package com.example.superchatbackendchallengemoritz.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "messages")
public class Message extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Contact recipient;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Contact sender;

    @Column private String content;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
}
