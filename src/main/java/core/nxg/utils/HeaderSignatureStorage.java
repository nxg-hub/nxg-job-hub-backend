package core.nxg.utils;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HeaderSignatureStorage extends JpaRepository<HeaderSignature, Long> {
    @Query("select (count(h) > 0) from HeaderSignature h where upper(h.signature) = upper(:signature)")
    boolean existsByValueIgnoreCase(@Param("signature") String signature);
}
