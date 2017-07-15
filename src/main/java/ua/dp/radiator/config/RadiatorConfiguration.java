package ua.dp.radiator.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ua.dp.radiator.config.properties.RadiatorProperties;


@EnableConfigurationProperties(RadiatorProperties.class)
public class RadiatorConfiguration {
}
