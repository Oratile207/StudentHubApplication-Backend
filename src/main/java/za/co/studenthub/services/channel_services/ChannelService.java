package za.co.studenthub.services.channel_services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.studenthub.domain.Channel;
import za.co.studenthub.repository.ChannelRepository;

import java.util.List;

@Service
public class ChannelService implements IChannelService {

    private final ChannelRepository repository;

    @Autowired
    public ChannelService(ChannelRepository repository) {
        this.repository = repository;
    }

    @Override
    public Channel create(Channel channel) {
        return repository.save(channel);
    }

    @Override
    public Channel read(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Channel update(Channel channel) {
        return repository.save(channel);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Channel> getAll() {
        return repository.findAll();
    }
}
