# Report Generator

Consider you are getting different set of feed files in csv format from an upstream source.
For each set of feeds you need to generate a report  based on certain rules and reference data to lookup from reference files. There can be different set of feed files with each set containing the input.csv.
The report generation may be scheduled and the schedule can change in future.
Create a java spring boot service that is able to ingest these files and create transformed reports in scheduled time.
One should also be able to trigger the report generation via Rest API.

 

## __Considerations__

1. Application may be enhanced in future to allow formats other than csv i.e. excel, json etc

2. Transformation rules can change in future.

3. Output file format can change in future.

4. The service may receive bigger files(more than 1GB file) in a high activity day.

5. Number of input and output fields can range up to 250

5. Report generation for a single file should be fast enough completing in less than 30 seconds

 

> Sample Extract format details for a set ( In actual extract would be bigger and have data)

 

1. input.csv format (can be up to 1 GB in size)

field1(String), field2(String), field3(String), field4(String), field5(Decimal), refkey1(String), refkey2(String)

 

2. reference.csv format linked to input via refkey1, refkey2 (can be up to 1 GB in size)

refkey1(String), refdata1(String), refkey2(String), refdata2(String), refdata3(String),refdata4(Decimal)

 

3. Output.csv format

outfield1, outfield2, outfield3, outfield4, outfield5

 

Transformation Rules (Configurable)

outfield1 =field1+field2

outfield2 = refdata1

outfield3 = refdata2 + refdata3

outfield4 = field3 * max(field5,refdata4)

outfield5 = max(field5,refdata4)

 

## While writing code take care of following points.

1. Code should be committed in GitHub.

2. Appropriate logging should be there.

3. Unit test cases should be there.