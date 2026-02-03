package fploy.bai5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.After;
import org.junit.Test;

public class JobTitleTest {

    @After
    public void cleanup() {
        // delete any temp files left behind
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        for (File f : tmpDir.listFiles((d, name) -> name.startsWith("job-spec-") )) {
            f.delete();
        }
    }

    @Test
    public void testValidation() {
        JobTitle job = new JobTitle("Dev", "Desc", "", "");
        job.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTitle() {
        JobTitle job = new JobTitle("", "Desc", "", "");
        job.validate();
    }

    @Test
    public void testTitleMaxLengthAllowed() {
        String title = new String(new char[100]).replace('\0', 'A');
        JobTitle job = new JobTitle(title, "Desc", "", "");
        job.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTitleTooLong() {
        String title = new String(new char[101]).replace('\0', 'A');
        JobTitle job = new JobTitle(title, "Desc", "", "");
        job.validate();
    }

    @Test
    public void testDescriptionMaxLengthAllowed() {
        String desc = new String(new char[400]).replace('\0', 'D');
        JobTitle job = new JobTitle("Dev", desc, "", "");
        job.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDescriptionTooLong() {
        String desc = new String(new char[401]).replace('\0', 'D');
        JobTitle job = new JobTitle("Dev", desc, "", "");
        job.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoteTooLong() {
        String note = new String(new char[401]).replace('\0', 'N');
        JobTitle job = new JobTitle("Dev", "Desc", "", note);
        job.validate();
    }

    @Test
    public void testSpecificationEmptyFileAllowed() throws IOException {
        File tmp = File.createTempFile("job-spec-", ".tmp");
        // empty file (0 bytes)
        tmp.deleteOnExit();
        JobTitle job = new JobTitle("Dev", "Desc", tmp.getAbsolutePath(), "");
        job.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpecificationTooLarge() throws IOException {
        File tmp = File.createTempFile("job-spec-", ".tmp");
        tmp.deleteOnExit();
        // create a file slightly larger than 1,024 KB (need > 1024 KB to trigger)
        try (FileOutputStream fos = new FileOutputStream(tmp)) {
            byte[] block = new byte[1024];
            // write 1025 blocks = 1025 KB (> 1024)
            for (int i = 0; i < 1025; i++) fos.write(block);
        }
        JobTitle job = new JobTitle("Dev", "Desc", tmp.getAbsolutePath(), "");
        job.validate();
    }
}