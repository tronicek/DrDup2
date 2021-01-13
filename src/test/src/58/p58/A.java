package p58;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class A {

    void list(DirectoryStream<Path> dir, DirectoryStream<Path> files) {
    }

    void listDir(Path dir) throws IOException {
        try (DirectoryStream<Path> files = Files.newDirectoryStream(dir)) {
            try (DirectoryStream<Path> files2 = Files.newDirectoryStream(dir)) {
                list(files, files2);
            }
        }
    }

    void listDir2(Path dir) throws IOException {
        try (DirectoryStream<Path> files = Files.newDirectoryStream(dir)) {
            try (DirectoryStream<Path> files2 = Files.newDirectoryStream(dir)) {
                list(files2, files);
            }
        }
    }
}
