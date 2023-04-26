package com.devsuperior.movieflix.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.MovieByGenreDTO;
import com.devsuperior.movieflix.dto.MovieDTO;
import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Transactional(readOnly = true)
	public MovieDTO findById(Long id) {
		Optional<Movie> obj = movieRepository.findById(id);
		Movie movie = obj.orElseThrow(() -> new ResourceNotFoundException("Id not found: " + id));
		return new MovieDTO(movie);
	}
	
	@Transactional(readOnly = true)
	public Page<MovieByGenreDTO> findByGenre(Long genreId, Pageable pageable) {
		Genre genre = (genreId == 0) ? null : genreRepository.getOne(genreId);
		Page<Movie> page = movieRepository.find(genre, pageable);
		movieRepository.findMoviesWithGenres(page.getContent());
		return page.map(x -> new MovieByGenreDTO(x));
	}
	
	@Transactional(readOnly = true)
	public List<ReviewDTO> findMovieReviews(Long id){
		List<Review> obj = reviewRepository.findReviewByMovieId(id);
		return obj.stream().map(x -> new ReviewDTO(x)).toList();
	}
}
