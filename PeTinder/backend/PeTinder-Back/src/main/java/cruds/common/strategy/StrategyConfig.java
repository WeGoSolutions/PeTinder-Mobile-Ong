package cruds.common.strategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StrategyConfig {

    @Bean
    @Profile("default")
    public ImageStorageStrategy imageStorageStrategy() {
        return new LocalImageStorageStrategy();
    }

    @Bean
    @Profile("test")
    public ImageStorageStrategy memoryStorage() {
        return new InMemoryImageStorageStrategy();
    }

    @Bean
    @Profile("azure")
    public AzureImageStorageStrategy azureBlobStorageStrategy() {
        return new AzureImageStorageStrategy();
    }

}

