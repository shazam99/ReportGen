package com.natwest.reportGen.services;

import com.natwest.reportGen.model.InputRecord;
import com.natwest.reportGen.model.OutputRecord;
import com.natwest.reportGen.model.ReferenceRecord;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@Service
public class FileProcessorService {

    @Value("${file.upload-dir}")
    private String filePath;

    private static final org.apache.log4j.Logger logger = Logger.getLogger(FileProcessorService.class.getName());

    private static final int BATCH_SIZE = 10000;

    public void processFiles(Function<List<InputRecord>, List<OutputRecord>> transformFunction) throws Exception {
        try (BufferedReader inputReader = new BufferedReader(new FileReader(filePath + "input.csv"));
             CSVReader csvInputReader = new CSVReader(inputReader);
             BufferedWriter outputWriter = new BufferedWriter(new FileWriter(filePath + "output.csv"));
             CSVWriter csvOutputWriter = new CSVWriter(outputWriter)) {

            // Skip header
            csvInputReader.readNext();

            // Write output header
            csvOutputWriter.writeNext(new String[]{"outfield1", "outfield2", "outfield3", "outfield4", "outfield5"});

            List<InputRecord> batch = new ArrayList<>(BATCH_SIZE);
            String[] line;
            while ((line = csvInputReader.readNext()) != null) {
                batch.add(mapToInputRecord(line));
                if (batch.size() >= BATCH_SIZE) {
                    processBatch(batch, transformFunction, csvOutputWriter);
                    batch.clear();
                }
            }
            // Process remaining records
            if (!batch.isEmpty()) {
                processBatch(batch, transformFunction, csvOutputWriter);
            }
        }
        finally {
            deleteFile(filePath + "input.csv");
            deleteFile(filePath + "reference.csv");
        }
    }

    private void processBatch(List<InputRecord> batch, Function<List<InputRecord>, List<OutputRecord>> transformFunction, CSVWriter writer) throws IOException {
        List<OutputRecord> outputRecords = transformFunction.apply(batch);
        for (OutputRecord record : outputRecords) {
            writer.writeNext(record.toCsvRow());
        }
    }

    private InputRecord mapToInputRecord(String[] row) {
        return new InputRecord(row[0], row[1], row[2], row[3], Double.parseDouble(row[4]), row[5], row[6]);
    }

    public Iterable<ReferenceRecord> readReferenceFile() {
        return () -> new Iterator<ReferenceRecord>() {
            private BufferedReader reader;
            private CSVReader csvReader;
            private String[] nextLine;

            {
                try {
                    reader = new BufferedReader(new FileReader(filePath + "reference.csv"));
                    csvReader = new CSVReader(reader);
                    // Skip header
                    csvReader.readNext();
                    nextLine = csvReader.readNext();
                } catch (Exception e) {
                    throw new RuntimeException("Error initializing reference file reader", e);
                }
            }

            @Override
            public boolean hasNext() {
                return nextLine != null;
            }

            @Override
            public ReferenceRecord next() {
                ReferenceRecord record = mapToReferenceRecord(nextLine);
                try {
                    nextLine = csvReader.readNext();
                    if (nextLine == null) {
                        csvReader.close();
                        reader.close();
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Error reading next line from reference file", e);
                } catch (CsvValidationException e) {
                    throw new RuntimeException(e);
                }
                return record;
            }
        };
    }

    private ReferenceRecord mapToReferenceRecord(String[] row) {
        return new ReferenceRecord(row[0], row[1], row[2], row[3], row[4], Double.parseDouble(row[5]));
    }

    private void deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.delete(path);
            logger.info("File deleted : " + filePath);
        } catch (Exception e) {
            logger.error("Error deleting file : " + filePath);
        }
    }
}