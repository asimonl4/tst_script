package com.arnaud.jasper.report.generation;
//Imports java
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

//Manipulation Bean
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;

import com.arnaud.jasper.report.generation.DataBean;

//Jasper
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;

public class ExportReportToPDF {
	public static void main(String[] args) {		
		try {
			
			//Test : Utiliser une ou plusieurs connexion SQL pour alimenter le rapport ==> OK		
			/*
			Connection connection_report_1 = SQLConnect("INFOCENTRERECET01");
			Connection connection_report_2 = SQLConnect("TransportFac01");
			*/
			
			//récupération des paramètres et des données du rapport jasper 
			// via les arguments de la fonction main (récupérés au moment de l'exécution du jar)
			String jrxml_name = null;
			String data_headers = null;
			String tmp_data_filename = null;
			String username = null;
			String activite = null;
			Boolean archivage = false;
			if(args.length > 5) {
				jrxml_name = args[0];
				data_headers = args[1];
				//supprimer l'extension .txt
				tmp_data_filename = args[2].substring(0, args[2].length()-4);
				System.out.println("tmp data filename = "+tmp_data_filename);
				username = args[3];
				activite = args[4];
				if(args[5].equals("True")) {
					archivage = true;
				};
			}
			
			//Initialiser les chemins des fichiers jasper et des ressources	
			String jrxmlPath = "C:\\Program Files\\komoto\\L4Plugin\\Scripts\\java\\rapports_jrxml\\"+jrxml_name+".jrxml";//BL_simple.jrxml";
			
			//Récupération des données depuis un fichier TXT temporaire
			Collection<DataBean> dataList = GetJasperDataFromTempFile(data_headers, tmp_data_filename);
	        JRBeanCollectionDataSource beanColDataSource = new 
			         JRBeanCollectionDataSource(dataList);
			
	        //Test avec utilisation de fichiers temporaires => 
	        // copie du JRXML en le renommant avec le nom temporaire (évite la concurrence potentielle)
	        //FileUtils.copyFile(new File(jrxmlPath), new File(jrxmlCopyPath));
			
	        //Instancier les paramètres du rapport jasper
	        Map parametersDict = GetJasperParameters(data_headers, jrxmlPath, dataList);
			//Ajout de parametres custom, exemple : la source de donnes
			//parametersDict.put("subconnection", connection);
			
	        //Génération du PDF jasper (byte[])
	        byte[] PDF_bytes = GetJasperPDF(jrxmlPath, parametersDict, beanColDataSource);
	        
	        //Stock du rapport en BDD (archivage ou stockage temporaire)
	        StoreReportInDB(jrxml_name, username, activite, PDF_bytes, archivage);
	     
		}catch(Exception exce) {
			System.out.println("Exception globale : " + exce.getMessage());
			exce.printStackTrace();
		}
	}
	
	//Fonction pour retourner une connexion SQL
	private static Connection SQLConnect(String db_name) {
		System.out.println("try to connect to sql");
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String url = "jdbc:sqlserver://;serverName=SRVSQL-01-TST;port=50090;databaseName="+db_name;
			Connection connection = DriverManager.getConnection(url, "Cpt_reporting_test", "Qu8p*!34");
			System.out.println("connected to SQL");
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Can\'t connect to SQL, error : " + e.getMessage());
			return null;
		}
	}
	
	//Fonction pour récupérer le contenu du texte temporaire dans une collection d'objet java (+ suppression du fichier temporaire)
	private static Collection<DataBean> GetJasperDataFromTempFile(String data_headers, String tmp_data_filename) {
		System.out.println("Get jasper data from file function");
		try {
			//Récupération des données depuis un fichier TXT temporaire
			//Entêtes
			String[] tmp_txt_headers = data_headers.split("\\;");
			System.out.println("data headers = " + data_headers);
			System.out.println("Txt headers number = " + tmp_txt_headers.length);
			
			//Lire le contenu du texte temporaire et le stocker dans une collection d'objets java
			Collection<DataBean> dataList = new ArrayList<DataBean>();
			File file = new File(
	            "C:\\Program Files\\komoto\\L4Plugin\\Scripts\\java\\data\\"+tmp_data_filename+".txt");
	        Path file_path = Paths.get("C:\\Program Files\\komoto\\L4Plugin\\Scripts\\java\\data\\"+tmp_data_filename+".txt");
	        // Création d'un buffer reader pour lire les données dans le fichier temporaire
	        BufferedReader br = Files.newBufferedReader(file_path);

	        String current_line;
	        while ((current_line = br.readLine()) != null) {
	            // Affichage test
	            System.out.println("current line = " + current_line);
	     
	        	//Itérer sur les valeurs de la ligne, construire la string "finale" utilisée pour construire un DataBean
	        	// en concaténant les headers aux valeurs
	        	String[] splitted_str = current_line.split(";");
	        	for(int i = 0; i < splitted_str.length; i++) {
	        		splitted_str[i] = tmp_txt_headers[i] + "=" + splitted_str[i];
	        	}
	        	//Print string finale de génération de l'objet java custom
	        	System.out.println("final line = " + String.join(";", splitted_str));
	        	DataBean new_object = new DataBean(String.join(";", splitted_str));
	        	dataList.add(new_object);
	        	
	        	System.out.println("new Databean Livraison ligne 1 => " + new_object.getLivraisonLigneAdresse1());
	        }
	        
	        //Une fois la récupération effectuée, supprimer le fichier temporaire
	        try {
	        	System.out.println("Try to delete file");
	        	br.close();
	        	boolean file_del = file.delete();
	        	System.out.println("File should be deleted ? ==> " + file_del);
	        }catch (SecurityException e) {
	            System.out.println(
	                "No such file/directory exists, "+e.getMessage());
	            return null;
	        }
	        catch(Exception exx) {
	        	System.out.println("Exception occured : " + exx.getMessage());
	        	return null;
	        }
	        
	        
	        return dataList;
		}catch(Exception ex) {
			System.out.println("Exception lors de la récupération des données dans le fichier temporaire : " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}
	
	//Fonction pour récupérer les paramètres du rapport jasper dans les données du fichier temporaire
	private static Map GetJasperParameters(String data_headers, String jrxmlPath, Collection<DataBean> dataList) {
        System.out.println("Récupération des paramètres du rapport jasper");
		try {
			//Instancier le dictionnaire de paramètre et ajouter l'activité
			String[] tmp_txt_headers = data_headers.split("\\;");
			Map parametersDict = new HashMap();
			//Récupération des paramètres depuis le JRXML
			JasperDesign jasperDesign = JRXmlLoader.load(jrxmlPath);//(jrxmlCopyPath);
			List<JRParameter> parameter_list = jasperDesign.getParametersList();
			for(int i = 0; i < parameter_list.size(); i++) {
				String current_parameter_name = parameter_list.get(i).getName();
				//Si le parametre existe dans les headers, intialiser la hashmap avec la valeur du paramètre sur le 1er élément de la datalist
				int index_of_parameter = Arrays.asList(tmp_txt_headers).indexOf(current_parameter_name);
				if(index_of_parameter != -1) {
					parametersDict.put(current_parameter_name, dataList.stream().findFirst().get().dynamicalGetter(current_parameter_name));
					System.out.println("value entered for "+ current_parameter_name +" = " + dataList.stream().findFirst().get().dynamicalGetter(current_parameter_name));
				}
				System.out.println("parameter i = " + i + " = " + parameter_list.get(i).getName());
			}
			return parametersDict;
        }catch(Exception ex) {
        	System.out.println("Exception lors de la récupération des paramètres du rapport jasper : " + ex.getMessage());
        	ex.printStackTrace();
        	return null;
        }
	}
	
	//Fonction qui permet de récupérer le rapport jasper au format PDF (en bytes)
	private static byte[] GetJasperPDF(String jrxmlPath, Map parametersDict, JRBeanCollectionDataSource beanColDataSource) {
		System.out.println("Get Jasper ,PDF in byte[]");
		try {
			JasperReport current_jasper_report = JasperCompileManager.compileReport(jrxmlPath);		      
			//Compilation du sous-rapport
			//JasperCompileManager.compileReportToFile(subJrxmlPath, subJasperPath);
			
			//remplir le rapport avant export
		    JasperPrint current_jasper_print = JasperFillManager.fillReport(current_jasper_report,
		    		parametersDict, beanColDataSource);//connection); //beanColDataSource);
		    //exporter en PDF
		    //JasperExportManager.exportReportToPdfFile(current_jasper_print, pdfPath);
		    
		    /*
		    //NOTE : export XLS
            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME,
               printFileName);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
               xlsPath);
            exporter.exportReport();
            */
		    
	    	//récupération des bytes du PDF (pour le stocker en BDD)
	    	byte[] PDF_bytes = JasperExportManager.exportReportToPdf(current_jasper_print);
	    	return PDF_bytes;
		}catch(Exception ex) {
			System.out.println("Exception lors de la génération du PDF jasper : " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}
	
	//Fonction pour stocker le rapport jasper (byte[]) en BDD
	private static boolean StoreReportInDB(String jrxml_name, String username, String activite, byte[] PDF_bytes, boolean archivage) {
		System.out.println("Stockage du rapport jasper PDF en BDD");
    	try {
    		//connexion à la BDD
    		Connection connection = SQLConnect("INFOCENTRERECET01");
    		
    		//récupération de la date d'archivage
    	    Instant right_now = Instant.now();
    	    DateTimeFormatter insert_dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    	    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(right_now, ZoneId.systemDefault());
    	    String bdd_now = insert_dtf.format(zonedDateTime);
    	    System.out.println(bdd_now);
    		
    		//Insertion
    		String query = "INSERT INTO _TestArchiveJasperKetra VALUES (?, ?, ?, ?, ?, ?)";
    		String converted_date = "CONVERT(datetime, '" + bdd_now + "', 121)";
    		System.out.println("try to connect to sql TST, bdd_now = " + bdd_now);
    		System.out.println("CONVERT val = " + converted_date);
    		
    		PreparedStatement statement = connection.prepareStatement(query);
    		statement.setString(1, jrxml_name);
    		statement.setString(2, username);
    		statement.setString(3, activite);
    		statement.setTimestamp(4, java.sql.Timestamp.valueOf(bdd_now));
    		statement.setBytes(5, PDF_bytes);
    		statement.setBoolean(6, archivage);
    		statement.executeUpdate();
    		
    		System.out.println("Archivage OK");
    		
    		return true;
    	}catch(Exception ex) {
    		System.out.println("Exception lors du stockage du rapport jasper en BDD : " + ex.getMessage());
    		ex.printStackTrace();
    		return false;
    	}
	}
}
