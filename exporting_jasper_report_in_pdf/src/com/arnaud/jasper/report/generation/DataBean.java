package com.arnaud.jasper.report.generation;

public class DataBean {
   private String LivraisonNomPrenom;
   private String LIVADR1;
   private String LivraisonLigneAdresse1;
   private String LivraisonLigneAdresse2;
   private String LivraisonLigneAdresse3;
   private String LivraisonCPVille;
   private String LIBPAYS;
   private String LIVPAYS;
   private String LivraisonPays;
   private String CABUDP;
   private String IDORDER;
   private String noCde;
   private String TRPCONT;
   private String transporteur;
   private String DATECDE;
   private String dateExpedition;
   private String datevalidation;
   private String articleReference;
   private String articleDesignation;
   private String articleQteLivree;
   private String articleQteCommandee;
   private String COLORIS;
   private String TAILLE1;
   private String IDCANAL;
   private String TYPECDE;
   private String IDCLIEN;
   
   public DataBean(String constructor_params) {
	 try {
		 // affecter les attributs selon les labels
		 String[] column = constructor_params.split(";");
		 for(int i = 0; i < column.length; i++) {
			 String label = column[i].split("=")[0];
			 String value = column[i].split("=")[1];
			 switch(label){
			 	case "LivraisonNomPrenom":
			 		this.setLivraisonNomPrenom(value);
			 		break;
			 	case "LIVADR1":
			 		this.setLIVADR1(value);
			 		break;
			 	case "LivraisonLigneAdresse1":
			 		this.setLivraisonLigneAdresse1(value);
			 		break;
			 	case "LivraisonLigneAdresse2":
			 		this.setLivraisonLigneAdresse2(value);
			 		break;
			 	case "LivraisonLigneAdresse3":
			 		this.setLivraisonLigneAdresse3(value);
			 		break;
			 	case "LivraisonCPVille":
			 		this.setLivraisonCPVille(value);
			 		break;
			 	case "LIBPAYS":
			 		this.setLIBPAYS(value);
			 		break;
			 	case "LIVPAYS":
			 		this.setLIVPAYS(value);
			 		break;
			 	case "LivraisonPays":
			 		this.setLivraisonPays(value);
			 		break;
			 	case "CABUDP":
			 		this.setCABUDP(value);
			 		break;
			 	case "IDORDER":
			 		this.setIDORDER(value);
			 		break;
			 	case "noCde":
			 		this.setNoCde(value);
			 		break;
			 	case "TRPCONT":
			 		this.setTRPCONT(value);
			 		break;
			 	case "transporteur":
			 		this.setTransporteur(value);
			 		break;
			 	case "DATECDE":
			 		this.setDATECDE(value);
			 		break;
			 	case "dateExpedition":
			 		this.setDateExpedition(value);
			 		break;
			 	case "datevalidation":
			 		this.setDatevalidation(value);
			 		break;
			 	case "articleReference":
			 		this.setArticleReference(value);
			 		break;
			 	case "articleDesignation":
			 		this.setArticleDesignation(value);
			 		break;
			 	case "articleQteLivree":
			 		this.setArticleQteLivree(value);
			 		break;
			 	case "articleQteCommandee":
			 		this.setArticleQteCommandee(value);
			 		break;
			 	case "COLORIS":
			 		this.setCOLORIS(value);
			 		break;
			 	case "TAILLE1":
			 		this.setTAILLE1(value);
			 		break;
			 	case "IDCANAL":
			 		this.setIDCANAL(value);
			 		break;
			 	case "TYPECDE":
			 		this.setTYPECDE(value);
			 		break;
			 	case "IDCLIEN":
			 		this.setIDCLIEN(value);
			 		break;
			 	default:
			 		break;
			 }
		 }
	 }catch(Exception e) {
		 e.printStackTrace();
	 }
   }

	public String dynamicalGetter(String value) {
		switch(value){
		 	case "LivraisonNomPrenom":
		 		return this.LivraisonNomPrenom;
		 	case "LIVADR1":
		 		return this.LIVADR1;
		 	case "LivraisonLigneAdresse1":
		 		return this.LivraisonLigneAdresse1;
		 	case "LivraisonLigneAdresse2":
		 		return this.LivraisonLigneAdresse2;
		 	case "LivraisonLigneAdresse3":
		 		return this.LivraisonLigneAdresse3;
		 	case "LivraisonCPVille":
		 		return this.LivraisonCPVille;
		 	case "LIBPAYS":
		 		return this.LIBPAYS;
		 	case "LIVPAYS":
		 		return this.LIVPAYS;
		 	case "LivraisonPays":
		 		return this.LivraisonPays;
		 	case "CABUDP":
		 		return this.CABUDP;
		 	case "IDORDER":
		 		return this.IDORDER;
		 	case "noCde":
		 		return this.noCde;
		 	case "TRPCONT":
		 		return this.TRPCONT;
		 	case "transporteur":
		 		return this.transporteur;
		 	case "DATECDE":
		 		return this.DATECDE;
		 	case "dateExpedition":
		 		return this.dateExpedition;
		 	case "datevalidation":
		 		return this.datevalidation;
		 	case "articleReference":
		 		return this.articleReference;
		 	case "articleDesignation":
		 		return this.articleDesignation;
		 	case "articleQteLivree":
		 		return this.articleQteLivree;
		 	case "articleQteCommandee":
		 		return this.articleQteCommandee;
		 	case "COLORIS":
		 		return this.COLORIS;
		 	case "TAILLE1":
		 		return this.TAILLE1;
		 	case "IDCANAL":
		 		return this.IDCANAL;
		 	case "TYPECDE":
		 		return this.TYPECDE;
		 	case "IDCLIEN":
		 		return this.IDCLIEN;
		 	default:
		 		return "ERREUR";
		}
	}
   

	public String getLivraisonNomPrenom() {
		return LivraisonNomPrenom;
	}
	
	
	public void setLivraisonNomPrenom(String livraisonNomPrenom) {
		LivraisonNomPrenom = livraisonNomPrenom;
	}


	public String getLIVADR1() {
		return LIVADR1;
	}


	public void setLIVADR1(String lIVADR1) {
		LIVADR1 = lIVADR1;
	}


	public String getLivraisonCPVille() {
		return LivraisonCPVille;
	}


	public void setLivraisonCPVille(String livraisonCPVille) {
		LivraisonCPVille = livraisonCPVille;
	}


	public String getLIBPAYS() {
		return LIBPAYS;
	}


	public void setLIBPAYS(String lIBPAYS) {
		LIBPAYS = lIBPAYS;
	}
	
	
	public String getLIVPAYS() {
		return LIVPAYS;
	}


	public void setLIVPAYS(String lIVPAYS) {
		LIVPAYS = lIVPAYS;
	}


	public String getCABUDP() {
		return CABUDP;
	}


	public void setCABUDP(String cABUDP) {
		CABUDP = cABUDP;
	}


	public String getIDORDER() {
		return IDORDER;
	}


	public void setIDORDER(String iDORDER) {
		IDORDER = iDORDER;
	}


	public String getTRPCONT() {
		return TRPCONT;
	}


	public void setTRPCONT(String tRPCONT) {
		TRPCONT = tRPCONT;
	}


	public String getDATECDE() {
		return DATECDE;
	}


	public void setDATECDE(String dATECDE) {
		DATECDE = dATECDE;
	}


	public String getArticleReference() {
		return articleReference;
	}


	public void setArticleReference(String articleReference) {
		this.articleReference = articleReference;
	}


	public String getArticleDesignation() {
		return articleDesignation;
	}


	public void setArticleDesignation(String articleDesignation) {
		this.articleDesignation = articleDesignation;
	}


	public String getArticleQteLivree() {
		return articleQteLivree;
	}


	public void setArticleQteLivree(String articleQteLivree) {
		this.articleQteLivree = articleQteLivree;
	}


	public String getCOLORIS() {
		return COLORIS;
	}


	public void setCOLORIS(String cOLORIS) {
		COLORIS = cOLORIS;
	}


	public String getTAILLE1() {
		return TAILLE1;
	}


	public void setTAILLE1(String tAILLE1) {
		TAILLE1 = tAILLE1;
	}


	public String getIDCANAL() {
		return IDCANAL;
	}


	public void setIDCANAL(String iDCANAL) {
		IDCANAL = iDCANAL;
	}


	public String getTYPECDE() {
		return TYPECDE;
	}


	public void setTYPECDE(String tYPECDE) {
		TYPECDE = tYPECDE;
	}

	public String getNoCde() {
		return noCde;
	}


	public void setNoCde(String noCde) {
		this.noCde = noCde;
	}


	public String getTransporteur() {
		return transporteur;
	}


	public void setTransporteur(String transporteur) {
		this.transporteur = transporteur;
	}


	public String getLivraisonLigneAdresse1() {
		return LivraisonLigneAdresse1;
	}


	public void setLivraisonLigneAdresse1(String livraisonLigneAdresse1) {
		LivraisonLigneAdresse1 = livraisonLigneAdresse1;
	}


	public String getLivraisonLigneAdresse2() {
		return LivraisonLigneAdresse2;
	}


	public void setLivraisonLigneAdresse2(String livraisonLigneAdresse2) {
		LivraisonLigneAdresse2 = livraisonLigneAdresse2;
	}


	public String getLivraisonLigneAdresse3() {
		return LivraisonLigneAdresse3;
	}


	public void setLivraisonLigneAdresse3(String livraisonLigneAdresse3) {
		LivraisonLigneAdresse3 = livraisonLigneAdresse3;
	}


	public String getLivraisonPays() {
		return LivraisonPays;
	}


	public void setLivraisonPays(String livraisonPays) {
		LivraisonPays = livraisonPays;
	}


	public String getIDCLIEN() {
		return IDCLIEN;
	}


	public void setIDCLIEN(String iDCLIEN) {
		IDCLIEN = iDCLIEN;
	}


	public String getDateExpedition() {
		return dateExpedition;
	}


	public void setDateExpedition(String dateExpedition) {
		this.dateExpedition = dateExpedition;
	}


	public String getDatevalidation() {
		return datevalidation;
	}


	public void setDatevalidation(String datevalidation) {
		this.datevalidation = datevalidation;
	}


	public String getArticleQteCommandee() {
		return articleQteCommandee;
	}


	public void setArticleQteCommandee(String articleQteCommandee) {
		this.articleQteCommandee = articleQteCommandee;
	}
   
}
