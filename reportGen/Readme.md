# REPORT GENERATOR

## __DESCRIPTION__

This Spring Boot application is designed to process large CSV files and generate reports based on configurable transformation rules. It's capable of handling input and reference files up to 1GB in size, performing data transformations, and producing output reports efficiently.

### Key Features
1. Large File Processing: Handles CSV input and reference files up to 1GB in size.
2. Batch Processing: Implements batch processing to manage memory usage effectively.
3. Configurable Transformations: Allows for flexible data transformation rules.
4. Scheduled Execution: Supports scheduled report generation via cron jobs.
5. On-Demand Processing: Provides a REST API endpoint to trigger report generation on demand.
6. Extensible Design: Built with future enhancements in mind, such as supporting different file formats (Excel, JSON, etc.).

### Core Components
* `FileProcessorService`: Manages reading input files, processing reference data, and writing output files in batches.
* `TransformationService`: Applies business logic to transform input data using reference data.
* `ReportService`: Orchestrates the overall report generation process.
* `FilesUploadService` : Validates and saves file to server to process.
* `SchedulerConfig`: Configures scheduled execution of report generation.
* `ReportController`: Exposes REST API endpoints for on-demand report generation.
* `Uploadoller`: Exposes REST API endpoints for File upload.

### Tech Stack

* Java 17+
* Spring Boot 3.3.1
* OpenCSV for CSV file handling
* SLF4J for logging
* Maven for dependency management and build automation

### Performance Considerations

1. Optimized for processing large files (1GB+) with minimal memory footprint.
2. Implements batch processing to manage memory usage.
3. Utilizes Java streams for efficient data processing.


## API

### 1. POST [http://localhost:8080/api/upload/file]
Upload `input.csv` and `reference.csv` files to server to get processed.

- Method: POST
- Path: `/api/upload/file`
- Parameters [Form Data]: 
    | key             | value         |
    | :--             | :----         |
    | `inputFile`     | input.csv     |
    | `referenceFile` | reference.csv |

- Response : "Files uploaded successfully"


### 2. POST [http://localhost:8080/api/report/generate]
use to generate `output.csv` file.

- Method: POST
- Path: `/api/report/generate`
- Response: "Report generation request received and is being processed."

> NOTE: This  is an `Asynchronous` api. 

### 3. GET [http://localhost:8080/api/report/fetch]
use to download/fetch `output.csv` file.

- Method: GET
- Path: `/api/report/fetch`
- Response: 
    * If File is ready to Download : `Downloads the file on browser`
    * If no file exists:   `File not Found`.
    * If file is being processed : `File is being processed!`


## LOGS
Logs can be found under `/meta-logs/reportGen.log`

## CRON
CRON job is scheduled to run everyday at `01:00 AM` daily.