package com.gitegg.code.generator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sound.sampled.DataLine;

public class Test {

    private static Connection conn;

    public static void main(String[] args) {
        DBinit();
        try {
            //获取数据库的元数据
            DatabaseMetaData dbMetaData = conn.getMetaData();
            //从元数据中获取到所有的表名
            ResultSet rs = dbMetaData.getTables("gitegg_cloud", null, null,new String[] { "TABLE" });
            //存放所有表名
            List<String> tableNames = new ArrayList<>();
            //存放当前表的字段
            List<String>  fields= new ArrayList<>();
            //存放当前表的字段类型
            List<String> fieldstype = new ArrayList<>();
            while(rs.next())
            {
                System.out.println("表名: "+rs.getString("TABLE_NAME"));
                System.out.println("表类型: "+rs.getString("TABLE_TYPE"));
                System.out.println("表所属数据库: "+rs.getString("TABLE_CAT"));
                System.out.println("表所属用户名: "+rs.getString("TABLE_SCHEM"));
                System.out.println("表备注: "+rs.getString("REMARKS"));
                tableNames.add(rs.getString("TABLE_NAME"));
            }
            //查询每个表的字段
            for(String record:tableNames)
            {
//                String sql = "select * from " + record;
                String sql = "show full columns from " + record;
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rstable = ps.executeQuery();

                while (rstable.next()) {
                    System.out.println(rstable.getString("Field"));
                    System.out.println(rstable.getString("Type"));
                    System.out.println(rstable.getString("NULL"));
                    System.out.println(rstable.getString("KEY"));
                    System.out.println(rstable.getString("Default"));
                    System.out.println(rstable.getString("Extra"));
                    System.out.println(rstable.getString("Comment"));
                }

                //结果集元数据
                ResultSetMetaData meta = rstable.getMetaData();
                //表列数量
                int columeCount=meta.getColumnCount();
                for (int i=1;i<=columeCount;i++)
                {
                    fields.add(meta.getColumnName(i));
                    fieldstype.add(meta.getColumnTypeName(i));
                    System.out.println(meta.getCatalogName(i));
                    System.out.println(meta.getColumnClassName(i));
                    System.out.println(meta.getColumnCount());
                    System.out.println(meta.getColumnDisplaySize(i));
                    System.out.println(meta.getColumnLabel(i));
                    System.out.println(meta.getColumnName(i));
                    System.out.println(meta.getColumnType(i));
                    System.out.println(meta.getColumnTypeName(i));
                    System.out.println(meta.getPrecision(i));
                    System.out.println(meta.getScale(i));
                    System.out.println(meta.getSchemaName(i));
                    System.out.println(meta.getTableName(i));
                }
                System.out.println("表"+record+"字段: "+fields);
                System.out.println("表"+record+"字段类型: "+fieldstype);
                fields.clear();
            }
        } catch (SQLException e) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        DBclose();
    }

    public static void DBinit(){

        //基础配置
//        String url = "jdbc:mysql://172.16.20.188/gitegg_cloud?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf8&all owMultiQueries=true&serverTimezone=Asia/Shanghai";
//        String username = "myHisc";
//        String password = "root4Hisc";


        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        //MySQL配置时的用户名
        String user ="myHisc";
        //MySQL配置时的密码
        String password = "root4Hisc";
        //不同端口号
        String[] db_url_port = {"32080", "32081", "32082","3308","3306"};
        //数据库服务器
        String dbServer="";
//        for(String db_port:db_url_port)
//        {
//            dbServer="ip:"+db_port;
            //URL指向要访问的数据库名mydata
            String url = "jdbc:mysql://172.16.20.188/gitegg_cloud";
            try {
                Class.forName("com.mysql.jdbc.Driver");
                //声明Connection对象
                conn = DriverManager.getConnection(url,user,password);
                conn.setCatalog("gitegg_cloud");//没作用
                if(!conn.isClosed()){
                    System.out.println("当前访问数据库端，数据库连接成功！");
//                    break;
                }
            } catch (Exception e) {
                System.out.println("当前访问数据库端口号为，数据库连接失败！");
//                continue;
            }
//        }
    }

    public static void DBclose(){
        try {
            conn.close();
        } catch (SQLException e) {
            //TODO Auto-generated catch block
            System.out.println("数据关闭异常");
            e.printStackTrace();
        }
    }

}
