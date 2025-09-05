/*
 * This file is part of the OWL API.
 *  
 * The contents of this file are subject to the LGPL License, Version 3.0.
 *
 * Copyright (C) 2011, The University of Manchester
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 *
 *  
 * Alternatively, the contents of this file may be used under the terms of the Apache License, Version 2.0
 * in which case, the provisions of the Apache License Version 2.0 are applicable instead of those above.
 *
 * Copyright 2011, The University of Manchester
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.semanticweb.binaryowl.change;

import com.google.common.collect.ImmutableList;
import org.semanticweb.binaryowl.BinaryOWLMetadata;
import org.semanticweb.binaryowl.BinaryOWLParseException;
import org.semanticweb.binaryowl.BinaryOWLVersion;
import org.semanticweb.binaryowl.chunk.ChunkUtil;
import org.semanticweb.binaryowl.chunk.SkipSetting;
import org.semanticweb.binaryowl.chunk.TimeStampedMetadataChunk;
import org.semanticweb.binaryowl.lookup.IRILookupTable;
import org.semanticweb.binaryowl.lookup.LookupTable;
import org.semanticweb.binaryowl.stream.BinaryOWLInputStream;
import org.semanticweb.binaryowl.stream.BinaryOWLOutputStream;
import org.semanticweb.owlapi.change.*;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/05/2012
 * <p>
 *     Represents a list of {@link OWLOntologyChangeRecord}s with a timestamp and metadata.
 * </p>
 */
public class OntologyChangeRecordList implements TimeStampedMetadataChunk {
    
    private static final short VERSION_1 = 1;

    private static final short VERSION_2 = 2;

    private static final short VERSION_3 = 3;

    public static final int CHUNK_TYPE_MARKER = ChunkUtil.toInt("ochr");

    private static final int CHUNK_TYPE_AND_LENGTH_SIZE = 8;

    private long timestamp;
    
    private BinaryOWLMetadata metadata;
    
    private ImmutableList<OWLOntologyChangeRecord> changeRecords;
    
    public OntologyChangeRecordList(List<OWLOntologyChange> changes, long timestamp, BinaryOWLMetadata metadata) {
        this(timestamp, metadata, convertToChangeRecordList(changes));
    }

    private static List<OWLOntologyChangeRecord> convertToChangeRecordList(List<OWLOntologyChange> changes) {
        List<OWLOntologyChangeRecord> records = new ArrayList<OWLOntologyChangeRecord>(changes.size() + 1);
        for(OWLOntologyChange change : changes) {
            records.add(change.getChangeRecord());
        }
        return records;
    }

    public OntologyChangeRecordList(long timestamp, BinaryOWLMetadata metadata, List<OWLOntologyChangeRecord> changeRecords) {
        this(timestamp, metadata, ImmutableList.copyOf(changeRecords));
    }


    public OntologyChangeRecordList(long timestamp, BinaryOWLMetadata metadata, ImmutableList<OWLOntologyChangeRecord> changeRecords) {
        this.timestamp = timestamp;
        this.metadata = checkNotNull(metadata);
        this.changeRecords = checkNotNull(changeRecords);
    }
    
    public OntologyChangeRecordList(BinaryOWLInputStream inputStream, SkipSetting skipSetting) throws IOException, BinaryOWLParseException {
        read(inputStream, skipSetting);
    }

    public int getChunkType() {
        return CHUNK_TYPE_MARKER;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public BinaryOWLMetadata getMetadata() {
        return metadata;
    }

    public List<OWLOntologyChangeRecord> getChangeRecords() {
        return changeRecords;
    }

    public List<OntologyChangeRecordRun> getRuns() {
        List<OntologyChangeRecordRun> result = new ArrayList<OntologyChangeRecordRun>();
        if (!changeRecords.isEmpty()) {
            OWLOntologyID currentRunId = null;
            List<OWLOntologyChangeData> runInfoList = new ArrayList<OWLOntologyChangeData>();
            for(OWLOntologyChangeRecord record : changeRecords) {
                if(currentRunId == null) {
                    // First run
                    currentRunId = record.getOntologyID();
                }
                else if(!currentRunId.equals(record.getOntologyID())) {
                    // Store current run
                    result.add(new OntologyChangeRecordRun(currentRunId, new ArrayList<OWLOntologyChangeData>(runInfoList)));
                    // Reset for fresh run
                    currentRunId = record.getOntologyID();
                    runInfoList.clear();
                }
                // Add to current run
                runInfoList.add(record.getData());
            }
            result.add(new OntologyChangeRecordRun(currentRunId, new ArrayList<OWLOntologyChangeData>(runInfoList)));
        }
        return result;
    }
    
    public void write(BinaryOWLOutputStream os) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream bufDataOutputStream = new DataOutputStream(buf);

        IRILookupTable iriLookupTable = new IRILookupTable(getChangeSignature());
        LookupTable lookupTable = new LookupTable(iriLookupTable);
        BinaryOWLOutputStream bufOWLOutputStream = new BinaryOWLOutputStream(bufDataOutputStream, lookupTable, BinaryOWLVersion.getVersion(VERSION_3));

        // Record format version
        bufDataOutputStream.writeShort(VERSION_3);

        // LookupTable
        lookupTable.getIRILookupTable().write(bufDataOutputStream);

        // Timestamp
        bufDataOutputStream.writeLong(timestamp);
        
        // Metadata:  Size and Data
        ByteArrayOutputStream metadataBuffer = new ByteArrayOutputStream();
        DataOutputStream metadataDataOutputStream = new DataOutputStream(metadataBuffer);
        BinaryOWLOutputStream metadataOWLOutputStream = new BinaryOWLOutputStream(metadataDataOutputStream, os.getVersion());
        metadata.write(metadataOWLOutputStream);
        bufDataOutputStream.writeInt(metadataBuffer.size());
        metadataBuffer.writeTo(bufDataOutputStream);

        // Split into runs - saves us repeatedly storing the same ontology id.
        List<OntologyChangeRecordRun> runs = getRuns();

        bufDataOutputStream.writeInt(runs.size());
        for(OntologyChangeRecordRun run : runs) {
            run.write(bufOWLOutputStream);
        }
        bufOWLOutputStream.flush();


        // Size, Type, Data
        final int bufferSize = buf.size();
        os.writeInt(bufferSize);
        os.writeInt(CHUNK_TYPE_MARKER);
        buf.writeTo(os);
    }

    /**
     * Gets the signature in the set of change records in this list.
     * @return The signature.
     */
    private Set<OWLEntity> getChangeSignature() {
        Set<OWLEntity> result = new HashSet<OWLEntity>();
        final OWLOntologyChangeDataVisitor<Set<OWLEntity>, RuntimeException> visitor = new OWLOntologyChangeDataVisitor<Set<OWLEntity>, RuntimeException>() {
            @Override
            public Set<OWLEntity> visit(AddAxiomData data) throws RuntimeException {
                return data.getAxiom().getSignature();
            }

            @Override
            public Set<OWLEntity> visit(RemoveAxiomData data) throws RuntimeException {
                return data.getAxiom().getSignature();
            }

            @Override
            public Set<OWLEntity> visit(AddOntologyAnnotationData data) throws RuntimeException {
                return data.getAnnotation().getSignature();
            }

            @Override
            public Set<OWLEntity> visit(RemoveOntologyAnnotationData data) throws RuntimeException {
                return data.getAnnotation().getSignature();
            }

            @Override
            public Set<OWLEntity> visit(SetOntologyIDData data) throws RuntimeException {
                return Collections.emptySet();
            }

            @Override
            public Set<OWLEntity> visit(AddImportData data) throws RuntimeException {
                return Collections.emptySet();
            }

            @Override
            public Set<OWLEntity> visit(RemoveImportData data) throws RuntimeException {
                return Collections.emptySet();
            }
        };

        for(OWLOntologyChangeRecord record : changeRecords) {
            result.addAll(record.getData().accept(visitor));
        }
        return result;
    }

    private void read(BinaryOWLInputStream inputStream, SkipSetting skipSetting) throws IOException, BinaryOWLParseException {
        long startPos = inputStream.getBytesRead();
        // Size
        int chunkSize = inputStream.readInt();
        // Marker
        int chunkTypeMarker = inputStream.readInt();
        if(chunkTypeMarker != CHUNK_TYPE_MARKER) {
            throw new BinaryOWLParseException("Expected chunk type 0x" + Integer.toHexString(CHUNK_TYPE_MARKER) + " but encountered 0x" + Integer.toHexString(chunkTypeMarker));
        }

        // Record format version
        short versionNumber = inputStream.readShort();
        // For the moment we can only handle version 1 stuff
        if(versionNumber != VERSION_1 && versionNumber != VERSION_2 && versionNumber != VERSION_3) {
            throw new BinaryOWLParseException("Invalid version specifier.  Found 0x" + Integer.toHexString(versionNumber) + " but expected 0x" + Integer.toHexString(VERSION_1) + " or 0x" + Integer.toHexString(VERSION_2));
        }

        inputStream.setVersion(BinaryOWLVersion.getVersion(versionNumber));

        if(versionNumber >= VERSION_2) {
            IRILookupTable iriLookupTable = inputStream.readIRILookupTable();
            LookupTable lookupTable = new LookupTable(iriLookupTable);
            inputStream.pushLookupTable(lookupTable);
        }

        // Actual data
        timestamp = inputStream.readLong();

        int metadataSize = inputStream.readInt();
        if(skipSetting.isSkipMetadata()) {
            inputStream.skipBytes(metadataSize);
            metadata = new BinaryOWLMetadata();
        }
        else {
            metadata = new BinaryOWLMetadata(inputStream);
        }
        long curPos = inputStream.getBytesRead();
        if(skipSetting.isSkipData()) {
            int bytesToSkip = chunkSize - (int)(curPos - startPos) + CHUNK_TYPE_AND_LENGTH_SIZE;
            inputStream.skipBytes(bytesToSkip);
            changeRecords = ImmutableList.of();
        }
        else {
            changeRecords = readRecords(inputStream);
        }

        long nextPos = inputStream.getBytesRead();
        if(versionNumber == VERSION_2) {
            inputStream.popLookupTable();
        }
    }

    private static ImmutableList<OWLOntologyChangeRecord> readRecords(BinaryOWLInputStream inputStream) throws IOException, BinaryOWLParseException {
        int numberOfRuns = inputStream.readInt();
        ImmutableList.Builder<OWLOntologyChangeRecord> recordsBuilder = ImmutableList.builder();
        for(int i = 0; i < numberOfRuns; i++) {
            OntologyChangeRecordRun run = new OntologyChangeRecordRun(inputStream);
            for(OWLOntologyChangeData info : run.getChangeDataList()) {
                recordsBuilder.add(new OWLOntologyChangeRecord(run.getOntologyID(), info));
            }
        }
        return recordsBuilder.build();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OntologyChangeRecordList(");
        sb.append(timestamp);
        sb.append(" ");
        sb.append(metadata);
        sb.append(" ");
        for(OWLOntologyChangeRecord record : changeRecords) {
            sb.append(" ");
            sb.append(record);
            sb.append(" ");
        }
        sb.append(")");
        return sb.toString();
    }



    //    private static void skipToTimestamp(DataInput dataInput) throws IOException {
//        // Size (4 bytes) + Type (4 bytes) + Timestamp (8 bytes)
//        final int bytesToSkip = 8;
//        long skipped = dataInput.skip(bytesToSkip);
//        if(skipped != bytesToSkip) {
//            throwSkipFailure(bytesToSkip, skipped);
//        }
//    }
//
//    public static long readTimeStamp(DataInput dataInput) throws IOException {
//        skipToTimestamp(dataInput);
//        return dataInput.readLong();
//    }
//    
//    private static void skipToMetadata(DataInput dataInput) throws IOException {
//        skipToTimestamp(dataInput);
//        // Time stamp is 8 bytes
//        final int bytesToSkip = 8;
//        long skipped = dataInput.skip(bytesToSkip);
//        if(skipped != bytesToSkip) {
//            throwSkipFailure(bytesToSkip, skipped);
//        }
//    }
//    
//    public static BinaryOWLMetadata readMetadata(DataInput dataInput, OWLDataFactory dataFactory) throws IOException {
//        skipToTimestamp(dataInput);
//        long skippedBytes = dataInput.skip(4);
//        if(skippedBytes != 4) {
//            throwSkipFailure(4, skippedBytes);
//        }
//        return new BinaryOWLMetadata(dataInput, dataFactory);
//    }
//
//    private List<OWLOntologyChangeRecord> skipToData(DataInput dataInput) throws IOException {
//        skipToMetadata(dataInput);
//        int metadataSize = dataInput.readInt();
//        long skippedBytes = dataInput.skip(metadataSize);
//        if(skippedBytes != metadataSize) {
//            throwSkipFailure(metadataSize, skippedBytes);
//        }
//        
//    }




//    private static void throwSkipFailure(long bytesToSkip, long skipped) throws IOException {
//        throw new IOException("Tried to skip " + bytesToSkip + " bytes, but only skipped " + skipped + " bytes");
//    }
//    
}
