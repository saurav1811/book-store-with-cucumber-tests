package com.equalexperts.bookstorewithcucumbertests.repositories;

import com.equalexperts.bookstorewithcucumbertests.entities.Stock;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {

    @Query("SELECT s FROM Stock s WHERE s.book.id IN (SELECT b.id FROM Book b WHERE UPPER(b.name) IN :upperBookNames)")
    List<Stock> findStocksByBookNames(@Param("upperBookNames") List<String> upperBookNames);
}
