package ua.dp.example.e001_create;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


public class CreateMonoTest {

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
    public void monoWithElementShouldBeCreatedFromPublisher() {
        //given
        String element1 = "element1";
        String element2 = "element2";
        Flux<String> flux = Flux.just(element1, element2);

        //when
        Mono<String> actual = Mono.from(flux);

        //then
        StepVerifier.create(actual)
                .expectNext(element1)
                .expectComplete()
                .verify();
    }

    @Test
    public void monoWithElementShouldBeCreatedFromConsumer() {
        //given
        String element = "element";

        //when
        Mono<String> actual = Mono.create(sink -> sink.success(element));

        //then
        StepVerifier.create(actual)
                .expectNext(element)
                .expectComplete()
                .verify();
    }
}