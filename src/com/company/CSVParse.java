package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CSVParse {
    public static final String delimiter = ",";

    public void process(File csvFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            String line;
            String[] tempArr;

            List<Patient> list = new ArrayList<>();
            while((line = br.readLine()) != null) {
                tempArr = line.split(delimiter);
                int index = this.indexForPatientWithId(Integer.valueOf(tempArr[0]), list);
                // check if there is an existing patient by id
                if ( index != -1) {
                    // If the incoming version is lower than the existing one, skip
                    if (list.get(index).getVersion() > Integer.valueOf(tempArr[3])) {
                        continue;
                    } else {
                        // update existing one with higher version
                        list.set(index, new Patient(Integer.valueOf(tempArr[0]), tempArr[1], tempArr[2], Integer.valueOf(tempArr[3]), tempArr[4]));
                    }
                } else {
                    // add a patient info
                    list.add(new Patient(Integer.valueOf(tempArr[0]), tempArr[1], tempArr[2], Integer.valueOf(tempArr[3]), tempArr[4]));
                }

                File file = new File(tempArr[4]);
                if (file.exists()) {
                    file.delete();
                }
            }
            br.close();
            // using java 8 stream api
            Comparator<Patient> compareByName = Comparator
                    .comparing(Patient::getFirstName)
                    .thenComparing(Patient::getLastName);
            List<Patient> sortedPatients = list.stream()
                    .sorted(compareByName)
                    .collect(Collectors.toList());
            //System.out.println(sortedPatients);
            this.writeToFile(sortedPatients);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
    public static void main(String[] args) {
        // csv files to read
        File dir = new File("files");
        File[] files = dir.listFiles();
        for (File csvFile: files) {
            System.out.println(csvFile.getName());
            new CSVParse().process(csvFile);
        }
    }

    private int indexForPatientWithId(Integer id, List<Patient> sortedPatients) {
        for (int i = 0; i < sortedPatients.size(); i++) {
            if (sortedPatients.get(i).getId().intValue() == id.intValue()) {
                return i;
            }
        }
        return -1;
    }

    private void writeToFile(List<Patient> sortedPatients) throws IOException {
        BufferedWriter writer;
        for (Patient patient: sortedPatients) {
            File file = new File(patient.getInsuranceName());
            writer = new BufferedWriter(new FileWriter(file, true));
            StringBuilder str = new StringBuilder();
            str.append(patient.getId().toString()).append(' ').append(patient.getFirstName()).append(' ').append(patient.getLastName()).append(' ')
                    .append(patient.getVersion().toString());
            writer.append(str);
            writer.append('\n');
            writer.close();
        }
    }
}
