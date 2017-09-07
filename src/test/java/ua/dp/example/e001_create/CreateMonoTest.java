package ua.dp.example.e001_create;

import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


public class CreateMonoTest {

    @Test
    public void monoWithElementShouldBeCreated() {
        //given
        String element = "Element";

        //when
        Mono<String> actual = Mono.just(element);

        //then
        StepVerifier.create(actual)
                .expectNext(element)
                .expectComplete()
                .verify();
    }

    @Test
    public void emptyMonoShouldBeCreated() {
        //given

        //when
        Mono<String> actual = Mono.empty();

        //then
        StepVerifier.create(actual)
                .expectComplete()
                .verify();
    }

    @Test
    public void monoWithExceptionShouldBeCreated() {
        //given
        RuntimeException exception = new RuntimeException("Some unexpected error");

        //when
        Mono<String> actual = Mono.error(exception);

        //then
        StepVerifier.create(actual)
                .expectError()
                .verify();
    }
}