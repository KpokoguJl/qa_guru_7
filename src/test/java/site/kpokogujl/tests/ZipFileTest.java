package site.kpokogujl.tests;

import com.codeborne.pdftest.PDF;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class ZipFileTest {

    private ClassLoader cl = ZipFileTest.class.getClassLoader();

    @Test
    void zipTest() throws Exception {

        try (ZipFile zf = new ZipFile(new File(cl.getResource("ZIPArchive.zip").toURI()));) {
            ZipEntry zipEntryPdf = zf.getEntry("PDFFile.pdf");
            InputStream streamPdf = zf.getInputStream(zipEntryPdf);
            PDF parsedPdf = new PDF(streamPdf);

            assert parsedPdf.text.contains("Пример PDF формы");
            assert parsedPdf.creator.equals("Writer");

            ZipEntry zipEntryCsv = zf.getEntry("CSVFile.csv");
            InputStream streamCsv = zf.getInputStream(zipEntryCsv);
            CSVReader parsedCsv = new CSVReader(new InputStreamReader(streamCsv));
            List<String[]> list = parsedCsv.readAll();
            assert list.contains(new String[] {"First", "1"});
            assert list.contains(new String[] {"Second", "2"});

            System.out.println("");
        }



    }
}
