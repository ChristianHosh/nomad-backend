package com.nomad.socialspring.image;

import com.nomad.socialspring.error.BxException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Slf4j
public class ImageMapper {

  private ImageMapper() {
  }

  public static final String HOSTNAME = "https://nomad-backend-production-62f9.up.railway.app";

  @NotNull
  private static byte[] compressImage(byte[] data) {

    Deflater deflater = new Deflater();
    deflater.setLevel(Deflater.BEST_COMPRESSION);
    deflater.setInput(data);
    deflater.finish();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] tmp = new byte[4 * 1024];
    while (!deflater.finished()) {
      int size = deflater.deflate(tmp);
      outputStream.write(tmp, 0, size);
    }
    try {
      outputStream.close();
    } catch (Exception e) {
      throw BxException.unexpected(e);
    }
    return outputStream.toByteArray();
  }

  @NotNull
  private static byte[] decompressImage(byte[] data) {
    Inflater inflater = new Inflater();
    inflater.setInput(data);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] tmp = new byte[4 * 1024];
    try {
      while (!inflater.finished()) {
        int count = inflater.inflate(tmp);
        outputStream.write(tmp, 0, count);
      }
      outputStream.close();
    } catch (Exception e) {
      throw BxException.unexpected(e);
    }
    return outputStream.toByteArray();
  }

  public static String entityToUrl(Image image) {
    if (image == null)
      return null;

    return HOSTNAME + "/api/images/" + image.getId();
  }

  public static ImageResponse entityToResponse(Image image) {
    return ImageResponse.builder()
            .id(image.getId())
            .src(entityToUrl(image))
            .build();
  }

  @NotNull
  public static byte[] entityToBytes(@NotNull Image image) {
    return decompressImage(image.getImageData());
  }

  public static Image requestToEntity(@NotNull MultipartFile imageFile) throws IOException {
    return Image.builder()
            .imageData(compressImage(imageFile.getBytes()))
            .build();
  }
}
