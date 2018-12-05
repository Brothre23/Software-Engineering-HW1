import java.io.IOException;
import java.sql.*;
import java.util.Enumeration;
import java.util.Hashtable;

public class SQLiteModel extends PhoneBookModel {
    public SQLiteModel(PhoneBookView view) throws IOException{
        phonebookview = view;
        phoneBook = new Hashtable();
        try{
            Connection database = DriverManager.getConnection("jdbc:sqlite:SQLdatabase.db");
            // database中不存在table的話則新增table
            DatabaseMetaData md = database.getMetaData();
            ResultSet rs = md.getTables(null, null, "PHONEBOOK", null);
            Statement stmt = database.createStatement();
            if (!rs.next()){
                stmt.executeUpdate("CREATE TABLE PHONEBOOK(NAME TEXT, PHONE TEXT);");
            }
            // 從table中取出資料
            rs = stmt.executeQuery("SELECT * FROM PHONEBOOK");
            while (rs.next()){
                String name = rs.getString("NAME");
                String phone = rs.getString("PHONE");
                phoneBook.put(name, phone);
            }
            rs.close();
            // stmt.close();
            database.close();
        } catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
    public void updateDB() throws Exception{
        Enumeration names = phoneBook.keys();
        String name, phone, sql;
        try{
            Connection database = DriverManager.getConnection("jdbc:sqlite:SQLdatabase.db");
            Statement stmt = database.createStatement();
            // 因為介面配合關係，TXT和SQL的model在更新時都是把檔案清空再重新寫入，
            // 而SQLite沒有TRUNCATE指令，所以使用DROP把整個table刪除再新增
            sql = "DROP TABLE PHONEBOOK";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE PHONEBOOK" +
                  "(NAME TEXT, PHONE TEXT);";
            stmt.executeUpdate(sql);
            // 從hash table中取出資料寫入資料庫
            while (names.hasMoreElements()){
                name = (String) names.nextElement();
                phone = phoneBook.get(name);
                name = "'" + name + "'";
                phone = "'" + phone + "'";
                sql = "INSERT INTO PHONEBOOK (NAME,PHONE)" + "VALUES (" + name + ", " + phone + ");";
                stmt.executeUpdate(sql);
            }
            stmt.close();
            database.close();
        } catch (Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
}
