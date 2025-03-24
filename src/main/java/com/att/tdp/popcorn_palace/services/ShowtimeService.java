package com.att.tdp.popcorn_palace.services;

import com.att.tdp.popcorn_palace.dtos.ShowtimeRequest;
import com.att.tdp.popcorn_palace.dtos.ShowtimeResponse;
import com.att.tdp.popcorn_palace.exceptions.InvalidDateException;
import com.att.tdp.popcorn_palace.exceptions.ShowtimeNotFoundException;
import com.att.tdp.popcorn_palace.exceptions.ShowtimeOverlapException;
import com.att.tdp.popcorn_palace.models.Movie;
import com.att.tdp.popcorn_palace.models.Showtime;
import com.att.tdp.popcorn_palace.models.Theater;
import com.att.tdp.popcorn_palace.repositories.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repositories.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
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

    public Showtime getShowtimeEntityById(Long id) {
        // Find and retrieve showtime by id
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new ShowtimeNotFoundException(id));
    }

    public ShowtimeResponse getShowtimeById(Long id) {
        // Find and retrieve showtime by id
        Showtime showtime = getShowtimeEntityById(id);
        return convertShowtimeToShowtimeResponse(showtime);
    }

    public ShowtimeResponse createShowtime(ShowtimeRequest showtimeRequest) {
        // Call Dates validations
        checkShowtimeDates(showtimeRequest.getStartTime(), showtimeRequest.getEndTime());

        // Check if movie.id exists in the DB
        Movie movie = movieService.getMovieEntityById(showtimeRequest.getMovieId());

        // Get Theater by name or created it if it's not found
        String theaterName = showtimeRequest.getTheaterName();
        Theater theater = theaterRepository.findByName(theaterName)
                .orElseGet(() -> theaterRepository.save(new Theater(theaterName)));

        // Check if there are overlapping shows
        List<Showtime> overlapping = showtimeRepository.findOverlappingShowtimes(
                theater.getId(), showtimeRequest.getStartTime(), showtimeRequest.getEndTime());
        if (!overlapping.isEmpty()) {
            throw new ShowtimeOverlapException("Overlapping showtime exists for theater " + theaterName);
        }

        // Save the new showtime in DB and return it
        Showtime showtime = convertShowtimeRequestToShowtime(showtimeRequest, movie, theater);
        showtimeRepository.save(showtime);
        return convertShowtimeToShowtimeResponse(showtime);
    }

    public void updateShowtime(Long id, ShowtimeRequest showtimeRequest) {
        // Check if there is a showtime with the given title in the DB
        Showtime existingShowtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ShowtimeNotFoundException(id));

        LocalDateTime updatedStartTime = showtimeRequest.getStartTime();
        LocalDateTime updatedEndTime = showtimeRequest.getEndTime();

        // Check if the startTime or endTime has changed
        boolean isStartTimeChanged = !existingShowtime.getStartTime().equals(updatedStartTime);
        boolean isEndTimeChanged = !existingShowtime.getEndTime().equals(updatedEndTime);

        // If either startTime or endTime has changed, validate the new values and updated them
        if (isStartTimeChanged || isEndTimeChanged) {
            checkShowtimeDates(updatedStartTime, updatedEndTime);

            // Update showtime dates
            existingShowtime.setStartTime(updatedStartTime);
            existingShowtime.setEndTime(updatedEndTime);
        }

        // Check if movie.id has changed
        if (!existingShowtime.getMovie().getId().equals(showtimeRequest.getMovieId())) {
            // Check if the udpated movie.id exists in the DB
            Movie movie = movieService.getMovieEntityById(showtimeRequest.getMovieId());

            // Update showtime movie
            existingShowtime.setMovie(movie);
        }

        // Check if theater has changed
        if (!existingShowtime.getTheater().getName().equals(showtimeRequest.getTheaterName())) {
            // Get updated Theater by name or created it if it's not found
            String theaterName = showtimeRequest.getTheaterName();
            Theater theater = theaterRepository.findByName(theaterName)
                    .orElseGet(() -> theaterRepository.save(new Theater(theaterName)));

            // Check if there are overlapping shows
            List<Showtime> overlapping = showtimeRepository.findOverlappingShowtimes(
                    theater.getId(), showtimeRequest.getStartTime(), showtimeRequest.getEndTime());
            if (!overlapping.isEmpty()) {
                throw new ShowtimeOverlapException("Overlapping showtime exists for theater " + theaterName);
            }

            // Update showtime theater
            existingShowtime.setTheater(theater);
        }

        // Update Price
        existingShowtime.setPrice(showtimeRequest.getPrice());
        showtimeRepository.save(existingShowtime);
    }

    public void deleteShowtime(Long id) {
        // Delete movie by id
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new ShowtimeNotFoundException(id));
        showtimeRepository.delete(showtime);
    }

    private void checkShowtimeDates(LocalDateTime startTime, LocalDateTime endTime) {
        // Validate that startTime and endTime are not in the past
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new InvalidDateException("Show's start time cannot be in the past.");
        }
        if (endTime.isBefore(LocalDateTime.now())) {
            throw new InvalidDateException("Show's end time cannot be in the past.");
        }

        // Validate that startTime is before endTime
        if (startTime.isAfter(endTime)) {
            throw new InvalidDateException("Show's start time must be before end time.");
        }

        // Calculate the duration between startTime and endTime
        Duration duration = Duration.between(startTime, endTime);

        // Check if the duration is greater than 24 hours
        if (duration.toHours() >= 24) {
            throw new InvalidDateException("Showtime duration cannot exceed 24 hours.");
        }
    }

    private ShowtimeResponse convertShowtimeToShowtimeResponse(Showtime showtime) {
        return new ShowtimeResponse(
                showtime.getId(),
                showtime.getMovie().getId(),
                showtime.getTheater().getName(),
                showtime.getStartTime(),
                showtime.getEndTime(),
                showtime.getPrice()
        );
    }

    private Showtime convertShowtimeRequestToShowtime(ShowtimeRequest showtime, Movie movie, Theater theater) {
        return new Showtime(
                movie,
                theater,
                showtime.getStartTime(),
                showtime.getEndTime(),
                showtime.getPrice()
        );
    }
}
