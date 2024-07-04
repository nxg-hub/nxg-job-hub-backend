package core.nxg.utils;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


public interface HeaderSignatureStorage extends MongoRepository<HeaderSignature, String> {
//    @Query("{ 'signature' : { $regex: ?0, $options: 'i' } }")
//    boolean existsByValueIgnoreCase(@Param("signature") String signature);
    boolean existsBySignature(String signature);
}

