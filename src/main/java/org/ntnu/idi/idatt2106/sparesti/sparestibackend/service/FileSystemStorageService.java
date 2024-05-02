package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

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
import org.springframework.web.multipart.MultipartFile;

/**
 * Service responsible for uploading and getting images for users, challenges and goals.
 *
 * @author L.M.L. Nilsen
 * @version 1.0
 * @since 1.5.24
 */
@Service
public class FileSystemStorageService {

    private final UserService userService;
    private final ChallengeRepository challengeRepository;
    private final GoalRepository goalRepository;
    private final StorageProperties properties;
    private final Path rootLocation;

    /**
     * Constructor for FileSystemStorageService
     * @param userService User service for communicating with users
     * @param challengeRepository Challenge repo for finding challenges
     * @param goalRepository Goal repo for finding goals
     * @param properties Storage properties
     */
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

    /**
     * Save a valid image for a user, challenge or goal depending on the filename.
     * If the filename has the format {username}-P.png the image belongs to a user,
     * {goalId}-G.png for goals and {challengeId}-C.png for a challenge.
     * The valid filetypes are png, jpg, jpeg and gif.
     *
     * @param file The image file that is getting saved.
     * @param identifier The filename without the extension.
     * @param userDetails UserDetails for user who wants to save image.
     * @throws IOException If the file is empty.
     * @throws StorageException If the file type is unsupported, or the filename format is wrong.
     */
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

    /**
     * Checks if a file extension is supported.
     * The supported file extensions are png, jpg, jpeg and gif.
     * @param extension The file extension being checked.
     * @return true or false depending on if the extension is valid.
     */
    private boolean isValidExtension(String extension) {
        return extension.matches("\\.(png|jpg|jpeg|gif)");
    }

    /**
     * Checks if a filename is supported.
     * The supported filenames are {username}-P.extension,
     * {goalId}-G.extension and {challengeId}-C.extension
     * @param filename The filename being checked
     * @return true or false depending on if the filename is valid.
     */
    private boolean isValidFilename(String filename) {
        return filename.contains("-P") || filename.contains("-C") || filename.contains("-G");
    }

    /**
     * Extracts the ID from a filename.
     * The id can be either a username,
     * challengeID or GoalID.
     * @param filename The filename to extract the ID from.
     * @return The ID in the filename if the ID format is valid.
     * @throws StorageException If the ID format is invalid.
     */
    private long extractId(String filename) throws StorageException {
        try {
            return Long.parseLong(filename.split("-")[0]);
        } catch (NumberFormatException e) {
            throw new StorageException("Invalid ID format.", e);
        }
    }

    /**
     * Checks the ownership of goal or challenge, before saving the file.
     * Used so that only the user that owns the challenge or goal with the given ID can save images for it.
     * @param file The file to save.
     * @param user The user wanting to save the file.
     * @param id The ID of the challenge or goal.
     * @param newFilename The new filename of the file that is being stored.
     * @throws IOException If method has problems writing file.
     * @throws StorageException If the challenge or goal does not belong to the user trying to save it.
     */
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

    /**
     * Gets an image for a user, challenge or goal.
     * @param baseFilename The name of the file, without the file extension.
     * @param userDetails The UserDetails of the user wanting to get an image.
     * @return Returns the file as a Resource.
     * @throws StorageException If the challenge or goal does not belong to the user.
     */
    public Resource getImage(String baseFilename, UserDetails userDetails) {
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
}
