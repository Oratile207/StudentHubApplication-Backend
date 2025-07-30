package za.co.studenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import za.co.studenthub.domain.Channel;
import za.co.studenthub.services.channel_services.ChannelService;

import java.util.List;

@RestController
@RequestMapping("channel")
@CrossOrigin(origins = "http://localhost:3000")
public class ChannelController {
    private final ChannelService channelService;

    @Autowired
    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping("/create")
    public Channel create(@RequestBody Channel channel) {
        return channelService.create(channel);
    }

    @GetMapping("/read/{id}")
    public Channel read(@PathVariable Long id) {
        return channelService.read(id);
    }

    @PutMapping("/update")
    public Channel update(@RequestBody Channel channel) {
        return channelService.update(channel);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        channelService.delete(id);
    }

    @GetMapping("/getAll")
    public List<Channel> getAll() {
        return channelService.getAll();
    }
}
