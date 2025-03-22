package com.att.tdp.popcorn_palace.services;

import com.att.tdp.popcorn_palace.dtos.ShowtimeRequest;
import com.att.tdp.popcorn_palace.dtos.ShowtimeResponse;
import com.att.tdp.popcorn_palace.exceptions.ShowtimeNotFoundException;
import com.att.tdp.popcorn_palace.exceptions.ShowtimeOverlapException;
import com.att.tdp.popcorn_palace.models.Movie;
import com.att.tdp.popcorn_palace.models.Showtime;
import com.att.tdp.popcorn_palace.models.Theater;
import com.att.tdp.popcorn_palace.repositories.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repositories.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowtimeService {
    private final MovieService movieService;
    private final ShowtimeRepository showtimeRepository;
    private final TheaterRepository theaterRepository;

    @Autowired
    public ShowtimeService(MovieService movieService, ShowtimeRepository showtimeRepository, TheaterRepository theaterRepository) {
        this.movieService = movieService;
        this.showtimeRepository = showtimeRepository;
        this.theaterRepository = theaterRepository;
    }

    public ShowtimeResponse getShowtimeById(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ShowtimeNotFoundException(id));
        return convertShowtimeToShowtimeResponse(showtime);
    }

    public ShowtimeResponse createShowtime(ShowtimeRequest requestDTO) {
        Movie movie = movieService.getMovieById(requestDTO.getMovieId());

        String theaterName = requestDTO.getTheater();
        Theater theater = theaterRepository.findByName(theaterName)
                .orElseGet(() -> theaterRepository.save(new Theater(theaterName)));

        List<Showtime> overlapping = showtimeRepository.findOverlappingShowtimes(
                theater.getId(), requestDTO.getStartTime(), requestDTO.getEndTime());
        if (!overlapping.isEmpty()) {
            throw new ShowtimeOverlapException("Overlapping showtime exists for theater " + theaterName);
        }
        Showtime showtime = convertShowtimeRequestToShowtime(requestDTO, movie, theater);
        showtimeRepository.save(showtime);

        return convertShowtimeToShowtimeResponse(showtime);
    }

    public void updateShowtime(Long id, ShowtimeRequest requestDTO) {
        // Check if there is a showtime with the given title in the DB
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ShowtimeNotFoundException(id));
        // TODO update showtime
        showtimeRepository.save(showtime);
    }

    public void deleteShowtime(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ShowtimeNotFoundException(id));
        showtimeRepository.delete(showtime);
    }

    private ShowtimeResponse convertShowtimeToShowtimeResponse(Showtime showtime){
        return new ShowtimeResponse(
                showtime.getId(),
                showtime.getMovie().getId(),
                showtime.getTheater().getName(),
                showtime.getStartTime(),
                showtime.getEndTime(),
                showtime.getPrice()
        );
    }

    private Showtime convertShowtimeRequestToShowtime(ShowtimeRequest showtime, Movie movie, Theater theater){
        return new Showtime(
                movie,
                theater,
                showtime.getStartTime(),
                showtime.getEndTime(),
                showtime.getPrice()
        );
    }
}
