package io.github.invvk.seniorparkour.config.holder.bean;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ParkourBeanMap {

    @Getter private final Map<String, ParkourCnfData> parkours = new HashMap<>();

}
