package io.github.invvk.seniorparkour.config.holder;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.BeanProperty;
import io.github.invvk.seniorparkour.config.holder.bean.ParkourBeanMap;


public class ParkourProperties implements SettingsHolder {

    public static final BeanProperty<ParkourBeanMap> PARKOURS =
            new BeanProperty<>(ParkourBeanMap.class, "parkours",
                    new ParkourBeanMap());

}
