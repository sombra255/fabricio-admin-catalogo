package com.fabricio.admin.catalogo.infrastructure;

import com.fabricio.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world11111!");
//        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "development");
//        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);
    }

//    @RabbitListener(queues = "video.encoded.queue")
//    void dummyListener() {
//
//    }

//    @Bean
//    public ApplicationRunner runner(CategoryRepository repository) {
//        return args -> {
//            List<CategoryJpaEntity> all = repository.findAll();
//            Category filmes = Category.newCategory("Filmes", null, true);
//            repository.saveAndFlush(CategoryJpaEntity.from(filmes));
//            repository.deleteAll();
//        };
//    }

//    @Bean
//    @DependsOnDatabaseInitialization
//    public ApplicationRunner runner(@Autowired CreateCategoryUseCase createCategoryUseCase) {
//        return args -> {
//
//        };
//    }

//    @Bean
//    @DependsOnDatabaseInitialization
//    public ApplicationRunner runner(@Autowired CreateGenreUseCase createGenreUseCase) {
//        return args -> {
//
//        };
//    }
}