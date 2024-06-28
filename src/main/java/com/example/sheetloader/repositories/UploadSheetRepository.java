package com.example.sheetloader.repositories;

import com.example.sheetloader.config.QueryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Slf4j
@Component
@Repository
public class UploadSheetRepository {
    private static final Logger log = LoggerFactory.getLogger(UploadSheetRepository.class);


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QueryBuilder queryBuilder;

    public boolean checkIfTableExists(String tableName) {
        boolean tableExists = false;

        try {
            tableExists = Boolean
                    .TRUE
                    .equals(this.jdbcTemplate.queryForObject(
                            this.queryBuilder.getCheckTableExistQuery(tableName),
                            Boolean.class
                    ));

            if (tableExists) {
                log.warn("Table named as : {} already exists", tableName);
            } else {
                log.info("Table named as : {} does not exist", tableName);
            }

            return tableExists;
        } catch (Exception e) {
            log.error(e.getMessage());
            return tableExists;
        }
    }

    public boolean createTable(Sheet sheet) {
        String createQuery = null;

        try {
            createQuery = this.queryBuilder.getCreateTableQuery(sheet);

            this.jdbcTemplate.execute(createQuery);
            log.info("SUCCESS: Create Query -- {}", createQuery);
            return true;
        } catch (Exception e) {
            log.error("FAILURE: Create Query -- {}", createQuery);
            log.error(e.getMessage());
            return false;
        }
    }

    public void insertSheetDataIntoDb(Sheet sheet, int lastFilledRow) {
        String insertQuery = null;

        try {
            insertQuery = this.queryBuilder.getInsertValuesQuery(sheet, lastFilledRow);
            this.jdbcTemplate.execute(insertQuery);
            log.info("SUCCESS: Insert Query -- {}", insertQuery);
        } catch (Exception e) {
            log.error("FAILURE: Insert Query -- {}", insertQuery);
            log.error(e.getMessage());
        }
    }
}
