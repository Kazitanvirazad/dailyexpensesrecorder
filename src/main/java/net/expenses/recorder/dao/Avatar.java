package net.expenses.recorder.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Kazi Tanvir Azad
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "avatar", schema = "EXPENSE_RECORDER")
public class Avatar {
    @Id
    @Column(name = "avatarid", columnDefinition = "SERIAL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer avatarId;

    @Column(name = "avatarencoded", columnDefinition = "TEXT")
    private String avatarEncodedImage;

    @Column(name = "isdefaultavatar", columnDefinition = "BOOL")
    private Boolean isDefaultAvatar;
}
