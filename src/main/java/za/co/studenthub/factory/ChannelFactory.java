package za.co.studenthub.factory;

import za.co.studenthub.domain.Admin;
import za.co.studenthub.domain.Channel;
import za.co.studenthub.domain.enums.ChannelType;
import za.co.studenthub.util.Helper;

public class ChannelFactory {
    public static Channel createChannel(String channelName, ChannelType channelType, Admin admin) {
        if (Helper.isNullOrEmpty(channelName) || channelType == null || admin == null) {
            return null;
        }

        return Channel.builder()
                .channelId(Helper.generateId())
                .channelName(channelName)
                .channelType(channelType)
                .adminCreatedChannel(admin)
                .build();
    }
}