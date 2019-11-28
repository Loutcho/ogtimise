package xt.influenza;

// http://board.ogame.fr/thread.php?threadid=442461

public class Formule
{
	static double prod_a[] = { 30.0, 20.0 };
	static double prod_b[] = { 20.0, 10.0 }; 

	public static double productionHoraire(int ressource, int niveau)
	{
		return prod_a[ressource] * niveau * Math.pow(1.1, niveau) + prod_b[ressource];
	}
	
	static double cout_base[] = { 1.5, 1.6 };
	static double cout_coef[][] = { { 60.0, 15.0 }, { 48.0, 24.0 } };
	
	public static double cout(int enRessource, int deLaMineDe, int deNiveau)
	{
		return cout_coef[deLaMineDe][enRessource] * Math.pow(cout_base[deLaMineDe], deNiveau);
	}
	
	public static double points(int deLaMineDe, int deNiveau)
	{
		int niveauI;
		double accu = 0.0;
		for (niveauI = 0; niveauI < deNiveau; niveauI ++)
		{
			accu += cout(Ressource.METAL, deLaMineDe, niveauI) + cout(Ressource.CRISTAL, deLaMineDe, niveauI);
		}
		return accu;
	}
}
