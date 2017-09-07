package ua.dp.example.e001_create;

import org.junit.Test;
import org.junit.runner.Runner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ivan on 26.08.17.
 */
public class CreateFluxTest {

    @Test
    public void fluxWithElementsShouldBeCreated() {
        //given
        String element1 = "Element #1";
        String element2 = "Element #2";
        String element3 = "Element #3";

        //when
        Flux<String> actual = Flux.just(element1, element2, element3);


        actual.map


        //then
        StepVerifier.create(actual)
                .expectNext(element1)
                .expectNext(element2)
                .expectNext(element3)
                .expectComplete()
                .verify();
    }


    @Test
    public void fluxWithIterableElementsShouldBeCreated() {
        //given
        String element1 = "Element #1";
        String element2 = "Element #2";
        String element3 = "Element #3";
        List<String> elements = Arrays.asList(element1, element2, element3);

        //when
        Flux<String> actual = Flux.fromIterable(elements);

        //then
        StepVerifier.create(actual)
                .expectNext(element1)
                .expectNext(element2)
                .expectNext(element3)
                .expectComplete()
                .verify();
    }

    @Test
    public void fluxRangeFromFiveToSevenShouldBeCreated() {
        //given
        String element1 = "Element #1";
        String element2 = "Element #2";
        String element3 = "Element #3";
        List<String> elements = Arrays.asList(element1, element2, element3);

        //when
        Flux<Integer> actual = Flux.range(5, 3);

        //then
        StepVerifier.create(actual)
                .expectNext(5)
                .expectNext(6)
                .expectNext(7)
                .expectComplete()
                .verify();
    }

    @Test
    public void fluxShoulBeGenerated() {
        //given

        //when
        Flux<String> actual = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3*state);
                    if (state == 10) {
                        sink.complete();
                    }

                    return state + 1;
                });

        //then
        StepVerifier.create(actual)
                .expectNext("3 x 0 = 0")
                .expectNext("3 x 1 = 3")
                .expectNext("3 x 2 = 6")
                .expectNext("3 x 3 = 9")
                .expectNext("3 x 4 = 12")
                .expectNext("3 x 5 = 15")
                .expectNext("3 x 6 = 18")
                .expectNext("3 x 7 = 21")
                .expectNext("3 x 8 = 24")
                .expectNext("3 x 9 = 27")
                .expectNext("3 x 10 = 30")
                .expectComplete()
                .verify();
    }

    @Test
    public void fluxShoulBeCreated() {
        //given
        //when
        Flux<String> actual = Flux.create(sink -> {
            EventProcessor.register(
                    new EventListener<String>() {

                        public void onDataChunk(List<String> chunk) {
                            for(String s : chunk) {
                                sink.next(s);
                            }
                        }

                        public void processComplete() {
                            sink.complete();
                        }
                    });
        });

        EventProcessor.run();

        //then
        StepVerifier.create(actual)
                .expectNext("Q1")
                .expectNext("Q2")
                .expectNext("D1")
                .expectComplete()
                .verify();
    }

    static class EventProcessor {
        private static EventListener<String> eventListener;

        static void register(EventListener<String> eventListener) {

            EventProcessor.eventListener = eventListener;
        }

        static void run () {
            System.out.println("1" + Thread.currentThread());

            new Thread(
            new Runnable() {

                @Override
                public void run() {
                    System.out.println("2" + Thread.currentThread());

                    try {
                        Thread.sleep(100L);
                        eventListener.onDataChunk(Arrays.asList("Q1", "Q2"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(100L);
                        eventListener.onDataChunk(Arrays.asList("D1"));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(100L);
                        eventListener.processComplete();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    interface EventListener<T> {
        void onDataChunk(List<T> chunk);
        void processComplete();
    }

    @Test
    public void emptyFluxShouldBeCreated() {
        //given

        //when
        Flux<String> actual = Flux.empty();

        //then
        StepVerifier.create(actual)
                .expectComplete()
                .verify();
    }

    @Test
    public void fluxWithExceptionShouldBeCreated() {
        //given
        RuntimeException exception = new RuntimeException("Some unexpected error");

        //when
        Flux<String> actual = Flux.error(exception);

        //then
        StepVerifier.create(actual)
                .expectError()
                .verify();

    }
}