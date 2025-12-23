package com.hackerrank.sample.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain model representing an item that can be used
 * in the product comparison feature (Item Comparison exercise).
 */

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model implements Serializable {

    @Id
    private Long id;

    @NotBlank(message = "name is required")
    @Size(max = 100, message = "name must be at most 100 characters")
    private String name;

    /*
    @NotBlank(message = "imageUrl is required")
    @Size(max = 255, message = "imageUrl must be at most 255 characters")
    private String imageUrl;

    @NotBlank(message = "description is required")
    @Size(max = 500, message = "description must be at most 500 characters")
    private String description;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "rating is required")
    @DecimalMin(value = "0.0", message = "rating must be at least 0")
    @DecimalMax(value = "5.0", message = "rating must be at most 5")
    private Double rating;

    @NotBlank(message = "specifications is required")
    @Size(max = 1000, message = "specifications must be at most 1000 characters")
    private String specifications;*/
}
