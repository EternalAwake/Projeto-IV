package com.projeto.songSystem.repositories;

import com.projeto.songSystem.models.SetlistItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface SetlistItemRepository extends JpaRepository<SetlistItemModel, Long> {

    List<SetlistItemModel> findBySetlistIdOrderByOrdemAsc(Long setlistId);

    @Modifying
    @Transactional
    @Query("DELETE FROM SetlistItemModel s WHERE s.setlist.id = :setlistId")
    void deleteBySetlistId(Long setlistId);

}