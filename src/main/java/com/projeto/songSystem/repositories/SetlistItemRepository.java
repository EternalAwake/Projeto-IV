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

    // Itens de setlist que referenciam um determinado item de repertório.
    // Usado ao excluir uma música: cada SetlistItem aponta para um RepertorioItem,
    // que por sua vez aponta para a música.
    List<SetlistItemModel> findByRepertorioItemId(Long repertorioItemId);

    @Modifying
    @Transactional
    @Query("DELETE FROM SetlistItemModel s WHERE s.repertorioItem.id = :repertorioItemId")
    void deleteByRepertorioItemId(Long repertorioItemId);

}