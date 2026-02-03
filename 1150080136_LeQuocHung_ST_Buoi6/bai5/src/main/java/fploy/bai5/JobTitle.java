package fploy.bai5;

public class JobTitle {
    private String title;
    private String description;
    private String specificationPath;
    private String note;

    public JobTitle() {
    }

    public JobTitle(String title, String description, String specificationPath, String note) {
        this.title = title;
        this.description = description;
        this.specificationPath = specificationPath;
        this.note = note;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecificationPath() {
        return specificationPath;
    }

    public void setSpecificationPath(String specificationPath) {
        this.specificationPath = specificationPath;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // Validation
    public void validate() {
        // Partition 1 & 3: Job Title Validation
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Job Title cannot be empty!");
        }
        if (title.length() > 100) {
            throw new IllegalArgumentException("Job Title cannot exceed 100 characters!");
        }

        // Partition 4-6: Description Validation
        if (description != null && description.length() > 400) {
            throw new IllegalArgumentException("Description cannot exceed 400 characters!");
        }

        // Partition 10-12: Note Validation
        if (note != null && note.length() > 400) {
            throw new IllegalArgumentException("Note cannot exceed 400 characters!");
        }

        // Partition 7-9: Job Specification (File) Validation
        if (specificationPath != null && !specificationPath.isEmpty() && !specificationPath.equals("No file chosen")) {
            java.io.File file = new java.io.File(specificationPath);
            if (file.exists()) {
                long fileSizeInBytes = file.length();
                long fileSizeInKB = fileSizeInBytes / 1024;

                if (fileSizeInBytes == 0) {
                    // Partition 7: File Empty
                    // Note: Requirement says "File rỗng", normally this might be a warning or error
                    // depending on business rule.
                    // Let's assume valid but maybe log, or just pass?
                    // The table just says "File rỗng". Let's assume it's acceptable unless strictly
                    // forbidden.
                    // However, usually "upload" implies some content.
                    // But strictly following boundaries: 0 is lower boundary.
                    // Let's NOT throw exception for 0 byte file unless specified.
                    // actually, let's treat it as valid unless it breaks something.
                }

                if (fileSizeInKB > 1024) { // > 1MB
                    // Partition 9: File too large
                    throw new IllegalArgumentException("Job Specification file cannot exceed 1MB!");
                }
            }
        }
    }
}