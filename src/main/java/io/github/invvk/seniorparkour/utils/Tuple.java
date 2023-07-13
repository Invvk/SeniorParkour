package io.github.invvk.seniorparkour.utils;

import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
public class Tuple<V1, V2> {

    public static <A,B> Tuple<A,B> of(A a, B b) {
        return new Tuple<>(a, b);
    }

    private V1 value1;
    private V2 value2;

    public V1 v1() {
        return value1;
    }

    public V2 v2() {
        return value2;
    }
}
