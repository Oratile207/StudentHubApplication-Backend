package za.co.studenthub.domain;
import jakarta.persistence.*;
import lombok.*;
import za.co.studenthub.domain.enums.ChannelType;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "channel_details")
public class ChannelDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long channelId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "channel_id")
    private Channel channel;

    private String channelName;

    @ManyToOne
    @JoinColumn(name = "admin_id") // matches primary key column in Admin/User
    private Admin adminCreatedChannel;


    @Enumerated(EnumType.STRING)
    private ChannelType channelType;
}
