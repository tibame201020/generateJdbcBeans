package org.tsri.batch;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Component
public class Main {
    public void execute() throws Exception {

        String properties = "dataSource.properties";
        String xml = "jdbc-beans.xml";
        String beansJava = "../java/org/dao/dataSourceConfig/JdbcBeans.java";
        String persistenceJava = "../java/org/dao/dataSourceConfig/Persistence.java";

        List<Map<String, String>> dataSources = propertiesToDsConfig(readFromInputStream(getIS(properties)));

        InputStream beansHeaderIs = Main.class.getResourceAsStream("/xml/jdbc-beans-header-template.xml");
        InputStream beansTemplateIs = Main.class.getResourceAsStream("/xml/jdbc-beans-template.xml");

        String beansHeader = readFromInputStream(beansHeaderIs);
        String beansTemplate = readFromInputStream(beansTemplateIs);

        String beansXml = generateBeansXml(beansHeader, beansTemplate, dataSources);
        String beans = generateBeansJava(dataSources);
        String persistence = generateBeansPersistence(dataSources);

        writeToFile(xml, beansXml);
        writeToFile(beansJava, beans);
        writeToFile(persistenceJava, persistence);

    }

    private String generateBeansPersistence(List<Map<String, String>> dataSources) throws Exception {
        String header = readFromInputStream(Main.class.getResourceAsStream("/persistence/PersistenceHeaderTemplate.java"));
        String template = readFromInputStream(Main.class.getResourceAsStream("/persistence/PersistenceTemplate.java"));

        String jdbcBeans = "";
        for (Map<String, String> dataSource : dataSources) {
            String jdbcTemplateBeanName = dataSource.get("username").split("\\.")[0] + "Jdbc";

            String toUpper = jdbcTemplateBeanName.substring(0, 1).toUpperCase() + jdbcTemplateBeanName.substring(1);
            String getMethod = "get" + toUpper;

            String enumName = dataSource.get("username").split("\\.")[0].toUpperCase();

            String jdbcBean = template
                    .replaceFirst("DATASOURCE_JDBC_ENUM_NAME", enumName)
                    .replaceFirst("getDataSourceJdbc", getMethod);

            jdbcBeans = jdbcBeans + jdbcBean;
        }
        jdbcBeans = jdbcBeans.substring(0, jdbcBeans.length() - 2) + ";";

        return header.replace("jdbcBeansReplaceHere", jdbcBeans);
    }

    private String generateBeansJava(List<Map<String, String>> dataSources) throws Exception {
        String header = readFromInputStream(Main.class.getResourceAsStream("/jdbcBeans/JdbcBeansHeaderTemplate.java"));
        String template = readFromInputStream(Main.class.getResourceAsStream("/jdbcBeans/JdbcBeansTemplate.java"));

        String jdbcBeans = "";

        for (Map<String, String> dataSource : dataSources) {
            String jdbcTemplateBeanName = dataSource.get("username").split("\\.")[0] + "Jdbc";

            String toUpper = jdbcTemplateBeanName.substring(0, 1).toUpperCase() + jdbcTemplateBeanName.substring(1);

            String setMethod = "set" + toUpper;
            String getMethod = "get" + toUpper;

            String jdbcBean =template
                    .replaceAll("dataSourceJdbcName", jdbcTemplateBeanName)
                    .replaceFirst("setDataSourceJdbc", setMethod)
                    .replaceFirst("getDataSourceJdbc", getMethod);

            jdbcBeans = jdbcBeans + jdbcBean;
        }

        return header.replace("jdbcBeansReplaceHere", jdbcBeans);
    }

    private void writeToFile(String xml, String beansXml) throws Exception {
        PrintWriter writer = new PrintWriter(xml, "UTF-8");
        writer.println(beansXml);
        writer.close();
    }

    private String generateBeansXml(String beansHeader, String beansTemplate, List<Map<String, String>> dataSources) {

        String jdbcBeans = "";

        for (Map<String, String> dataSource : dataSources) {
            String dataSourceName = dataSource.get("username").split("\\.")[0];
            String dataSourceUrl = dataSource.get("url");
            String dataSourceUserName = dataSource.get("username");
            String dataSourcePassword = dataSource.get("password");
            String dataSourceDriver = dataSource.get("driver");
            String jdbcTemplateBeanName = dataSourceName + "Jdbc";
            String hikariConfigBeanId = dataSourceName + "_Hikari";

            String jdbcBean = beansTemplate
                    .replaceAll("dataSourceName", dataSourceName)
                    .replaceFirst("dataSourceUrl", dataSourceUrl)
                    .replaceFirst("dataSourceUserName", dataSourceUserName)
                    .replaceFirst("dataSourcePassword", dataSourcePassword)
                    .replaceFirst("dataSourceDriver", dataSourceDriver)
                    .replaceFirst("jdbcTemplateBeanName", jdbcTemplateBeanName)
                    .replaceAll("hikariConfigBeanId", hikariConfigBeanId);

            jdbcBeans = jdbcBeans + "\n" + jdbcBean;
        }

        return beansHeader.replace("jdbcBeansReplaceHere", jdbcBeans);
    }

    private List<Map<String, String>> propertiesToDsConfig(String propertiesStr) {

        Map<String, String> map = new HashMap<>();
        for (String prop : propertiesStr.split("\n")) {
            if (StringUtils.isNotBlank(prop)) {
                String[] props = prop.replaceFirst("=", "135434787qsqw21e12@!@!s").split("135434787qsqw21e12@!@!s");
                map.put(props[0], props[0]);
            }
        }

        Set<String> prefixSet = getPreFixSet(map.keySet());

        List<Map<String, String>> dataSources = new ArrayList<>();
        String[] dsProps = new String[] { "username", "password", "url", "driver" };

        for (String prefix : prefixSet) {
            Map<String, String> dataSource = new LinkedHashMap<>();
            for (String dsProp : dsProps) {
                String key = prefix + "." + dsProp;
                String value = map.get(key);
                dataSource.put(dsProp, value);
            }
            dataSources.add(dataSource);
        }
        return dataSources;
    }


    private static Set<String> getPreFixSet(Set<String> resource) {
        Set<String> preFix = new HashSet<>();
        for (String key: resource) {
            key = key.split("\\.")[0];
            preFix.add(key);
        }

        return preFix;
    }


    private static InputStream getIS(String file) throws Exception {
        return Files.newInputStream(Paths.get(file));
    }

    private static String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        } finally {
            inputStream.close();
        }
        return resultStringBuilder.toString();
    }

}
