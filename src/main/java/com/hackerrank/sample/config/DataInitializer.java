package com.hackerrank.sample.config;

import com.hackerrank.sample.model.Model;
import com.hackerrank.sample.repository.ModelRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initModels(ModelRepository modelRepository) {
        return args -> {
            /*
            if (modelRepository.count() > 0) {
                return;
            }

            List<Model> seedModels = List.of(
                    Model.builder()
                            .id(1L)
                            .name("Smartphone X1")
                            .imageUrl("https://example.com/x1.png")
                            .description("Smartphone gama media")
                            .price(new BigDecimal("299.99"))
                            .rating(4.2)
                            .specifications("4GB RAM, 128GB, 6.1\"")
                            .build(),
                    Model.builder()
                            .id(2L)
                            .name("Smartphone X2")
                            .imageUrl("https://example.com/x2.png")
                            .description("Smartphone gama alta")
                            .price(new BigDecimal("699.99"))
                            .rating(4.8)
                            .specifications("8GB RAM, 256GB, 6.5\"")
                            .build(),
                    Model.builder()
                            .id(3L)
                            .name("Laptop Pro 14")
                            .imageUrl("https://example.com/laptop14.png")
                            .description("Laptop ultrabook 14\"")
                            .price(new BigDecimal("1299.00"))
                            .rating(4.7)
                            .specifications("16GB RAM, 512GB SSD, i7")
                            .build(),
                    Model.builder()
                            .id(4L)
                            .name("Laptop Pro 16")
                            .imageUrl("https://example.com/laptop16.png")
                            .description("Laptop 16\" para creadores")
                            .price(new BigDecimal("1899.00"))
                            .rating(4.9)
                            .specifications("32GB RAM, 1TB SSD, GPU dedicada")
                            .build(),
                    Model.builder()
                            .id(5L)
                            .name("Auriculares BT")
                            .imageUrl("https://example.com/headphones.png")
                            .description("Auriculares bluetooth con ANC")
                            .price(new BigDecimal("149.90"))
                            .rating(4.4)
                            .specifications("ANC, 30h batería, USB-C")
                            .build(),
                    Model.builder()
                            .id(6L)
                            .name("Smartwatch Sport")
                            .imageUrl("https://example.com/watch.png")
                            .description("Reloj inteligente deportivo")
                            .price(new BigDecimal("199.90"))
                            .rating(4.1)
                            .specifications("GPS, HR, resistencia al agua 5ATM")
                            .build(),
                    Model.builder()
                            .id(7L)
                            .name("Monitor 27\" 4K")
                            .imageUrl("https://example.com/monitor27.png")
                            .description("Monitor 4K 27 pulgadas")
                            .price(new BigDecimal("399.00"))
                            .rating(4.3)
                            .specifications("IPS, 60Hz, HDR10")
                            .build(),
                    Model.builder()
                            .id(8L)
                            .name("Teclado Mecánico")
                            .imageUrl("https://example.com/keyboard.png")
                            .description("Teclado mecánico RGB")
                            .price(new BigDecimal("89.90"))
                            .rating(4.5)
                            .specifications("Switches red, layout ANSI")
                            .build(),
                    Model.builder()
                            .id(9L)
                            .name("Mouse Inalámbrico")
                            .imageUrl("https://example.com/mouse.png")
                            .description("Mouse inalámbrico ergonómico")
                            .price(new BigDecimal("49.90"))
                            .rating(4.0)
                            .specifications("2.4G + BT, DPI ajustable")
                            .build(),
                    Model.builder()
                            .id(10L)
                            .name("Parlante Bluetooth")
                            .imageUrl("https://example.com/speaker.png")
                            .description("Parlante portátil resistente al agua")
                            .price(new BigDecimal("79.90"))
                            .rating(4.6)
                            .specifications("IPX7, 12h batería, USB-C")
                            .build()
            );

            modelRepository.saveAll(seedModels);
            */
        };
    }
}
