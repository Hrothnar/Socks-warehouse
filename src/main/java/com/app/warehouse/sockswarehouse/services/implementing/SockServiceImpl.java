package com.app.warehouse.sockswarehouse.services.implementing;

import com.app.warehouse.sockswarehouse.exceptions.JsonIOException;
import com.app.warehouse.sockswarehouse.exceptions.NotFoundException;
import com.app.warehouse.sockswarehouse.exceptions.RuntimeIOException;
import com.app.warehouse.sockswarehouse.models.Sock;
import com.app.warehouse.sockswarehouse.models.SockJournal;
import com.app.warehouse.sockswarehouse.models.SockPair;
import com.app.warehouse.sockswarehouse.models.enums.OperationType;
import com.app.warehouse.sockswarehouse.services.SockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;

@Service
public class SockServiceImpl implements SockService {
    @Value("${app.exportWarehouse.filepath}")
    private String warehouseFilePath;
    @Value("${app.exportJournal.fileDirectory}")
    private String journalFileDirectory;
    @Value("${app.exportJournal.fileName}")
    private String journalFileName;

    private LinkedList<SockJournal> journal = new LinkedList<>();
    private HashSet<SockPair> warehouse = new HashSet<>();


    @Override
    public boolean addSocks(SockPair newSupply) {
        boolean isNotExist = true;
        if (ObjectUtils.isNotEmpty(newSupply) && newSupply.getSock().getComposition() > 0
                && newSupply.getSock().getComposition() < 100) {
            addToJournal(newSupply, OperationType.ADDITION);
            for (SockPair sockPair : warehouse) {
                if (sockPair.getSock().equals(newSupply.getSock())) {
                    sockPair.setQuantity(sockPair.getQuantity() + newSupply.getQuantity());
                    isNotExist = false;
                    break;
                }
            }
            if (isNotExist) {
                warehouse.add(newSupply);
            }
            saveToFile();
            return true;
        }
        return false;
    }

    @Override
    public boolean addSocksFromFile(SockJournal newSupply) {
        boolean isNotExist = true;
        SockPair pair = newSupply.getSockPair();
        if (ObjectUtils.isNotEmpty(newSupply) && pair.getSock().getComposition() > 0
                && pair.getSock().getComposition() < 100) {
            addToJournal(newSupply);
            for (SockPair sockPair : warehouse) {
                if (sockPair.getSock().equals(pair.getSock())) {
                    sockPair.setQuantity(sockPair.getQuantity() + pair.getQuantity());
                    isNotExist = false;
                    break;
                }
            }
            if (isNotExist) {
                warehouse.add(pair);
            }
            saveToFile();
            return true;
        }
        return false;
    }


    @Override
    public boolean realizeGoods(SockPair newRealise) {
        if (ObjectUtils.isNotEmpty(newRealise) && newRealise.getSock().getComposition() > 0 && newRealise.getSock().getComposition() < 100) {
            addToJournal(newRealise, OperationType.REALIZATION);
            for (SockPair sockPair : warehouse) {
                if (sockPair.getSock().equals(newRealise.getSock()) && sockPair.getQuantity() >= newRealise.getQuantity()) {
                    sockPair.setQuantity(sockPair.getQuantity() - newRealise.getQuantity());
                    saveToFile();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean realizeGoodsFromFile(SockJournal newRealise) {
        if (ObjectUtils.isNotEmpty(newRealise) && newRealise.getSockPair().getSock().getComposition() > 0 && newRealise.getSockPair().getSock().getComposition() < 100) {
            addToJournal(newRealise);
            for (SockPair sockPair : warehouse) {
                if (sockPair.getSock().equals(newRealise.getSockPair().getSock()) && sockPair.getQuantity() >= newRealise.getSockPair().getQuantity()) {
                    sockPair.setQuantity(sockPair.getQuantity() - newRealise.getSockPair().getQuantity());
                    saveToFile();
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public boolean removeDefectiveGoods(SockPair newWriteOff) {
        if (ObjectUtils.isNotEmpty(newWriteOff) && newWriteOff.getSock().getComposition() > 0 && newWriteOff.getSock().getComposition() < 100) {
            addToJournal(newWriteOff, OperationType.REMOVE);
            for (SockPair sockPair : warehouse) {
                if (sockPair.getSock().equals(newWriteOff.getSock()) && sockPair.getQuantity() >= newWriteOff.getQuantity()) {
                    sockPair.setQuantity(sockPair.getQuantity() - newWriteOff.getQuantity());
                    saveToFile();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean removeDefectiveGoodsFromFile(SockJournal newWriteOff) {
        SockPair pair = newWriteOff.getSockPair();
        addToJournal(newWriteOff);
        if (ObjectUtils.isNotEmpty(newWriteOff) && pair.getSock().getComposition() > 0 && pair.getSock().getComposition() < 100) {
            for (SockPair sockPair : warehouse) {
                if (sockPair.getSock().equals(pair.getSock()) && sockPair.getQuantity() >= pair.getQuantity()) {
                    sockPair.setQuantity(sockPair.getQuantity() - pair.getQuantity());
                    saveToFile();
                    return true;
                }
            }
        }
        return false;
    }

    private void addToJournal(SockPair sockPair, OperationType operation) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            String value = objectMapper.writeValueAsString(sockPair);
            SockPair clonedSockPair = objectMapper.readValue(value, SockPair.class);
            SockJournal sockJournal = new SockJournal(clonedSockPair, operation, LocalDateTime.now());
            journal.add(sockJournal);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonIOException(e);
        }
    }

    private void addToJournal(SockJournal sockJournal) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            String value = objectMapper.writeValueAsString(sockJournal);
            SockJournal clonedSockJournal = objectMapper.readValue(value, SockJournal.class);
            journal.add(clonedSockJournal);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonIOException(e);
        }
    }


    @Override
    public int getQuantityOfSocks(String color, int size, Integer cottonMin, Integer cottonMax) {
        int count = 0;
        if (cottonMax == null) {
            cottonMax = 99;
        }
        if (cottonMin == null) {
            cottonMin = 0;
        }
        for (SockPair sockPair : warehouse) {
            Sock sock = sockPair.getSock();
            if (sock.getColor().getColor().equals(color)
                    && sock.getSize().getSize() == size
                    && sock.getComposition() >= cottonMin
                    && sock.getComposition() <= cottonMax) {
                count += sockPair.getQuantity();
            }
        }
        return count;
    }


    @Override
    public void saveToFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        ObjectMapper objectMapper1 = new ObjectMapper();
        objectMapper1.findAndRegisterModules();
        try {
            String stringWarehouse = objectMapper.writeValueAsString(warehouse);
            Files.writeString(Path.of(warehouseFilePath), stringWarehouse);
            String stringJournal = objectMapper.writeValueAsString(journal);
            Files.writeString(Path.of(journalFileDirectory, journalFileName), stringJournal);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonIOException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeIOException(e);
        }
    }


    @Override
    public boolean addFromFile(InputStream inputStream) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        try {
            LinkedList<SockJournal> tempList = objectMapper.readValue(inputStream, new TypeReference<>() {});
            for (SockJournal sockJournal : tempList) {
                if (sockJournal.getOperationType().getType().equals(OperationType.ADDITION.getType())) {
                    addSocksFromFile(sockJournal);
                } else if (sockJournal.getOperationType().getType().equals(OperationType.REALIZATION.getType())) {
                realizeGoodsFromFile(sockJournal);
                } else {
                removeDefectiveGoodsFromFile(sockJournal);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeIOException(e);
        }
        return true;
    }


    @PostConstruct
    private void initialMethod() {
        try {
            if (Files.size(Path.of(journalFileDirectory, journalFileName)) != 0) {
                File in = new File(journalFileDirectory + journalFileName);
                File out = new File(journalFileDirectory + "temp" + journalFileName);
                FileUtils.copyFile(in, out);
                addFromFile(new FileInputStream(out));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new NotFoundException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeIOException(e);
        }
    }


}
