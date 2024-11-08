package onetomany.images;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImageRepository extends JpaRepository<onetomany.images.Image, Integer> {
    List<onetomany.images.Image> findByUserId(Integer userId);
    void deleteByUserIdAndImageId(Integer userId, Integer imageId);
}