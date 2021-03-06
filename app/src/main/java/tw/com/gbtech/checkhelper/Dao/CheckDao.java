package tw.com.gbtech.checkhelper.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import tw.com.gbtech.checkhelper.Entity.Check;
import tw.com.gbtech.checkhelper.Entity.TotalBoxCheck;
import tw.com.gbtech.checkhelper.Entity.TotalCheck;

@Dao
public interface CheckDao {
    @Query("SELECT * FROM `check`")
    List<Check> getAll();
    @Query("SELECT * FROM `check` WHERE store = :store AND checkDate = :checkDate ORDER BY name")
    List<Check> getCheck(String store, String checkDate);
    @Query("SELECT * FROM `check` WHERE store = :store AND checkDate = :checkDate AND barcode = :barcode AND name = :boxnum")
    List<Check> getCheck(String store, String checkDate,String boxnum, String barcode);
    @Query("SELECT store, checkDate, SUM(quantity) AS quantity FROM `check` GROUP BY store, checkDate")
    List<TotalCheck> getTotalCheck();

    @Query("SELECT name as boxNum, quantity,SUM(quantity) AS quantity FROM `check` WHERE store= :store AND checkDate= :checkDate GROUP BY Name")
    List<TotalBoxCheck> getTotalBoxCheck(String store, String checkDate);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Check ... checks);


    @Query("DELETE FROM `check` WHERE store= :store AND checkDate= :checkDate")
    void  deleteByStoreAndDate(String store,String checkDate);
}
