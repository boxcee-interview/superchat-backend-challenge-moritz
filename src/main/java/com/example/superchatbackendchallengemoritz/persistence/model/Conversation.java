package com.example.superchatbackendchallengemoritz.persistence.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@Table(name = "conversations")
public class Conversation extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "recipient_id")
    private Contact recipient;

    @OneToOne
    @JoinColumn(name = "sender_id")
    private Contact sender;

    @OneToMany(mappedBy = "conversation")
    private List<Message> messages;
}
