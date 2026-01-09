package com.sonda.controller;

import com.sonda.dto.OperacionDto;
import com.sonda.service.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@Tag(name = "Operaciones", description = "Endpoints para la obtención de operaciones")
public class OperacionController {

    private final ModelService modelService;

    public OperacionController(ModelService modelService) {
        this.modelService = modelService;
    }

    /**
     * Retorna el número de operación asociado al número de registro proporcionado.
     */
    @Operation(summary = "Obtener operación", description = "Retorna el número de operación asociado al número de registro proporcionado.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Operacion encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OperacionDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Operacion no encontrada"
            )
    })
    @GetMapping("/operacion")
    public ResponseEntity<OperacionDto> getOperacion(
            @RequestParam("numeroRegistro") long numeroRegistro) {

        OperacionDto resultado = modelService.buscarOperacion(numeroRegistro);
        return ResponseEntity.ok(resultado);
    }
}
