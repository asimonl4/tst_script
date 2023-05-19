package com.arnaud.jasper.report.generation.save;

import java.io.BufferedReader;
//Imports java
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
			
			//Supprimer les vieux fichiers PDF (fichier avec nom d'utilisateur) ==> OBSOLETE (si méthode BDD conservée)
			/*
			File pdfFolder = new File("C:\\Program Files\\komoto\\L4Plugin\\Scripts\\java\\rapports_pdf");
	        FilenameFilter textFilefilter = new FilenameFilter(){
		          public boolean accept(File dir, String name) {
		             String lowercaseName = name.toLowerCase();
		             if (lowercaseName.endsWith("asimon_tmp.pdf")) {
		                return true;
		             } else {
		                return false;
		             }
		          }
		       };
		       
		       //List of all PDF files for current user
		       String filesList[] = pdfFolder.list(textFilefilter);
		       System.out.println("List of the text files in the specified directory:");
		       for(String fileName : filesList) {
		          System.out.println(fileName);
		          new File("C:\\Program Files\\komoto\\L4Plugin\\Scripts\\java\\rapports_pdf\\"+fileName).delete();
		       }
			*/
		       
			
			//Initialiser les chemins des fichiers jasper et des ressources	
			String jrxmlPath = "C:\\Program Files\\komoto\\L4Plugin\\Scripts\\java\\rapports_jrxml\\"+jrxml_name+".jrxml";//BL_simple.jrxml";
			String jrxmlCopyPath = "C:\\Program Files\\komoto\\L4Plugin\\Scripts\\java\\rapports_jrxml\\"+tmp_data_filename+".jrxml";//BL_simple.jrxml";
			String jasperPath = "C:\\Program Files\\komoto\\L4Plugin\\Scripts\\java\\rapports_jrxml\\"+tmp_data_filename+".jasper";//BL_simple.jasper";//_" //+now+".jasper";
			String jrprintPath = "C:\\Program Files\\komoto\\L4Plugin\\Scripts\\java\\rapports_jrxml\\"+tmp_data_filename+".jrprint";
			String pdfPath = "C:\\Program Files\\komoto\\L4Plugin\\Scripts\\java\\rapports_pdf\\"+tmp_data_filename+".pdf";//BL_simple.pdf";//_"//+ now+".pdf";
			String xlsPath = "C:\\Program Files\\komoto\\L4Plugin\\Scripts\\java\\rapports_pdf\\"+tmp_data_filename+".xls";//BL_simple.xls";//_"//+ now+".pdf";
			
			/////////////////////////////////////////////////////////////////
			//TEST Récupération des données depuis un fichier TXT temporaire
			//headers
			String[] tmp_txt_headers = data_headers.split("\\&");
			System.out.println("data headers = " + data_headers);
			System.out.println("header  = " + tmp_txt_headers.length);
			
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
	        	String[] splitted_str = current_line.split("&");
	        	for(int i = 0; i < splitted_str.length; i++) {
	        		splitted_str[i] = tmp_txt_headers[i] + "=" + splitted_str[i];
	        	}
	        	//Print string finale de génération de l'objet java custom
	        	System.out.println("final line = " + String.join("&", splitted_str));
	        	dataList.add(new DataBean(String.join("&", splitted_str)));
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
	        }
	        catch(Exception exx) {
	        	System.out.println("Exception occured : " + exx.getMessage());
	        }
	        
	        JRBeanCollectionDataSource beanColDataSource = new 
			         JRBeanCollectionDataSource(dataList);
			
	        //
	        //Copie du JRXML en le renommant avec le nom temporaire (évite la concurrence potentielle)
	        //FileUtils.copyFile(new File(jrxmlPath), new File(jrxmlCopyPath));
	        
	        ///////////////////////////////////////////////////////
	        // Chargement du JRXML pour intialiser les paramètres 
			//   1) Récupération des noms des paramètres utilisés dans le rapport
			//	 2) Assignation des paramètres si le paramètre existe dans les headers (argument index 1 main)
			//		(Si le paramètre existe dans les headers, récupération de sa valeur dans le 1er élément de la liste de données)
			
	        //Instancier le dictionnaire de paramètre et ajouter l'activité
	        Map parametersDict = new HashMap();
			JasperDesign jasperDesign = JRXmlLoader.load(jrxmlPath);//(jrxmlCopyPath);
			List<JRParameter> parameter_list = jasperDesign.getParametersList();
			for(int i = 0; i < parameter_list.size(); i++) {
				String current_parameter_name = parameter_list.get(i).getName();
				//Si le parametre existe dans les headers, intialiser la hashmap avec la valeur du paramètre sur le 1er élément de la datalist
				int index_of_parameter = Arrays.asList(tmp_txt_headers).indexOf(current_parameter_name);
				if(index_of_parameter != -1) {
					parametersDict.put(current_parameter_name, dataList.stream().findFirst().get().dynamicalGetter(current_parameter_name));
				}
				System.out.println("parameter i = " + i + " = " + parameter_list.get(i).getName());
			}
			//Ajout de parametres custom, exemple : la source de donnes
			//parametersDict.put("subconnection", connection);

			//
			//récupération de la date actuelle pour le nommage des fichiers (si nécessaire)
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_hhmmss");
			String now = dtf.format(LocalDateTime.now());
			
			//compilation du rapport pour générer un .jasper
			try {
				JasperReport current_jasper_report = JasperCompileManager.compileReport(jrxmlPath);//(jrxmlCopyPath);			      
				
				//Compilation du sous-rapport
				//JasperCompileManager.compileReportToFile(subJrxmlPath, subJasperPath);
				
				//remplir le rapport avant export
			    JasperPrint current_jasper_print = JasperFillManager.fillReport(current_jasper_report,
			    		parametersDict, beanColDataSource);//connection); //beanColDataSource);
			      
			    //exporter en PDF
			    //JasperExportManager.exportReportToPdfFile(current_jasper_print, pdfPath);
			    
			    System.out.println("Archivage value = " + archivage);
			    //stock du rapport PDF en BDD ("archivage" indiquera si le fichier sera supprimé à terme ou archivé)
		    	//récupération des bytes du PDF
		    	byte[] PDF_bytes = JasperExportManager.exportReportToPdf(current_jasper_print);
		    	
		    	//récup des bytes du fichier PDF (pour voir s'il y a une différence)
		    	//Path pdf_file_path = Paths.get(pdfPath);
		    	//byte[] PDF_file_bytes = Files.readAllBytes(pdf_file_path);
		    	
		    	//stock en base de données
		    	//connexion à la BDD
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				String url = "jdbc:sqlserver://;serverName=SRVSQL-01-TST;port=50090;databaseName=INFOCENTRERECET01";
				Connection connection = DriverManager.getConnection(url, "Cpt_reporting_test", "Qu8p*!34");
		    	//Récupération de la date d'archivage
				//DateTimeFormatter insert_dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS");
				//String bdd_now = insert_dtf.format(LocalDateTime.now());
				
			    Instant right_now = Instant.now();
			    // convert Instant to ZonedDateTime
			    DateTimeFormatter insert_dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");//("uuuu/MM/dd HH:mm:ss");
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
			    
			    /*
			    //export en XLS
	            JRXlsExporter exporter = new JRXlsExporter();
	            exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME,
	               printFileName);
	            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
	               xlsPath);
	            exporter.exportReport();
	            System.out.println("export XLS terminé");
	            */
			    
		        //Une fois que toutes les manipulations ont été effectuées, supprimer les fichiers temporaires
			    // (sauf le PDF qui sera chargé par le plugin Ketra et supprimé)
		        try {
		        	System.out.println("Try to delete temporary files");
		        	//Suppressions
		        	//boolean first_delete = new File(jrxmlCopyPath).delete();
		        	//new File(jasperPath).delete();
		        	//new File(jrprintPath).delete();
		        	//new File(pdfPath).delete();
		        	//new File(xlsPath).delete();
		        	//System.out.println("Files should be deleted ?, first delete = " + first_delete);
		        }catch (SecurityException e) {
		            System.out.println(
		                "No such file/directory exists, "+e.getMessage());
		        }
		        catch(Exception exx) {
		        	System.out.println("Exception occured : " + exx.getMessage());
		        }
			}catch(Exception e) {
				System.out.println("Exception lors de l export PDF : " + e.getMessage());
				e.printStackTrace();
			}
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
}
