package za.co.studenthub.services.channel_services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.studenthub.domain.Channel;
import za.co.studenthub.domain.User;
import za.co.studenthub.repository.ChannelRepository;

import java.util.HashSet;
import java.util.List;

@Service
public class ChannelService implements IChannelService {
    private final ChannelRepository repository;

    @Autowired
    public ChannelService(ChannelRepository repository) {
        this.repository = repository;
    }

    @Override
    public Channel create(Channel channel) { return repository.save(channel); }

    @Override
    public Channel read(Long id) { return repository.findById(id).orElse(null); }

    @Override
    public Channel update(Channel channel) { return repository.save(channel); }

    @Override
    public void delete(Long id) { repository.deleteById(id); }

    @Override
    public List<Channel> getAll() { return repository.findAll(); }

    public List<Channel> findByName(String name) { 
        return repository.findByChannelNameContainingIgnoreCase(name);
    }
    
    public Channel addUserToChannel(Long channelId, User user) {
        Channel channel = repository.findById(channelId).orElse(null);
        if (channel != null && user != null) {
            if (channel.getChannelMembers() == null) {
                channel.setChannelMembers(new HashSet<>());
            }
            channel.getChannelMembers().add(user);
            return repository.save(channel);
        }
        return null;
    }
    
    public Channel removeUserFromChannel(Long channelId, User user) {
        Channel channel = repository.findById(channelId).orElse(null);
        if (channel != null && user != null && channel.getChannelMembers() != null) {
            channel.getChannelMembers().remove(user);
            return repository.save(channel);
        }
        return null;
    }
    
    public List<Channel> getChannelsForUser(User user) {
        return repository.findChannelsForUser(user);
    }
    
    public List<Channel> getChannelsByAdmin(User admin) {
        return repository.findByAdminCreatedChannel(admin);
    }
    
    public boolean isUserMemberOfChannel(Long channelId, User user) {
        Channel channel = repository.findById(channelId).orElse(null);
        if (channel != null && channel.getChannelMembers() != null) {
            return channel.getChannelMembers().contains(user) || 
                   channel.getAdminCreatedChannel().equals(user);
        }
        return false;
    }
}