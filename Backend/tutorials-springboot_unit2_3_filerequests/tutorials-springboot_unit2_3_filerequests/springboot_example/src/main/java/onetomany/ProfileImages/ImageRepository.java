package onetomany.images;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ImageRepository extends JpaRepository<onetomany.images.Image, Integer> {
    List<onetomany.images.Image> findByUserId(Integer userId);
    void deleteByUserIdAndImageId(Integer userId, Integer imageId);
}