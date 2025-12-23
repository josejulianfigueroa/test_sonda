package com.hackerrank.sample.controller;

import com.hackerrank.sample.model.Model;
import com.hackerrank.sample.service.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Items", description = "Endpoints for item comparison data")
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    /**
     * Simple health/home endpoint.
     */
    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Default Java 21 Project Home Page");
    }

    /**
     * Creates a new item.
     */
    @Operation(summary = "Create a new item", description = "Creates a new item that can be used in the comparison feature.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Item created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Model.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error in request body",
                    content = @Content
            )
    })
    @PostMapping(value = "/model", consumes = "application/json")
    public ResponseEntity<Model> createNewModel(@RequestBody @Valid Model model) {
        modelService.createModel(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    /**
     * Deletes all items.
     */
    @Operation(summary = "Delete all items", description = "Deletes all items in the system. Use for testing/cleanup.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "All items deleted successfully"
            )
    })
    @DeleteMapping("/erase")
    public ResponseEntity<String> deleteAllModels() {
        modelService.deleteAllModels();
        return ResponseEntity.ok("All items deleted");  // ✅ 200 OK
    }

    /**
     * Deletes an item by id.
     */
    @Operation(summary = "Delete item by id", description = "Deletes a single item identified by its id.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Item deleted"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item not found"
            )
    })
    @DeleteMapping("/model/{id}")
    public ResponseEntity<Void> deleteModelById(@PathVariable Long id) {
        modelService.deleteModelById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all items for comparison.
     */
    @Operation(summary = "Get all items", description = "Returns the full list of items available for comparison.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Items retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Model.class))
                    )
            )
    })
    @GetMapping("/model")
    public ResponseEntity<List<Model>> getAllModels() {
        List<Model> models = modelService.getAllModels();
        return ResponseEntity.ok(models);
    }

    /**
     * Retrieves a single item by id.
     */
    @Operation(summary = "Get item by id", description = "Returns a single item identified by its id.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Item found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Model.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item not found"
            )
    })
    @GetMapping("/model/{id}")
    public ResponseEntity<Model> getModelById(@PathVariable Long id) {
        Model model = modelService.getModelById(id);
        return ResponseEntity.ok(model);
    }
}
