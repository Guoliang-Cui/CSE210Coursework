package util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Connect and parse Excel file.
 */
public class ImportExcelUtil {

    private final static String excel2003L = ".xls"; // 2003- excel
    private final static String excel2007U = ".xlsx"; // 2007+ excel

    /**
     * Convert the file stream into List(Map(String, Object))
     * @param in the file stream that will be accepted
     * @param fileName the name of file that will be accepted
     * @return List(Map(String, Object))  parse the excel to get the List(Map(String, Object))
     * @throws [Exception] [IOException]
     */
    public static List<Map<String, Object>> parseExcel(InputStream in, String fileName) throws Exception {

        Workbook work = getWorkbook(in, fileName);
        if (work == null) {
            throw new Exception("The Excel Wookbook is null！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();



        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null)
                continue;
            row = sheet.getRow(0);
            String useDTitle1 = null;
            List<String> title = new ArrayList<>();
            List<Integer> titleNumber = new ArrayList<>();
            if (row != null) {
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    useDTitle1 = (String) getCellValue(cell);

                    if (useDTitle1.equals("University") || useDTitle1.equals("Department") || useDTitle1.equals("User")
                            || useDTitle1.equals("Topics") || useDTitle1.equals("Skills")) {
                        title.add((String) getCellValue(cell));
                        titleNumber.add(y);
                    }else{

                    }
                }


            } else
                continue;


            for(int i1 = sheet.getFirstRowNum()+1; i1 <= sheet.getLastRowNum(); i1++){

                Map<String, Object> m = new HashMap<String, Object>();
                row = sheet.getRow(i1);

                for(int j = 0; j < titleNumber.size(); j ++){
                    cell = row.getCell(titleNumber.get(j));
                    String key = title.get(j);

                    m.put(key,getCellValue(cell));

                }
                ls.add(m);

            }




        }
        work.close();
        return ls;
    }

    /**
     * Decide the excel version according to file extension
     * @param inStr the input stream of excel file
     * @param fileName the name of excel file
     * @return Workbook type
     * @throws   [java.lang.Exception] [java.lang.Exception]
     */
    public static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (excel2003L.equals(fileType)) {
            wb = new HSSFWorkbook(inStr); // 2003-
        } else if (excel2007U.equals(fileType)) {
            wb = new XSSFWorkbook(inStr); // 2007+
        } else {
            throw new Exception("The format is wrong！");
        }
        return wb;
    }

    /**
     * formatting the data in every cell in excel file
     * @param cell every cell in excel file
     * @return the Object of cell
     */
    public static Object getCellValue(Cell cell) {
        Object value = null;
        DecimalFormat df = new DecimalFormat("0"); // formatting number String
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd"); //formatting Date
        DecimalFormat df2 = new DecimalFormat("0"); // formatting number

        switch (cell.getCellTypeEnum()) {
            case STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    value = df.format(cell.getNumericCellValue());
                } else if ("m/d/yy".equals(cell.getCellStyle().getDataFormatString())) {
                    value = sdf.format(cell.getDateCellValue());
                } else {
                    value = df2.format(cell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }

    /**
     * get the specificColumn of the excel file
     * @param ls the List(Map(String, Object)) by parsing excel file
     * @param specificLine specify the needed line that will be extracted
     * @return the List(Object) of the specificColumn
     */
    public static List<Object> getSpecificColumn(List<Map<String, Object>> ls, String specificLine){
        List<Object> allObject = new ArrayList<>();
        //ge all title
        Set<String> titleSet = ls.get(0).keySet();
        //for the first title （key） traverse and get all value below this title
        for(String s1 : titleSet){
            if(s1.equals(specificLine)){
                for (int i = 0; i < ls.size(); i++){
                    Object value = ls.get(i).get(specificLine);
                    allObject.add(value);
                }
            }
        }
        return allObject;
    }

    //split a object list to a String list

    /**
     * Split a object list to a String list
     * by ", "
     * @param list the list of Object
     * @return List(String) the list of String
     */
    public static List<String> splitByColumn(List <Object> list){
        List<String> allList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String a = (String) list.get(i);
            if (a == "") {

            } else if (a.contains(",")) {
                String[] s = a.split(", ");
                for (String s1 : s) {
                    allList.add(s1);
                }
            } else {
                allList.add(a);
            }
        }
        return allList;

    }

    /**
     * Split a String to String list
     * by ", "
     * @param s the String that be split
     * @return the List(String) store single String between comma
     */
    public static List<String> splitSByComma(String s){
        List<String> result = new ArrayList<>();
        if (s == ""){
            return result;

        }else if(s.contains(",")){
            String[] s1 = s.split(", ");
            for (String s2:s1){
                result.add(s2);
            }
        }else{
            result.add(s);
        }
        return result;

    }

}

