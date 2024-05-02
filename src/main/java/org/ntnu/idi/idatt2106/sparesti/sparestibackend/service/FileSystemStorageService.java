package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.StorageException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.StorageFileNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.ChallengeRepository;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.GoalRepository;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.util.StorageProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService {

    private final UserService userService;
    private final ChallengeRepository challengeRepository;
    private final GoalRepository goalRepository;
    private final StorageProperties properties;
    private final Path rootLocation;

    public FileSystemStorageService(
            UserService userService,
            ChallengeRepository challengeRepository,
            GoalRepository goalRepository,
            StorageProperties properties) {
        this.userService = userService;
        this.challengeRepository = challengeRepository;
        this.goalRepository = goalRepository;
        this.properties = properties;
        this.rootLocation = Paths.get(properties.getLocation().trim());
    }

    public void save(MultipartFile file, String identifier, UserDetails userDetails)
            throws IOException {
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }

        User user = userService.findUserByUsername(userDetails.getUsername());
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        if (!isValidExtension(fileExtension)) {
            throw new StorageException("Unsupported file type.");
        }
        String newFilename = identifier + fileExtension;

        if (!isValidFilename(identifier)) {
            throw new StorageException("Incorrect file name format.");
        }

        if (!identifier.contains("-P")) {
            long id = extractId(identifier);
            checkOwnershipAndSave(file, user, id, newFilename);
        } else {
            checkOwnershipAndSave(file, user, 0L, newFilename);
        }
    }

    private boolean isValidExtension(String extension) {
        return extension.matches("\\.(png|jpg|jpeg|gif)");
    }

    private boolean isValidFilename(String filename) {
        return filename.contains("-P") || filename.contains("-C") || filename.contains("-G");
    }

    private long extractId(String filename) throws StorageException {
        try {
            return Long.parseLong(filename.split("-")[0]);
        } catch (NumberFormatException e) {
            throw new StorageException("Invalid ID format.", e);
        }
    }

    private void checkOwnershipAndSave(MultipartFile file, User user, long id, String newFilename)
            throws IOException {
        if (newFilename.contains("-C")
                && !challengeRepository.findByIdAndUser(id, user).isPresent()) {
            throw new StorageException("The challenge does not belong to you.");
        } else if (newFilename.contains("-G")
                && !goalRepository.findByIdAndUser(id, user).isPresent()) {
            throw new StorageException("The goal does not belong to you.");
        }

        String[] possibleExtensions = {".png", ".jpg", ".jpeg", ".gif"};
        String baseFilename = newFilename.split("\\.")[0];
        for (String ext : possibleExtensions) {
            Path testPath = rootLocation.resolve(baseFilename + ext);
            Files.deleteIfExists(testPath);
        }

        Path destinationFile =
                rootLocation.resolve(Paths.get(newFilename)).normalize().toAbsolutePath();
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public Resource getImage(String baseFilename, UserDetails userDetails) throws IOException {
        User user = userService.findUserByUsername(userDetails.getUsername());
        String[] possibleExtensions = {".png", ".jpg", ".jpeg", ".gif"};

        Path file = null;
        for (String ext : possibleExtensions) {
            Path testPath = rootLocation.resolve(baseFilename + ext);
            if (Files.exists(testPath)) {
                file = testPath;
                break;
            }
        }

        if (!isValidFilename(baseFilename)) {
            throw new StorageException("Incorrect file name format.");
        }

        long id = 0L;
        if (!baseFilename.contains("-P")) {
            id = extractId(baseFilename);
        }

        if (baseFilename.contains("-P")) {
        } else if (baseFilename.contains("-C")
                && !challengeRepository.findByIdAndUser(id, user).isPresent()) {
            throw new StorageException("The challenge does not belong to you.");
        } else if (baseFilename.contains("-G")
                && !goalRepository.findByIdAndUser(id, user).isPresent()) {
            throw new StorageException("The goal does not belong to you.");
        }

        Resource resource;
        try {
            resource = new UrlResource(file.toUri());
        } catch (Exception e) {
            throw new StorageFileNotFoundException("File not found.");
        }

        if (!resource.exists() || !resource.isReadable()) {
            throw new StorageFileNotFoundException("Could not read file: " + baseFilename);
        }

        return resource;
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
