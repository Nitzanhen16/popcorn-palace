package com.att.tdp.popcorn_palace.contollers;

import com.att.tdp.popcorn_palace.dtos.ShowtimeRequest;
import com.att.tdp.popcorn_palace.dtos.ShowtimeResponse;
import com.att.tdp.popcorn_palace.services.ShowtimeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @Autowired
    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    @GetMapping("/{showtimeId}")
    public ShowtimeResponse getShowtimeById(@PathVariable Long showtimeId) {
        return showtimeService.getShowtimeById(showtimeId);
    }

    @PostMapping
    public ShowtimeResponse addShowtime(@Valid @RequestBody ShowtimeRequest requestDTO) {
        return showtimeService.createShowtime(requestDTO);
    }

    @PostMapping("/update/{showtimeId}")
    public void updateShowtime(@PathVariable Long showtimeId, @Valid @RequestBody ShowtimeRequest requestDTO) {
        showtimeService.updateShowtime(showtimeId, requestDTO);
    }

    @DeleteMapping("/{showtimeId}")
    public void deleteShowtime(@PathVariable Long showtimeId) {
        showtimeService.deleteShowtime(showtimeId);
    }
}
