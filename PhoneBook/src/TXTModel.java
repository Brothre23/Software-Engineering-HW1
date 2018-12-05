import java.util.Hashtable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;


public class TXTModel extends PhoneBookModel{
    public TXTModel(PhoneBookView view) throws IOException{
        phonebookview = view;
        phoneBook = new Hashtable();
        String line;
        String[] tempArray;
        try{
            File file = new File("./TXTdatabase.txt");
            // 檔案不存在則新增
            if (!file.exists()){
                file.createNewFile();
            }
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            // 從檔案中一次讀一行
            while ((line = br.readLine()) != null) {
                // 用空白切割字串
                tempArray = line.split(" ");
                phoneBook.put(tempArray[0], tempArray[1]);
            }
            fr.close();
        } catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    public void updateDB() throws Exception{
        Enumeration names = phoneBook.keys();
        String str;
        try{
            // FileWriter寫入時預設就會覆蓋原本的內容
            FileWriter fw = new FileWriter("./TXTdatabase.txt");
            // 從hash table中取出資料寫入資料庫
            while(names.hasMoreElements()) {
                str = (String) names.nextElement();
                fw.write(str + " " + phoneBook.get(str) + "\r\n");
            }
            fw.close();
        } catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
}
