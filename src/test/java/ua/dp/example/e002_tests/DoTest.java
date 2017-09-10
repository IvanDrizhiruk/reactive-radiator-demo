package ua.dp.example.e002_tests;

import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

/**
 * Created by ivan on 26.08.17.
 */
public class DoTest {

    @Test
    public void fluxWith2ElementsAndExceptionShouldBeverified() {
        //given

//        Flux<User> personsByRating = findPersonsByReuting();
//
//        Flux<Person> threeUkrainianPersons = personsByRating
//                .onErrorResume(error -> findPersonsByReutingInAlternativeSource())
//                .filter(user -> user.country.equals("Ukraine"))
//                .take(3)
//                .map(user -> new Person(user));
//
//
//        Mono<JavaChampion> champion = findChampion();
//
//
//        Mono<Person> javaChampion = champion
//                .map(Person::new);
//
//
//        threeUkrainianPersons.mergeWith(javaChampion)
//                .flatMap(user -> loadBooks(user)
//                        .doOnNext(books -> user.books = books)
//                        .map(books -> user))

    }

    private Mono<List<String>> loadBooks(Person user) {
        return null;
    }

    private Flux<User> findPersonsByReuting() {
        return Flux.empty();
    }

    private Flux<User> findPersonsByReutingInAlternativeSource() {
        return Flux.empty();
    }

    private Mono<JavaChampion> findChampion() {
        return Mono.empty();
    }
}