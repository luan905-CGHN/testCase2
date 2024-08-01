package baithi;

abstract class PatientRecord {
    private int recordNumber;
    private String recordCode;
    private String patientCode;
    private String patientName;
    private String admissionDate;
    private String dischargeDate;
    private String reasonForAdmission;

    public PatientRecord(int recordNumber, String recordCode, String patientCode, String patientName,
                         String admissionDate, String dischargeDate, String reasonForAdmission) {
        this.recordNumber = recordNumber;
        this.recordCode = recordCode;
        this.patientCode = patientCode;
        this.patientName = patientName;
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.reasonForAdmission = reasonForAdmission;
    }


    public PatientRecord() {
    }


    public String getRecordCode() {
        return recordCode;
    }

    public String getPatientCode() {
        return patientCode;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public String getReasonForAdmission() {
        return reasonForAdmission;
    }

    public String toCSV() {
        return null;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setRecordCode(String recordCode) {
        this.recordCode = recordCode;
    }

    public void setPatientCode(String patientCode) {
        this.patientCode = patientCode;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public void setReasonForAdmission(String reasonForAdmission) {
        this.reasonForAdmission = reasonForAdmission;
    }

    @Override
    public String toString() {
        return recordNumber + "," + recordCode + "," + patientCode + "," + patientName + "," +
                admissionDate + "," + dischargeDate + "," + reasonForAdmission;
    }


}

class RegularPatientRecord extends PatientRecord {
    private double hospitalFee;

    public RegularPatientRecord(int recordNumber, String recordCode, String patientCode, String patientName,
                                String admissionDate, String dischargeDate, String reasonForAdmission, double hospitalFee) {
        super(recordNumber, recordCode, patientCode, patientName, admissionDate, dischargeDate, reasonForAdmission);
        this.hospitalFee = hospitalFee;
    }

    @Override
    public String toCSV() {
        return super.toCSV() + "," + hospitalFee;
    }
}

class VIPPatientRecord extends PatientRecord {
    private String vipType;
    private String vipDuration;

    public VIPPatientRecord(int recordNumber, String recordCode, String patientCode, String patientName,
                            String admissionDate, String dischargeDate, String reasonForAdmission, String vipType, String vipDuration) {
        super(recordNumber, recordCode, patientCode, patientName, admissionDate, dischargeDate, reasonForAdmission);
        this.vipType = vipType;
        this.vipDuration = vipDuration;
    }

    @Override
    public String toCSV() {
        return super.toCSV() + "," + vipType + "," + vipDuration;
    }
}
