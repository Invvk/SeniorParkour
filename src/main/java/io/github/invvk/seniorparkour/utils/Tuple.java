package io.github.invvk.seniorparkour.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Tuple<V1, V2> {

    private V1 value1;
    private V2 value2;

}
