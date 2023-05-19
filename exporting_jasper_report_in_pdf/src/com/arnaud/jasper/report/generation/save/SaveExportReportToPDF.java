package com.arnaud.jasper.report.generation.save;

public class SaveExportReportToPDF {
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
	
	//Vieux chemins pour gestion de l'impression d'un rapport jasper à l'aide de fichiers temporaires
	/*
	String jrxmlCopyPath = "C:\\Program Files\\komoto\\L4Plugin\\Scripts\\java\\rapports_jrxml\\"+tmp_data_filename+".jrxml";//BL_simple.jrxml";
	String jasperPath = "C:\\Program Files\\komoto\\L4Plugin\\Scripts\\java\\rapports_jrxml\\"+tmp_data_filename+".jasper";//BL_simple.jasper";//_" //+now+".jasper";
	String jrprintPath = "C:\\Program Files\\komoto\\L4Plugin\\Scripts\\java\\rapports_jrxml\\"+tmp_data_filename+".jrprint";
	String pdfPath = "C:\\Program Files\\komoto\\L4Plugin\\Scripts\\java\\rapports_pdf\\"+tmp_data_filename+".pdf";//BL_simple.pdf";//_"//+ now+".pdf";
	String xlsPath = "C:\\Program Files\\komoto\\L4Plugin\\Scripts\\java\\rapports_pdf\\"+tmp_data_filename+".xls";//BL_simple.xls";//_"//+ now+".pdf";
	*/
	
}
