package baithi;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
public class main {
    private static final String FILE_PATH = "src/data/medical_records.csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final Set<String> VIP_TYPES = new HashSet<>(Arrays.asList("VIP I", "VIP II", "VIP III"));

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Thêm mới");
            System.out.println("2. Xóa");
            System.out.println("3. Xem danh sách");
            System.out.println("4. Thoát");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addNewRecord(scanner);
                    break;
                case 2:
                    deleteRecord(scanner);
                    break;
                case 3:
                    viewRecords();
                    break;
                case 4:
                    System.out.println("Thoát chương trình.");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private static void addNewRecord(Scanner scanner) {
        try {

            int recordNumber = getNextRecordNumber();


            System.out.print("Nhập mã bệnh án (BA-XXX): ");
            String recordCode = scanner.nextLine();
            validateRecordCode(recordCode);

            System.out.print("Nhập mã bệnh nhân (BN-XXX): ");
            String patientCode = scanner.nextLine();
            validatePatientCode(patientCode);

            System.out.print("Nhập tên bệnh nhân: ");
            String patientName = scanner.nextLine();

            System.out.print("Nhập ngày nhập viện (dd/MM/yyyy): ");
            String admissionDate = scanner.nextLine();
            validateDate(admissionDate);

            System.out.print("Nhập ngày ra viện (dd/MM/yyyy): ");
            String dischargeDate = scanner.nextLine();
            validateDate(dischargeDate);
            validateDateRange(admissionDate, dischargeDate);

            System.out.print("Nhập lý do nhập viện: ");
            String reasonForAdmission = scanner.nextLine();

            System.out.print("Nhập loại bệnh án (1. Thường, 2. VIP): ");
            int recordType = scanner.nextInt();
            scanner.nextLine();

            if (recordType == 1) {
                System.out.print("Nhập phí nằm viện: ");
                double hospitalFee = scanner.nextDouble();
                scanner.nextLine();
                RegularPatientRecord record = new RegularPatientRecord(recordNumber, recordCode, patientCode, patientName,
                        admissionDate, dischargeDate, reasonForAdmission, hospitalFee);
                appendRecordToFile(record);
            } else if (recordType == 2) {
                System.out.print("Nhập loại VIP (VIP I, VIP II, VIP III1): ");
                String vipType = scanner.nextLine();
                validateVIPType(vipType);

                System.out.print("Nhập thời hạn VIP (dd/MM/yyyy): ");
                String vipDuration = scanner.nextLine();
                validateDate(vipDuration);

                VIPPatientRecord record = new VIPPatientRecord(recordNumber, recordCode, patientCode, patientName,
                        admissionDate, dischargeDate, reasonForAdmission, vipType, vipDuration);
                appendRecordToFile(record);
            } else {
                System.out.println("Loại bệnh án không hợp lệ.");
            }

        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private static void deleteRecord(Scanner scanner) {
        System.out.print("Nhập mã bệnh án cần xóa: ");
        String recordCode = scanner.nextLine();

        try {
            List<PatientRecord> records = loadRecordsFromFile();
            boolean found = false;
            for (PatientRecord record : records) {
                if (record.getRecordCode().equals(recordCode)) {
                    found = true;
                    System.out.print("Xóa bệnh án này? (Yes/No): ");
                    String confirmation = scanner.nextLine();
                    if (confirmation.equalsIgnoreCase("Yes")) {
                        records.remove(record);
                        saveRecordsToFile(records);
                        System.out.println("Bệnh án đã được xóa.");
                    } else {
                        System.out.println("Quay về menu chính.");
                    }
                    break;
                }
            }
            if (!found) {
                System.out.println("Mã bệnh án không tồn tại.");
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi xóa bệnh án: " + e.getMessage());
        }
    }

    private static void viewRecords() {
        try {
            List<PatientRecord> records = loadRecordsFromFile();
            for (PatientRecord record : records) {
                System.out.println(record);
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc dữ liệu: " + e.getMessage());
        }
    }

    private static int getNextRecordNumber() throws IOException {
        List<PatientRecord> records = loadRecordsFromFile();
        return records.isEmpty() ? 1 : records.get(records.size() - 1).getRecordNumber() + 1;
    }

    private static void appendRecordToFile (PatientRecord record) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(record.toCSV());
            writer.newLine();
        }
    }

    private static List<PatientRecord> loadRecordsFromFile() throws IOException {
        List<PatientRecord> records = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return records;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 7) {
                    continue;
                }

                int recordNumber = Integer.parseInt(parts[0]);
                String recordCode = parts[1];
                String patientCode = parts[2];
                String patientName = parts[3];
                String admissionDate = parts[4];
                String dischargeDate = parts[5];
                String reasonForAdmission = parts[6];

                if (parts.length == 8) {
                    double hospitalFee = Double.parseDouble(parts[7]);
                    records.add(new RegularPatientRecord(recordNumber, recordCode, patientCode, patientName,
                            admissionDate, dischargeDate, reasonForAdmission, hospitalFee));
                } else if (parts.length == 10) {
                    String vipType = parts[7];
                    String vipDuration = parts[8];
                    records.add(new VIPPatientRecord(recordNumber, recordCode, patientCode, patientName,
                            admissionDate, dischargeDate, reasonForAdmission, vipType, vipDuration));
                }
            }
        }
        return records;
    }

    private static void saveRecordsToFile(List<PatientRecord> records) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (PatientRecord record : records) {
                writer.write(record.toCSV());
                writer.newLine();
            }
        }
    }

    private static void validateRecordCode(String recordCode) throws IllegalArgumentException {
        if (!recordCode.matches("BA-\\d{3}")) {
            throw new IllegalArgumentException("Mã bệnh án không hợp lệ.");
        }
    }

    private static void validatePatientCode(String patientCode) throws IllegalArgumentException {
        if (!patientCode.matches("BN-\\d{3}")) {
            throw new IllegalArgumentException("Mã bệnh nhân không hợp lệ.");
        }
    }

    private static void validateDate(String date) throws IllegalArgumentException {
        try {
            DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Ngày tháng không hợp lệ.");
        }
    }

    private static void validateDateRange(String admissionDate, String dischargeDate) throws IllegalArgumentException {
        try {
            Date admission = DATE_FORMAT.parse(admissionDate);
            Date discharge = DATE_FORMAT.parse(dischargeDate);
            if (admission.after(discharge)) {
                throw new IllegalArgumentException("Ngày nhập viện không thể sau ngày ra viện.");
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Ngày tháng không hợp lệ.");
        }
    }

    private static void validateVIPType(String vipType) throws IllegalArgumentException {
        if (!VIP_TYPES.contains(vipType)) {
            throw new IllegalArgumentException("Loại VIP không hợp lệ.");
        }
    }
}


