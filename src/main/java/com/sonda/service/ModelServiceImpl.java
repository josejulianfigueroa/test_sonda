package com.sonda.service;

import com.sonda.dto.OperacionDto;
import org.springframework.stereotype.Service;
import com.sonda.utils.Util;
import java.io.FileInputStream;
import java.io.IOException;


@Service("modelService")
public class ModelServiceImpl implements ModelService {

    private static final String ARCHIVO_BIN = "src/main/resources/OperacionesBinarias.bin";
    private static final int REGISTRO_SIZE = 9; // 7 + 2 bytes
    private static final long TOTAL_REGISTROS = 1000000;

    @Override
    public OperacionDto buscarOperacion(long numeroRegistro) {
        try (FileInputStream fis = new FileInputStream(ARCHIVO_BIN)) {
            byte[] buffer = new byte[(int) (REGISTRO_SIZE * TOTAL_REGISTROS)];
            int bytesLeidos = fis.read(buffer);

            if (bytesLeidos != buffer.length) {
                throw new RuntimeException("Archivo incompleto");
            }

            for (int i = 0; i < buffer.length; i += REGISTRO_SIZE) {
                byte[] regBytes = new byte[7];
                System.arraycopy(buffer, i, regBytes, 0, 7);

                long regNum = Util.valor7bytesToInt(regBytes);
                if (regNum == numeroRegistro) {
                    byte[] opBytes = new byte[2];
                    System.arraycopy(buffer, i + 7, opBytes, 0, 2);
                    int operacion = Util.valor2bytesToInt(opBytes);
                    return new OperacionDto(numeroRegistro, operacion);
                }
            }
            // No encontrado
            return new OperacionDto(numeroRegistro, 0);

        } catch (IOException e) {
            throw new RuntimeException("Error leyendo archivo binario", e);
        }
    }


}
