package com.sonda.utils;

import com.sonda.exception.TablasReferenciasException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Util {

    private static final int MAXIMO_BYTE_TARJETA = 7;
    private static final int MASK_FF = 0xFF;

    public static byte[] invertirCadena(byte[] input) {
        byte[] resultado = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            resultado[i] = input[input.length - 1 - i];
        }
        return resultado;
    }

    public static long valor7bytesToInt(byte[] leidoIn) {
        byte[] leido = invertirCadena(leidoIn);
        if (leido.length != MAXIMO_BYTE_TARJETA) {
            throw new TablasReferenciasException("Arreglo debe ser de 7 bytes.");
        }
        byte[] bytes = new byte[8];
        for (int i = 0; i < MAXIMO_BYTE_TARJETA; i++) {
            bytes[1 + i] = leido[i];
        }
        ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    public static int valor2bytesToInt(byte[] leidoIn) {
        byte[] leido = invertirCadena(leidoIn);
        if (leido.length != 2) {
            throw new TablasReferenciasException("Arreglo debe ser de 2 bytes.");
        }
        byte[] bytes = new byte[4];
        bytes[2] = leido[0];
        bytes[3] = leido[1];
        ByteBuffer buffer = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    public static String byteArrayToHexStringSinEspacios(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & MASK_FF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
