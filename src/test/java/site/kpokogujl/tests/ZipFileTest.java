package site.kpokogujl.tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;


public class ZipFileTest {

    private final ClassLoader cl = ZipFileTest.class.getClassLoader();

    @Test
    void zipTest() throws Exception {

        try (ZipFile zf = new ZipFile(new File(Objects.requireNonNull(cl.getResource("ZIPArchive.zip")).toURI()))) {
            ZipEntry zipEntryPdf = zf.getEntry("PDFFile.pdf");
            try (InputStream streamPdf = zf.getInputStream(zipEntryPdf)) {
                PDF parsedPdf = new PDF(streamPdf);

                assertThat(parsedPdf.text).contains("Пример PDF формы");
                assertThat(parsedPdf.creator).isEqualTo("Writer");
            }

            ZipEntry zipEntryCsv = zf.getEntry("CSVFile.csv");
            try (InputStream streamCsv = zf.getInputStream(zipEntryCsv)) {
                CSVReader parsedCsv = new CSVReader(new InputStreamReader(streamCsv));
                List<String[]> list = parsedCsv.readAll();

                assertThat(list)
                        .hasSize(2)
                        .contains(
                                new String[]{"First", "1"},
                                new String[]{"Second", "2"}
                        );
            }

            ZipEntry zipEntryXlsx = zf.getEntry("XLSXFile.xlsx");
            try (InputStream streamXlsx = zf.getInputStream(zipEntryXlsx)){
            XLS parsedXlsx = new XLS(streamXlsx);

            assertThat(parsedXlsx.excel.getSheetAt(0).getRow(0).getCell(0).getStringCellValue())
                    .isEqualTo("a1");
            assertThat(parsedXlsx.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue())
                    .isEqualTo("b2");
            }
        }
    }
}
