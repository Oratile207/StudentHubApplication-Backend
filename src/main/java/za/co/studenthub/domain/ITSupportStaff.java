package za.co.studenthub.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@DiscriminatorValue("IT_SUPPORT")
@Table(name = "it_support_staff") // Specific table for Students' unique attributes
@SuperBuilder
public class ITSupportStaff extends User{
    private String facultyName;
    private String facultyDetails; // Assuming this is distinct from facultyName

    private String studentNumber; // Changed from int to String, student numbers are often not numeric
}
