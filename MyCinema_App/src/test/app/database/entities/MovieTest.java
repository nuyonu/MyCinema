package app.database.entities;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MovieTest {

    private Movie emptyMovie, completeMovie;

    @Before
    public void setUp() {
        completeMovie = new Movie("MovieTest", 3424, 24.5, "/images/test.jpg");
        emptyMovie = new Movie();
    }

    @Test
    public void should1GetMovieTitle(){
        assertThat(completeMovie.getTitle().equals("MovieTest")).isTrue();
    }

    @Test
    public void should2GetMovieSeconds(){
        assertThat(completeMovie.getSeconds() == 3424).isTrue();
    }

    @Test
    public void should3GetMoviePrice(){
        assertThat(completeMovie.getPrice() == 24.5).isTrue();
    }

    @Test
    public void should4GetMoviePath(){
        assertThat(completeMovie.getPath().equals("/images/test.jpg")).isTrue();
    }

    @Test
    public void should5MovieCreatedDateNotEmpty() {
        assertThat(completeMovie.getCreatedDate().isEmpty()).isFalse();
    }

    @Test
    public void should6SetMovieTitle() {
        emptyMovie.setTitle("MovieTest");
        assertThat(emptyMovie.getTitle().equals("MovieTest")).isTrue();
    }

    @Test
    public void should7SetMovieSeconds() {
        int seconds = new Random().nextInt((10000 - 100) + 1) + 100;
        emptyMovie.setSeconds(seconds);
        assertThat(seconds == emptyMovie.getSeconds()).isTrue();
    }

    @Test
    public void should8SetMoviePrice() {
        double price = 5 + (55 - 5) * new Random().nextDouble();
        emptyMovie.setPrice(price);
        assertThat(price == emptyMovie.getPrice()).isTrue();
    }

    @Test
    public void should9SetMoviePath() {
        emptyMovie.setPath("/image/test.jpg");
        assertThat(emptyMovie.getPath().equals("/image/test.jpg")).isTrue();
    }
}