package app.database.service;

import app.database.entities.User;
import app.database.infrastructure.IRepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private IRepositoryUser repositoryUser;

    public Page<User> findPaginated(Pageable pageable, String username) {
        List<User> users = repositoryUser.findAllByUsernameContainingOrderByUsernameAsc(username);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<User> list;

        if (users.size() < startItem)
            list = Collections.emptyList();
        else {
            int toIndex = Math.min(startItem + pageSize, users.size());
            list = users.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), users.size());
    }

    public void saveAvatarImage(MultipartFile imageFile, User user) {
        final String currentDir = System.getProperty("user.dir");
        final String folder = currentDir + "/src/main/resources/static/images/userAvatarImages/";

        try {
            byte[] bytes = imageFile.getBytes();
            Path path = Paths.get(folder + user.getId() + ".jpg");
            Files.write(path, bytes);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
