package xt.influenza;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Ce programme calcule l'ordre optimal des "upgrades" de mines de métal et de cristal,
 * dans le jeu OGame.
 * Quelques hypothèses simplificatrices ont été nécessaires :
 * - pas besoin d'énergie - on ignore l'aspect construction des centrales solaires
 * - le deutérium n'existe pas - on se focalise sur le métal et le cristal
 * - on ne se fait pas raider - on ne nous pique pas nos ressources
 * - dès que l'upgrade désiré est possible, il est lancé - on suppose que le joueur perd 0 seconde à cliquer
 * - dès qu'un upgrade est lancé, il est fini - on suppose les durées d'upgrade nulles
 * - on ne considère qu'une seule planète - on ignore les aspects entraide et transferts de ressources inter-planètes
 *
 * Luc Rousseau 2010
 */
public class OgameOptimizer
{
	static Map solutions = new TreeMap();
	
	public static void main(String[] args)
	{
		/*
		 * e0 est l'état initial. Dans le constructeur Etat(., ., ., ., .), on a :
		 * argument 1 : t, l'instant initial
		 * argument 2 : Nm, le niveau initial de la mine de métal
		 * argument 3 : Nc, le niveau initial de la mine de cristal
		 * argument 4 : Sm, le stock de métal disponible
		 * argument 5 : Sc, le stock de cristal disponible
		 * argument 6 : sequence, une chaîne de caractères représentant les choix effectués pour arriver dans cet état
		 */
		// Etat e0 = new Etat(0.0, 0, 0, 0.0, 0.0, "");

		/* simuler(e0, d) permet de calculer la meilleure séquence sur une durée de jeu d fixée (en heures) */
		// simuler(e0, 6.15);

		Etat e0 = new Etat(0.0, 24, 21, 0, 0, "");
		simuler(e0, 10000);

		Set scores = solutions.keySet();
		boolean worstScoreSet = false;
		double worstScore = 0.0;
		for (Object o : scores) {
			Double score = (Double) (o);
			if (! worstScoreSet) {
				worstScore = score;
				worstScoreSet = true;
			}
			String sequence = (String) (solutions.get(score));
			System.out.println(String.format("%s ==> +%.0fk", sequence, (score - worstScore) / 1000.0));
		}
	}
	
	private static void sous_simuler(Etat ei, double di, int minei, double dt)
	{
		if (di > dt)
		{
			ei.attendre(dt);
			Double score = ei.score();
			if (solutions.containsKey(score))
			{
				String sequence = (String)(solutions.get(score));
				solutions.put(score, sequence + " || " + ei.getSequence());
			}
			else
			{
				solutions.put(score, ei.getSequence());
			}
		}
		else
		{
			ei.attendre(di);
			ei.upgrader(minei);
			simuler(ei, dt - di);
		}
	}
	
	private static void simuler(Etat e0, double dt)
	{
		Etat e1 = e0.dup();
		Etat e2 = e0.dup();
		double d1 = e1.D1();
		double d2 = e2.D2();
		int mine1 = e1.mine1();
		int mine2 = e2.mine2();
		e2.addSequence("... ");
		
		sous_simuler(e1, d1, mine1, dt);
		sous_simuler(e2, d2, mine2, dt);
	}
}
