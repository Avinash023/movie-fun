package org.superbiz.moviefun.blobstore;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;

public class FileStore implements BlobStore {

    public void put(Blob blob) throws IOException {
        saveUploadToFile( IOUtils.toByteArray( blob.inputStream ), getCoverFile( Long.parseLong(blob.name)) );
    }

    public Optional<Blob> get( String name) throws IOException {

        try {
            Path coverFilePath = getExistingCoverPath( Long.parseLong( name ) );
            byte[] imageBytes = readAllBytes( coverFilePath );
            return Optional.of( new Blob( name, new ByteArrayInputStream( imageBytes ), new Tika().detect( imageBytes ) ));
        }
        catch (Exception e) {
            throw new IOException( e );
        }
    }

    public void deleteAll() {
        // ...
    }

    private void saveUploadToFile( byte[] uploadedFile, File targetFile) throws IOException {
        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            outputStream.write(uploadedFile);
        }
    }

    private File getCoverFile(long albumId) {
        String coverFileName = format("covers/%d", albumId);
        return new File(coverFileName);
    }

    private Path getExistingCoverPath(long albumId) throws URISyntaxException, ClassNotFoundException {
        File coverFile = getCoverFile(albumId);
        Path coverFilePath;

        if (coverFile.exists()) {
            coverFilePath = coverFile.toPath();
        } else {

            Class cls = Class.forName("org.superbiz.moviefun.albums.AlbumsController");
            ClassLoader cLoader = cls.getClassLoader();
            System.out.println(cLoader.getClass());
            URL url = cLoader.getSystemResource("default-cover.jpg");
            System.out.println("Value = " + url);
            coverFilePath = Paths.get(url.toURI());
        }
        return coverFilePath;
    }
}
