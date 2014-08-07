
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ExcelReader {

    private HSSFWorkbook wb;

    public ExcelReader() {
    }

    public void loadFile(String excelPath) {
        try {
            File excelFile = new File(excelPath);
            InputStream myxls = new FileInputStream(excelFile);
            wb = new HSSFWorkbook(myxls);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load excel file - "
                    + excelPath, e);
        }
    }

    public List<List<Double>> readData() {
        List<List<Double>> data = new ArrayList<List<Double>>();

        HSSFSheet sheet = wb.getSheetAt(0);
        Iterator<Row> rowIt = sheet.iterator();
        if (rowIt.hasNext()) {
            rowIt.next();	//skip caption row
        }
        while (rowIt.hasNext()) {
            Row row = rowIt.next();

            List<Double> curRow = new ArrayList<Double>();
            for (Cell cell : row) {

                curRow.add(cell.getNumericCellValue());

            }
            data.add(curRow);
        }

        return data;
    }

    public List<String> readCaptionRow() {
        List<String> captions = new ArrayList<String>();
        HSSFSheet sheet = wb.getSheetAt(0);
        Iterator<Row> rowIt = sheet.iterator();
        Row row = rowIt.next();
        for (Cell cell : row) {
            captions.add(cell.getStringCellValue());
        }
        return captions;
    }
    
    
}