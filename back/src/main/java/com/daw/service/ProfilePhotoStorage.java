package com.daw.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.UUID;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.daw.exceptions.InvalidImageException;

/**
 * Guarda las fotos de perfil como archivos en disco, en lugar de dentro de la base de datos.
 *
 * Cada foto se valida (tiene que ser una imagen real y no excesivamente grande), se recorta a
 * cuadrado, se reduce a {@value #SIZE}x{@value #SIZE} px y se vuelve a codificar como JPEG. Al
 * recodificar se eliminan los metadatos (EXIF, ubicación GPS...). El nombre del archivo es único
 * por subida, de modo que el navegador puede cachearlo para siempre.
 *
 * En la base de datos solo se guarda la ruta relativa, por ejemplo
 * {@code /uploads/avatars/12-3f2a....jpg}.
 */
@Component
public class ProfilePhotoStorage {

    /** Prefijo público bajo el que se sirven las fotos. */
    public static final String URL_PREFIX = "/uploads/avatars/";

    /** Lado, en píxeles, de la foto final (cuadrada). */
    public static final int SIZE = 256;

    /** Tamaño máximo del archivo subido. */
    private static final long MAX_BYTES = 5L * 1024 * 1024;

    /** Dimensión máxima aceptada, para evitar imágenes diseñadas para agotar la memoria. */
    private static final int MAX_DIMENSION = 6000;

    private static final float JPEG_QUALITY = 0.85f;

    private final Path root;
    private final Path avatars;

    public ProfilePhotoStorage(@Value("${app.upload-dir}") String uploadDir) {
        this.root = Path.of(uploadDir).toAbsolutePath().normalize();
        this.avatars = root.resolve("avatars");
        try {
            Files.createDirectories(avatars);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo crear el directorio de subidas: " + avatars, e);
        }
    }

    /** Directorio raíz de subidas, para servirlo como recurso estático. */
    public Path getRoot() {
        return root;
    }

    /**
     * Valida, reduce y guarda una foto.
     *
     * @param userId identificador del usuario dueño de la foto
     * @param bytes  contenido del archivo subido
     * @return ruta relativa pública de la foto guardada
     * @throws InvalidImageException si el archivo no es una imagen válida o es demasiado grande
     */
    public String store(Long userId, byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new InvalidImageException("No se ha enviado ninguna imagen");
        }
        if (bytes.length > MAX_BYTES) {
            throw new InvalidImageException("La imagen no debe superar los 5MB");
        }
        BufferedImage square = toSquare(read(bytes));
        String filename = userId + "-" + UUID.randomUUID() + ".jpg";
        try {
            Files.write(avatars.resolve(filename), encodeJpeg(square));
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar la foto de perfil", e);
        }
        return URL_PREFIX + filename;
    }

    /**
     * Borra una foto guardada. Ignora valores nulos y URLs externas (por ejemplo la foto de Google).
     *
     * @param url valor actual de {@code profileImageUrl}
     */
    public void delete(String url) {
        if (url == null || !url.startsWith(URL_PREFIX)) return;
        Path file = avatars.resolve(url.substring(URL_PREFIX.length())).normalize();
        if (!file.startsWith(avatars)) return;
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            // Un archivo huérfano no debe impedir actualizar el perfil
        }
    }

    /** Lee la imagen comprobando sus dimensiones antes de decodificarla entera. */
    private BufferedImage read(byte[] bytes) {
        try (ImageInputStream in = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes))) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (!readers.hasNext()) {
                throw new InvalidImageException("El archivo no es una imagen válida (usa JPG, PNG o GIF)");
            }
            ImageReader reader = readers.next();
            try {
                reader.setInput(in);
                if (reader.getWidth(0) > MAX_DIMENSION || reader.getHeight(0) > MAX_DIMENSION) {
                    throw new InvalidImageException("La imagen es demasiado grande");
                }
                return reader.read(0);
            } finally {
                reader.dispose();
            }
        } catch (IOException | RuntimeException e) {
            if (e instanceof InvalidImageException invalid) throw invalid;
            throw new InvalidImageException("El archivo no es una imagen válida (usa JPG, PNG o GIF)");
        }
    }

    /** Recorta al cuadrado central y reduce a SIZE x SIZE, con fondo blanco bajo la transparencia. */
    private BufferedImage toSquare(BufferedImage src) {
        int side = Math.min(src.getWidth(), src.getHeight());
        BufferedImage cropped = src.getSubimage((src.getWidth() - side) / 2, (src.getHeight() - side) / 2, side, side);
        Image scaled = cropped.getScaledInstance(SIZE, SIZE, Image.SCALE_AREA_AVERAGING);
        BufferedImage out = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = out.createGraphics();
        try {
            g.setColor(java.awt.Color.WHITE);
            g.fillRect(0, 0, SIZE, SIZE);
            g.drawImage(scaled, 0, 0, null);
        } finally {
            g.dispose();
        }
        return out;
    }

    private byte[] encodeJpeg(BufferedImage image) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             MemoryCacheImageOutputStream ios = new MemoryCacheImageOutputStream(out)) {
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(JPEG_QUALITY);
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), param);
            ios.flush();
            return out.toByteArray();
        } finally {
            writer.dispose();
        }
    }
}
