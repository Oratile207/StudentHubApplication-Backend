package za.co.studenthub.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
@Entity
@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
@AllArgsConstructor
@DiscriminatorValue("GUEST")
@Table(name = "guest_profile")
public class GuestProfile extends User {
    // no @NoArgsConstructor needed
}
