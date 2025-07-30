package za.co.studenthub.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "faculty_members") // Specific table for Students' unique attributes
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@DiscriminatorValue("FACULTY")
@SuperBuilder
public class FacultyMembers extends User{
    private String facultyName;
    private String facultyDetails; // Assuming this is distinct from facultyName

    private String studentNumber; // Changed from int to String, student numbers are often not numeric
}
