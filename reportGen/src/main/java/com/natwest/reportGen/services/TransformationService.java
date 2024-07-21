package com.natwest.reportGen.services;

import com.natwest.reportGen.model.InputRecord;
import com.natwest.reportGen.model.OutputRecord;
import com.natwest.reportGen.model.ReferenceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TransformationService {

    @Autowired
    public FileProcessorService fileProcessorService;

    public void transform() throws Exception {
        Map<String, List<ReferenceRecord>> referenceMap = createReferenceMap(fileProcessorService.readReferenceFile());
        fileProcessorService.processFiles(inputRecords -> transformBatch(inputRecords, referenceMap));
    }

    public Map<String, List<ReferenceRecord>> createReferenceMap(Iterable<ReferenceRecord> referenceRecords) {
        return StreamSupport.stream(referenceRecords.spliterator(), false)
                .collect(Collectors.groupingBy(
                        ref -> ref.getRefkey1() + ":" + ref.getRefkey2()
                ));
    }

    public List<OutputRecord> transformBatch(List<InputRecord> inputRecords, Map<String, List<ReferenceRecord>> referenceMap) {
        return inputRecords.stream()
                .map(input -> transformRecord(input, referenceMap))
                .collect(Collectors.toList());
    }

    public OutputRecord transformRecord(InputRecord input, Map<String, List<ReferenceRecord>> referenceMap) {
        String key = input.getRefkey1() + ":" + input.getRefkey2();
        List<ReferenceRecord> refs = referenceMap.getOrDefault(key, List.of(new ReferenceRecord()));
        ReferenceRecord ref = refs.get(0);  // Using the first one as an example

        String outfield1 = input.getField1() + input.getField2();
        String outfield2 = ref.getRefdata1();
        String outfield3 = ref.getRefdata2() + ref.getRefdata3();
        double outfield4 = Double.parseDouble(input.getField3()) * Math.max(input.getField5(), ref.getRefdata4());
        double outfield5 = Math.max(input.getField5(), ref.getRefdata4());

        return new OutputRecord(outfield1, outfield2, outfield3, outfield4, outfield5);
    }
}