package net.sghill.rescuebuddies.files;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class RecursiveCopyingFileVisitor extends SimpleFileVisitor<Path> {
    private Path currentHead;

    private RecursiveCopyingFileVisitor(Path target) {
        this.currentHead = target;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path directoryToCreate = Paths.get(dir.getFileName().toString()); // avoid filesystem mismatch
        currentHead = currentHead.resolve(directoryToCreate);
        Files.createDirectories(currentHead);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path fileToCreate = Paths.get(file.getFileName().toString());
        Files.copy(file, currentHead.resolve(fileToCreate));
        return FileVisitResult.CONTINUE;
    }

    public static FileVisitor<Path> fromURI(URI uri) {
        return new RecursiveCopyingFileVisitor(Paths.get(uri));
    }
}
